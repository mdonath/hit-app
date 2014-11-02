package nl.scouting.hit.app.courant;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.components.HitIconBarView;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.style.HitColor;
import nl.scouting.hit.app.util.FontUtil;
import nl.scouting.hit.app.util.TextUtil;

import static nl.scouting.hit.app.util.TextUtil.setText;

/**
 * Shows the info for a 'Kamponderdeel'.
 */
public class KampFragment extends Fragment {

	public static final String PARAM_ID = "courant.kamp.id";

	public View onCreateView(final LayoutInflater inflater,
							 final ViewGroup container,
							 final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_courant_kamp, container, false);

		final HitKamp kamp = getHitKamp();

		setTitle(kamp, view);
		setIconsbar(kamp, view);
		setInfobar(kamp, view);
		setCourantTekst(kamp, view);

		return view;
	}

	private HitKamp getHitKamp() {
		final long id = getArguments().getLong(PARAM_ID);
		return ((HitProjectContainable) getActivity()).getHitProject().getHitKampById(id);
	}

	private void setTitle(final HitKamp kamp, final View view) {
		final TextView tv = setText(view, R.id.naam, kamp.getNaam());
		FontUtil.setTypeface(view, R.id.naam);
		setText(view, R.id.index, String.valueOf(kamp.getKampIndex() + "/" + kamp.getPlaats().getProject().getKampen().size()));
	}

	private void setIconsbar(final HitKamp kamp, final View view) {
		final HitIconBarView iconsView = (HitIconBarView) view.findViewById(R.id.icons);
		iconsView.init(kamp.getIcoontjes());
	}

	private void setInfobar(final HitKamp kamp, final View view) {
		setText(view, R.id.plaats, kamp.getPlaats().getNaam());
		setText(view, R.id.dagen, kamp.formatPeriode());
		setText(view, R.id.leeftijd, kamp.formatLeeftijd());
		setText(view, R.id.groep, kamp.getSubgroep());
		setText(view, R.id.prijs, view.getContext().getString(R.string.prijs, kamp.getDeelnamekosten()));
	}

	private void setCourantTekst(final HitKamp kamp, final View view) {
		setText(view, R.id.courantTekst, TextUtil.cleanUp(kamp.getHitCourantTekst()));
	}
}
