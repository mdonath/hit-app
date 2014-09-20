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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Downloads new data using the Android DownloadManager.
 */
public class KampInfoDownloadViaDownloadManager {

	public static final String HIT_COURANT_ASSET_DATA = "hitcourant.json";
	public static final String HIT_COURANT_LOCAL_DATA = "HitApp/Courant/" + HIT_COURANT_ASSET_DATA;

	/**
	 * Checks the availability of the DownloadManager.
	 *
	 * @param context used to check the device version and DownloadManager information
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
	 * Checks whether an update of the data is needed.
	 *
	 * @param context
	 * @return true if an update is needed
	 */
	public static boolean isUpdateNeeded(Context context) {
		boolean result = false;
		if (isNetworkAvailable(context) && isDownloadManagerAvailable(context)) {
			if (isLocalDataAvailable(context)) {
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
	private static boolean isLastUpdateAtLeastOneHourAgo() {
		long lastUpdated = getLocalDataFile().lastModified();
		long now = System.currentTimeMillis();
		return Math.abs(now - lastUpdated) > 1000 * 60 * 60;
	}

	/**
	 * Checks for the existence of the local data file.
	 *
	 * @param context
	 * @return true if the local data file exists.
	 */
	public static boolean isLocalDataAvailable(Context context) {
		return getLocalDataFile().exists();
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
	 * Copies the prepackaged data file to the local data file location.
	 *
	 * @param context
	 * @return true if successfully copied
	 */
	public static boolean copyFromAssetsToLocalData(Context context) {
		File localDataFile = getLocalDataFile();
		File parent = localDataFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		return copyAsset(context.getAssets(), HIT_COURANT_ASSET_DATA, localDataFile);
	}

	/**
	 * Copies an asset to given <code>toPath</code>.
	 *
	 * @param assetManager
	 * @param fromAssetPath
	 * @param toPath
	 * @return true if file has been copied successfully.
	 */
	private static boolean copyAsset(final AssetManager assetManager,
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
	 * @param context
	 * @param url
	 */
	public static void startDownload(final Context context, final String url) {
		final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		request.setDescription("Nu wordt de laatste versie van de kampcourant opgehaald");
		request.setTitle("Ophalen laatste gegevens HIT Courant");
		// in order for this if to run, you must use the android 3.2 to compile your app
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}

		request.setDestinationInExternalFilesDir(context, null, HIT_COURANT_LOCAL_DATA);

		// get download service
		final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		// and enqueue file
		manager.enqueue(request);
	}
}
