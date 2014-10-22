package nl.scouting.hit.app.courant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;

/**
 * Created by martijn on 10-10-14.
 */
public class Kiezer extends Fragment {
	public View onCreateView(final LayoutInflater inflater,
							 final ViewGroup container,
							 final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_courant_kiezer, container, false);

		return view;
	}

}
