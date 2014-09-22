package nl.scouting.hit.app.courant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.scouting.hit.app.Main;
import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitIcon;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.util.TextUtil;

/**
 * Shows the info for a 'Kamponderdeel'.
 */
public class Kamp extends Fragment {

	public static final String PARAM_ID = "courant.kamp.id";
	private static final String TAG = "KampFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_courant_kamp, container, false);

		HitKamp kamp = getHitKamp();

		setTitle(inflater, kamp, view);
		setCourantTekst(inflater, kamp, view);
		setInfobar(inflater, kamp, view);
		setIconsbar(inflater, kamp, view);

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

	private void setIconsbar(final LayoutInflater inflater, final HitKamp kamp, final View view) {
		LinearLayout iconsView = (LinearLayout) view.findViewById(R.id.icons_container);

		for (HitIcon icon : kamp.getIcoontjes()) {
			ImageView child = new ImageView(inflater.getContext());
			child.setImageResource(icon.getResId());
			iconsView.addView(child);
		}
	}

	private void setInfobar(LayoutInflater inflater, HitKamp kamp, View view) {
		((TextView) view.findViewById(R.id.dagen)).setText(kamp.formatPeriode());
		((TextView) view.findViewById(R.id.leeftijd)).setText(kamp.formatLeeftijd());
		((TextView) view.findViewById(R.id.groep)).setText(formatGroep(kamp));
		((TextView) view.findViewById(R.id.prijs)).setText(inflater.getContext().getString(R.string.prijs, kamp.getDeelnamekosten()));
	}

	private String formatGroep(final HitKamp kamp) {
		return kamp.getSubgroep();
	}

	private void setCourantTekst(LayoutInflater inflater, HitKamp kamp, View view) {
		TextView info = (TextView) view.findViewById(R.id.courantTekst);
		info.setText(TextUtil.cleanUp(kamp.getHitCourantTekst()));
	}
}
