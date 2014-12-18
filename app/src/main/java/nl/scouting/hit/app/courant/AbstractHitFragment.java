package nl.scouting.hit.app.courant;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 */
public abstract class AbstractHitFragment extends Fragment {

	/**
	 * Opens a url in a browser.
	 */
	protected class OpenBrowserOnClickListener implements View.OnClickListener {

		private final String url;

		public OpenBrowserOnClickListener(String url) {
			this.url = url;
		}

		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
		}
	}
}
