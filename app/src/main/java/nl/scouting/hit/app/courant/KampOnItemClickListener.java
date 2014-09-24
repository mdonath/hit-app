package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;

/**
 * Created by martijn on 24-9-14.
 */
public class KampOnItemClickListener implements AdapterView.OnItemClickListener {
	private static final String TAG = "KampOneIteem";
	private Fragment context;

	public KampOnItemClickListener(Fragment context) {
		this.context = context;
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		HitKamp kamp = (HitKamp) parent.getItemAtPosition(position);
		Log.i(TAG, "Kamp gevonden: " + kamp);
		Fragment fragment = new Kamp();
		Bundle bundle = new Bundle();
		bundle.putLong(Kamp.PARAM_ID, kamp.getId());
		fragment.setArguments(bundle);
		context.getFragmentManager()
				.beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack(null)
				.commit();
	}
}
