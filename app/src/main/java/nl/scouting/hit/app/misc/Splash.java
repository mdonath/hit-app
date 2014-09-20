package nl.scouting.hit.app.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager;

import static nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager.isLocalDataAvailable;
import static nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager.isNetworkAvailable;
import static nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager.isUpdateNeeded;
import static nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager.startDownload;

/**
 * Shows a splashscreen for a few seconds with logo, name and version.
 */
public class Splash extends Activity {

	private static final String TAG = "Splash";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar and notification bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Setup splashscreen
		setContentView(R.layout.splash);

		// Retrieve version from manifest
		final TextView appVersion = (TextView) findViewById(R.id.app_version);
		appVersion.setText(getApplicationVersion());

		checkForUpdates();

		doorsturen();
	}

	private void checkForUpdates() {
		if (isNetworkAvailable(this)) {
			Log.i(TAG, "Er is netwerk");
			if (isLocalDataAvailable(this)) {
				Log.i(TAG, "Er is al een bestand");
				if (isUpdateNeeded(this)) {
					Log.i(TAG, "Het bestand moet nu bijgewerkt worden");
					updateSplashStatus("Controleer op updates...");
					startDownload(this, "https://hit.scouting.nl/index.php?option=com_kampinfo&task=hitapp.generate");
				} else {
					Log.i(TAG, "Het bestand hoeft nog niet bijgewerkt te worden");
					updateSplashStatus("Gegevens zijn nog actueel genoeg");
				}
			} else {
				Log.i(TAG, "Het bestand moet voor de eerste keer opgehaald worden");
				updateSplashStatus("Eerste keer downloaden...");
				startDownload(this, "https://hit.scouting.nl/index.php?option=com_kampinfo&task=hitapp.generate");
			}
		} else {
			Log.i(TAG, "Er is geen netwerk");
			if (isLocalDataAvailable(this)) {
				// doe niets,
				Log.i(TAG, "Er staat al een bestand en kan nu niet bijwerken, doe nu niets");
				updateSplashStatus("Geen netwerk gevonden, bestaande gegevens worden getoond");
			} else {
				Log.i(TAG, "Copieer voor het eerst de met de app meegeleverde data");
				if (KampInfoDownloadViaDownloadManager.copyFromAssetsToLocalData(this)) {
					updateSplashStatus("Geen netwerk gevonden, met app meegeleverde gegevens worden getoond");
				} else {
					Log.e(TAG, "Copiëren data ging fout?!");
					updateSplashStatus("Geen netwerk gevonden, met app meegeleverde gegevens worden getoond");
				}
			}
		}
	}

	/**
	 * Retrieves version from manifest.
	 *
	 * @return full text for textview with version indication
	 */
	private String getApplicationVersion() {
		String versionName;
		final Context context = getApplicationContext();
		try {
			versionName = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			versionName = "?";
		}
		return context.getString(R.string.app_version, versionName);
	}

	private void doorsturen() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				updateSplashStatus("Klaar");
				startActivity(new Intent(Splash.this, Main.class));
				finish();
			}
		}, 5000);
	}

	private void updateSplashStatus(final String newStatus) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				TextView statusTextView = (TextView) findViewById(R.id.status);
				statusTextView.setText(newStatus);
				Log.d("Splash", "Status aangepast: " + newStatus);
			}
		});
	}
}
