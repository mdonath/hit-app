package nl.scouting.hit.app.services;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Downloads new data using the Android DownloadManager.
 */
public final class KampInfoDownloadViaDownloadManager {

	private static final String TAG = "KampInfoDownloadViaDownloadManager";

	private static final String HIT_COURANT_ASSET_DATA = "hitcourant.json";
	private static final String HIT_COURANT_LOCAL_DATA = "HitApp/Courant/" + HIT_COURANT_ASSET_DATA;

	private Context context;
	private String url;

	private StatusListener statusListener = StatusListener.NULL_LISTENER;

	/**
	 * Interface for communicating status messages.
	 */
	public interface StatusListener {
		void update(String message);

		/** Null implementation of this interface. */
		StatusListener NULL_LISTENER = new StatusListener() {
			@Override
			public void update(String message) {
				// NOP (NO Operation == it does nothing)
			}
		};
	}

	public KampInfoDownloadViaDownloadManager(Context context) {
		this.context = context;
	}

	/**
	 * Registers a StatusListener.
	 *
	 * @param statusListener
	 * @return this
	 */
	public KampInfoDownloadViaDownloadManager addListener(StatusListener statusListener) {
		this.statusListener = statusListener;
		return this; // fluent
	}

	/**
	 * Sets the download url on hit.scouting.nl.
	 *
	 * @param url
	 * @return this
	 */
	public KampInfoDownloadViaDownloadManager setDownloadUrl(String url) {
		this.url = url;
		return this; // fluent
	}

	/**
	 * Updates the data file.
	 */
	public void update() {
		if (url == null || url.isEmpty()) {
			statusListener.update("Ik weet niet waar ik het moet zoeken");
			Log.e(TAG, "Geen url geconfigureerd!");
		} else {
			Uri uri = Uri.parse(url);

			if (isNetworkAvailable(context)) {
				Log.i(TAG, "Er is netwerk");
				if (isLocalDataAvailable()) {
					Log.i(TAG, "Er is al een bestand");
					if (isUpdateNeeded()) {
						Log.i(TAG, "Het bestand moet nu bijgewerkt worden");
						statusListener.update("Controleer op updates...");
						startDownload(uri);
					} else {
						Log.i(TAG, "Het bestand hoeft nog niet bijgewerkt te worden");
						statusListener.update("Gegevens zijn nog actueel genoeg");
					}
				} else {
					Log.i(TAG, "Het bestand moet voor de eerste keer opgehaald worden");
					statusListener.update("Eerste keer downloaden...");
					startDownload(uri);
				}
			} else {
				Log.i(TAG, "Er is geen netwerk");
				if (isLocalDataAvailable()) {
					// doe niets,
					Log.i(TAG, "Er staat al een bestand en kan nu niet bijwerken, doe nu niets");
					statusListener.update("Geen netwerk gevonden, bestaande gegevens worden getoond");
				} else {
					Log.i(TAG, "Copieer voor het eerst de met de app meegeleverde data");
					if (copyFromAssetsToLocalData()) {
						statusListener.update("Geen netwerk gevonden, met app meegeleverde gegevens worden getoond");
					} else {
						Log.e(TAG, "Copiëren data ging fout?!");
						statusListener.update("Geen netwerk gevonden, met app meegeleverde gegevens worden getoond");
					}
				}
			}
		}
	}

	/**
	 * Checks the availability of the DownloadManager.
	 *
	 * @param context used to update the device version and DownloadManager information
	 * @return true if the download manager is available
	 */
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
			return !list.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks for the availability of the Network.
	 *
	 * @param context
	 * @return true if network is available
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * @return the location of the local data file itself.
	 */
	public static File getLocalDataFile() {
		return new File( //
				Environment.getExternalStorageDirectory() //
				, HIT_COURANT_LOCAL_DATA);
	}

	/**
	 * Checks whether an update of the data is needed.
	 *
	 * @return true if an update is needed
	 */
	private boolean isUpdateNeeded() {
		boolean result = false;
		if (isNetworkAvailable(context) && isDownloadManagerAvailable(context)) {
			if (isLocalDataAvailable()) {
				result = isLastUpdateAtLeastOneHourAgo();
			} else {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Checks the timestamp of the local data and reports if it is longer than an hour ago.
	 *
	 * @return true if update occurred more than an hour ago.
	 */
	private boolean isLastUpdateAtLeastOneHourAgo() {
		long lastUpdated = getLocalDataFile().lastModified();
		long now = System.currentTimeMillis();
		return Math.abs(now - lastUpdated) > 1000 * 60 * 60;
	}

	/**
	 * Checks for the existence of the local data file.
	 *
	 * @return true if the local data file exists.
	 */
	private boolean isLocalDataAvailable() {
		return getLocalDataFile().exists();
	}

	/**
	 * Copies the prepackaged data file to the local data file location.
	 *
	 * @return true if successfully copied
	 */
	private boolean copyFromAssetsToLocalData() {
		File localDataFile = getLocalDataFile();
		checkParentAndCreate(localDataFile);
		return copyAsset(context.getAssets(), HIT_COURANT_ASSET_DATA, localDataFile);
	}

	private void checkParentAndCreate(File localDataFile) {
		File parent = localDataFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	/**
	 * Copies an asset to given <code>toPath</code>.
	 *
	 * @param assetManager
	 * @param fromAssetPath
	 * @param toPath
	 * @return true if file has been copied successfully.
	 */
	private boolean copyAsset(final AssetManager assetManager,
							  final String fromAssetPath, final File toPath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(fromAssetPath);
			out = new FileOutputStream(toPath);
			copyFile(in, out);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Copies an inputstream to an outputstream.
	 *
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void copyFile(final InputStream in, final OutputStream out) throws IOException {
		final byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	/**
	 * Starts the download from the remote server.
	 *
	 * @param uri
	 */
	private void startDownload(final Uri uri) {
		final DownloadManager.Request request = new DownloadManager.Request(uri);
		request.setDescription("Nu wordt de laatste versie van de kampcourant opgehaald");
		request.setTitle("Ophalen laatste gegevens HIT Courant");
		// in order for this if to run, you must use the android 3.2 to compile your app
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		}
		final File localDataFile = getLocalDataFile();
		request.setDestinationUri(Uri.fromFile(localDataFile));

		// Create destination
		checkParentAndCreate(localDataFile);

		// get download service
		final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		// and enqueue file
		manager.enqueue(request);
	}
}
