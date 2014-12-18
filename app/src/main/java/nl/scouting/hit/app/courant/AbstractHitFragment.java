package nl.scouting.hit.app.courant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

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

            //Duurde soms even voordat de Browser reageert, geef even kort feedback dat er wat is gebeurd
            Toast.makeText(getActivity(), "Link is geopend",
                    Toast.LENGTH_SHORT).show();

            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(250);

		}
	}
}
