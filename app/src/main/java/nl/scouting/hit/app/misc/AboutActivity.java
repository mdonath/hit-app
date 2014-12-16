package nl.scouting.hit.app.misc;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import nl.scouting.hit.app.R;

/**
 * Shows the About screen, displays contributors and ThankYou's.
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		final WebView meegewerkt = (WebView) findViewById(R.id.about_meegewerkt);

		String data = "<html><body style=\"margin:20px; padding:0px; text-align: center;\">"
				+ "<h1>Aan deze app hebben meegewerkt:</h1>"
				+ "<h2>Ontwikkelaars:</h1>"
				+ "<p>Martijn Donath (<a href=\"https://hit.scouting.nl\">hit.scouting.nl</a>)</p>" //
				+ "<p>Bastiaan Willem de Vries (<a href=\"http://www.bwebdesign.nl\">bwebdesign.nl</a>)</p>" //
				+ "<h2>Testers:</h2>"
				+ "<p>Ivo</p>" //
				+ "<p>Sven</p>" //
				+ "<p>Iris</p>" //
				+ "</ul>" //
				+ "<p><strong>Kijk voor meer informatie op <a href=\"https://hit.scouting.nl\">https://hit.scouting.nl</a>.</strong></p>"
				+ "</body></html>"
				;
		meegewerkt.setBackgroundColor(0);
		meegewerkt.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", null);
	}
}
