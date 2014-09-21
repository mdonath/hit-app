package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitPlaats;
import nl.scouting.hit.app.services.AvailableUtil;
import nl.scouting.hit.app.services.TextUtil;

/**
 * Shows the information for a 'HIT-plaats'.
 */
public class Plaats extends Fragment {

	public static final String PARAM_ID = "courant.plaats.id";
	public static final String PARAM_NAAM = "courant.plaats.naam";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_courant_plaats, container, false);

		final HitPlaats plaats = getHitPlaats();

		setTitle(inflater, plaats, view);
		setCourantTekst(inflater, plaats, view);
		setKampen(inflater, plaats, view);
		setWeblink(inflater, plaats, view);

		return view;
	}

	private HitPlaats getHitPlaats() {
		final long id = getArguments().getLong(PARAM_ID);
		return ((Main) getActivity()).getHitProject().getHitPlaatsById(id);
	}

	private void setTitle(LayoutInflater inflater, HitPlaats plaats, View view) {
		final TextView tv = (TextView) view.findViewById(R.id.naam);
		tv.setText(inflater.getContext().getString(R.string.courant_plaats_title, plaats.getNaam()));
	}

	private void setWeblink(final LayoutInflater inflater, final HitPlaats plaats, final View view) {
		final TextView tv = (TextView) view.findViewById(R.id.weblink);
		String url;
		if (AvailableUtil.isNetworkAvailable(inflater.getContext())) {
			url = "https://hit.scouting.nl/" + plaats.getNaam().toLowerCase() + "/";
		} else {
			url = "";
		}
		tv.setText(url);
	}

	private void setCourantTekst(LayoutInflater inflater, HitPlaats plaats, View view) {
		final TextView courantTekst = (TextView) view.findViewById(R.id.courantTekst);

		courantTekst.setText(TextUtil.cleanUp(plaats.getHitCourantTekst()));
	}

	private void setKampen(LayoutInflater inflater, HitPlaats plaats, View view) {

		final List<String> list = new ArrayList<String>();
		for (HitKamp kamp : plaats.getKampen()) {
			list.add(kamp.getNaam());
		}

		final ArrayAdapter adapter = new ArrayAdapter(
				getActivity().getApplicationContext()
				, R.layout.list_plaats_kampen
				, R.id.naam
				, list.toArray(new String[list.size()])
		);

		final ListView listview = (ListView) view.findViewById(R.id.kampen);
		listview.setAdapter(adapter);
	}
}
