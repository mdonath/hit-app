package nl.scouting.hit.app.courant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.components.ExpandableHeightListView;
import nl.scouting.hit.app.model.AbstractHitEntity;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitKampRO;
import nl.scouting.hit.app.model.HitPlaats;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.style.PlaatsStyle;
import nl.scouting.hit.app.util.AvailableUtil;
import nl.scouting.hit.app.util.TextUtil;

import static nl.scouting.hit.app.util.TextUtil.setText;

/**
 * Shows the information for a 'HIT-plaats'.
 */
public class PlaatsFragment extends Fragment implements AdapterView.OnItemClickListener {

	public static final String PARAM_ID = "courant.plaats.id";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_courant_plaats, container, false);

		final HitPlaats plaats = getHitPlaats();

		setTitle(plaats, view);
		setLogo(plaats, view);
		setWeblink(plaats, view);

		setCourantTekst(plaats, view);

		setKampen(plaats, view);

		return view;
	}

	private HitPlaats getHitPlaats() {
		final long id = getArguments().getLong(PARAM_ID);
		return getHitContainer().getHitProject().getHitPlaatsById(id);
	}

	private HitProjectContainable getHitContainer() {
		return ((HitProjectContainable) getActivity());
	}

	private void setTitle(final HitPlaats plaats, final View view) {
		setText(view, R.id.naam, view.getContext().getString(R.string.courant_plaats_title, plaats.getNaam()));
	}

	private void setLogo(final HitPlaats plaats, final View view) {
		final ImageView iv = (ImageView) view.findViewById(R.id.logo);
		iv.setImageResource(PlaatsStyle.by(plaats).logoBig);
	}

	private void setWeblink(final HitPlaats plaats, final View view) {
		String url;
		if (AvailableUtil.isNetworkAvailable(view.getContext())) {
			url = "https://hit.scouting.nl/" + plaats.getNaam().toLowerCase() + "/";
		} else {
			url = "";
		}
		setText(view, R.id.weblink, url);
	}

	private void setCourantTekst(final HitPlaats plaats, final View view) {
		setText(view, R.id.courantTekst, TextUtil.cleanUp(plaats.getHitCourantTekst()));
	}

	private void setKampen(final HitPlaats plaats, final View view) {
		TextView label = (TextView) view.findViewById(R.id.kampen_label);
		label.setText(view.getContext().getString(R.string.kampen_in_plaats_label, plaats.getNaam()));

		final List<HitKamp> list = new ArrayList<HitKamp>();
		for (HitKamp kamp : plaats.getKampen()) {
			list.add(new HitKampRO(kamp) {
				@Override
				public String toString() {
					return getNaam();
				}
			});
		}

		final ArrayAdapter<HitKamp> adapter = new ArrayAdapter<HitKamp>(
				getActivity().getApplicationContext()
				, R.layout.list_plaats_kampen
				, R.id.naam
				, list
		);
		final ExpandableHeightListView listview = (ExpandableHeightListView) view.findViewById(R.id.kampen);
		listview.setExpanded(true);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		getHitContainer().show((AbstractHitEntity) parent.getItemAtPosition(position));
	}
}
