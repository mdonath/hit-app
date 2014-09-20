package nl.scouting.hit.app.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.services.KampInfoDownloadViaDownloadManager;

/**
 * Shows a splashscreen for a few seconds with logo, name and version.
 */
public class Splash extends Activity implements KampInfoDownloadViaDownloadManager.StatusListener {

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

	@Override
	public void update(String message) {
		updateSplashStatus(message);
	}

	private void checkForUpdates() {
		new KampInfoDownloadViaDownloadManager(this)
				.addListener(this)
				.setDownloadUrl("https://hit.scouting.nl/index.php?option=com_kampinfo&task=hitapp.generate")
				.update();
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
			}
		});
	}
}
