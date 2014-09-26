package nl.scouting.hit.app.courant;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitIcon;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.style.HitColor;
import nl.scouting.hit.app.util.TextUtil;

import static nl.scouting.hit.app.util.TextUtil.setText;

/**
 * Shows the info for a 'Kamponderdeel'.
 */
public class Kamp extends Fragment {

	public static final String PARAM_ID = "courant.kamp.id";
	private static final String TAG = "KampFragment";

	public View onCreateView(final LayoutInflater inflater,
							 final ViewGroup container,
							 final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_courant_kamp, container, false);

		final HitKamp kamp = getHitKamp();

		setTitle(inflater, kamp, view);
		setIconsbar(inflater, kamp, view);
		setInfobar(inflater, kamp, view);
		setCourantTekst(inflater, kamp, view);

		return view;
	}

	private HitKamp getHitKamp() {
		final long id = getArguments().getLong(PARAM_ID);
		return ((HitProjectContainable) getActivity()).getHitProject().getHitKampById(id);
	}

	private void setTitle(LayoutInflater inflater, HitKamp kamp, View view) {
		TextView tv = setText(view, R.id.naam, kamp.getNaam());
		tv.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/impact.ttf"));
		tv.setTextColor(HitColor.RED.getColorValue());
		setText(view, R.id.index, String.valueOf(kamp.getKampIndex() + "/" + kamp.getPlaats().getProject().getKampen().size()));
	}

	private void setIconsbar(final LayoutInflater inflater, final HitKamp kamp, final View view) {
		final LinearLayout iconsView = (LinearLayout) view.findViewById(R.id.icons_container);

		for (final HitIcon icon : kamp.getIcoontjes()) {
			iconsView.addView(createImageView(inflater, icon));
		}
	}

	private ImageView createImageView(final LayoutInflater inflater, final HitIcon icon) {
		final ImageView child = new ImageView(inflater.getContext());
		child.setImageResource(icon.getResId());
		return child;
	}

	private void setInfobar(LayoutInflater inflater, HitKamp kamp, View view) {
		setText(view, R.id.dagen, kamp.formatPeriode());
		setText(view, R.id.leeftijd, kamp.formatLeeftijd());
		setText(view, R.id.groep, kamp.getSubgroep());
		setText(view, R.id.prijs, inflater.getContext().getString(R.string.prijs, kamp.getDeelnamekosten()));
	}

	private void setCourantTekst(LayoutInflater inflater, HitKamp kamp, View view) {
		setText(view, R.id.courantTekst, TextUtil.cleanUp(kamp.getHitCourantTekst()));
	}
}
