package nl.scouting.hit.app.services;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static nl.scouting.hit.app.util.AvailableUtil.isDownloadManagerAvailable;
import static nl.scouting.hit.app.util.AvailableUtil.isNetworkAvailable;

/**
 * Downloads new data using the Android DownloadManager.
 */
public final class KampInfoDownloadViaDownloadManager {

	private static final String TAG = "KampInfoDownloadViaDownloadManager";

	private static final String HIT_COURANT_ASSET_DATA = "hitcourant.json";
	public static final String APP_PATH = "HitApp/Courant/";
	private static final String HIT_COURANT_LOCAL_DATA = APP_PATH + HIT_COURANT_ASSET_DATA;
	private static final String HIT_COURANT_TEMP_DATA = APP_PATH + HIT_COURANT_ASSET_DATA + ".tmp";

	private static final String DL_ID = "downloadId";
	private final SharedPreferences prefs;
	private final DownloadManager manager;

	private StatusListener statusListener = StatusListener.NULL_LISTENER;
	private Context context;
	private String url;
	private long updateInterval = 1000 * 60 * 60;

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
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		// get download service
		manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
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
	 * Sets the download url on hit.scouting.nl.
	 *
	 * @param updateInterval
	 * @return this
	 */
	public KampInfoDownloadViaDownloadManager setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
		return this; // fluent
	}

	/**
	 * Updates the data file.
	 */
	public KampInfoDownloadViaDownloadManager update() {
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
		return this;
	}

	/**
	 * @return the location of the local data file itself.
	 */
	public static File getLocalDataFile() {
		return new File( //
				Environment.getExternalStorageDirectory() //
				, HIT_COURANT_LOCAL_DATA);
	}

	private static File getLocalTempFile() {
		return new File( //
				Environment.getExternalStorageDirectory() //
				, HIT_COURANT_TEMP_DATA);
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
		return Math.abs(now - lastUpdated) > updateInterval;
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
		final File localTempFile = getLocalTempFile();
		checkParentAndCreate(localTempFile);

		final DownloadManager.Request request = new DownloadManager.Request(uri)
				.setDescription("Nu wordt de laatste versie van de kampcourant opgehaald")
				.setTitle("Ophalen laatste gegevens HIT Courant")
				.setDestinationUri(Uri.fromFile(localTempFile));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		}

		// and enqueue file
		long id = manager.enqueue(request);
		prefs.edit()
				.putLong(DL_ID, id)
				.apply();

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				queryDownloadStatus();
			}
		};
		context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	private BroadcastReceiver receiver;

	public void stahp() {
		if (receiver != null) {
			context.unregisterReceiver(receiver);
			receiver = null;
		}
	}

	private void queryDownloadStatus() {
		Log.i(TAG, "queryDownloadStatus");
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(prefs.getLong(DL_ID, 0));
		Cursor c = manager.query(query);
		if (c.moveToFirst()) {
			int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
			Log.d(TAG, "Status Check: " + status);
			switch (status) {
				case DownloadManager.STATUS_PAUSED:
				case DownloadManager.STATUS_PENDING:
				case DownloadManager.STATUS_RUNNING:
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					try {
						Log.i(TAG, "Download was succesvol");
						long id = prefs.getLong(DL_ID, 0);
						ParcelFileDescriptor file = manager.openDownloadedFile(id);
						FileInputStream fis = new ParcelFileDescriptor.AutoCloseInputStream(file);
						File localDataFile = getLocalDataFile();
						copyFile(fis, new FileOutputStream(localDataFile));
						Log.i(TAG, "copy file naar " + localDataFile);
						removeIdFromPrefs();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case DownloadManager.STATUS_FAILED:
					Log.i(TAG, "Download is mislukt!");
					removeIdFromPrefs();
					break;
			}
		}
	}

	private void removeIdFromPrefs() {
		manager.remove(prefs.getLong(DL_ID, 0));
		prefs.edit().clear().commit();
	}
}
