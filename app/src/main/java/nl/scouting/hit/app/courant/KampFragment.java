package nl.scouting.hit.app.courant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.components.HitIconBarView;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.util.AvailableUtil;
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
		setOpenInschrijvingButton(kamp, view);

		return view;
	}

	private void setOpenInschrijvingButton(final HitKamp kamp, final View view) {
		Button openInschrijving = (Button) view.findViewById(R.id.openInschrijving);
		if (AvailableUtil.isNetworkAvailable(view.getContext()) && getHitProject().isInschrijvingGeopend()) {
			openInschrijving.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(getHitProject().getShantiUrl() + kamp.getShantiId()));
					startActivity(intent);
				}
			});
		} else {
			// Verstop de knop
			openInschrijving.setVisibility(View.INVISIBLE);
		}
	}

	private HitKamp getHitKamp() {
		final long id = getArguments().getLong(PARAM_ID);
		return getHitProject().getHitKampById(id);
	}

	private HitProject getHitProject() {
		return ((HitProjectContainable) getActivity()).getHitProject();
	}

	private void setTitle(final HitKamp kamp, final View view) {
		final TextView tv = setText(view, R.id.naam, kamp.getNaam());
		FontUtil.setTypeface(view, R.id.naam);
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Toast toast = Toast.makeText(view.getContext(), kamp.getVolTekst(), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
			}
		});

		// Vol indicator
		if (kamp.isVol()) {
			final TextView vol = (TextView) view.findViewById(R.id.vol_indicator);
			vol.setVisibility(View.VISIBLE);
		}
		// Nummering
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