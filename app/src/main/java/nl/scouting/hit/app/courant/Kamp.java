package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.util.TextUtil;

/**
 * Shows the info for a 'Kamponderdeel'.
 */
public class Kamp extends Fragment {

	public static final String PARAM_ID = "courant.kamp.id";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_courant_kamp, container, false);

		HitKamp kamp = getHitKamp();

		setTitle(inflater, kamp, view);
		setInfo(inflater, kamp, view);

		return view;
	}

	private HitKamp getHitKamp() {
		long id = getArguments().getLong(PARAM_ID);
		return ((Main) getActivity()).getHitProject().getHitKampById(id);
	}

	private void setTitle(LayoutInflater inflater, HitKamp kamp, View view) {
		TextView title = (TextView) view.findViewById(R.id.naam);
		title.setText(kamp.getNaam());
	}

	private void setInfo(LayoutInflater inflater, HitKamp kamp, View view) {
		TextView info = (TextView) view.findViewById(R.id.courantTekst);
		info.setText(TextUtil.cleanUp(kamp.getHitCourantTekst()));
	}
}
