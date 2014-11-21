package nl.scouting.hit.app.courant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.text.SimpleDateFormat;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;

/**
 * Shows the welcome information.
 */
public class ProjectFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_welcome, container, false);
		WebView webView = (WebView) view.findViewById(R.id.content);

		final HitProject project = getHitProject();
		final int jaar = project.getJaar();

		if (project.getPlaatsen() != null) {
			final int aantalHitPlaatsen = project.getPlaatsen().size();
			final int aantalHits = project.getKampen().size();

			String formatVrijdag;
			if (project.getVrijdag().getMonth() == project.getMaandag().getMonth()) {
				formatVrijdag = "EEEE d";
			} else {
				formatVrijdag = "EEEE d MMMM";
			}
			String start = new SimpleDateFormat(formatVrijdag).format(project.getVrijdag());
			String eind = new SimpleDateFormat("EEEE d MMMM").format(project.getMaandag());
			String hitdagen = start + " en " + eind;
			String startInschrijving = new SimpleDateFormat("d MMMM yyyy").format(project.getInschrijvingStartdatum());
			String eindeInschrijving = new SimpleDateFormat("d MMMM yyyy").format(project.getInschrijvingEinddatum());
			String data = "<html><head>" +
					"<style type=\"text/css\"> @font-face {\n" +
					" font-family: 'Impact';\n" +
					" src: url('fonts/impact.ttf') \n" +
					"}\n" +
					"h1 {font-family: \"Impact\"; color: red;} " +
					"</style>" +
					"</head><body>" +
					"<h1>HIT " + jaar + ": Een geweldige uitdaging!</h1>" +
					"<p><b>Wat ga jij doen met Pasen " + jaar + "? Naar de HIT natuurlijk! Want in " + jaar + " kun je bij de HIT kiezen uit " + aantalHits + " totaal verschillende, spannende en uitdagende activiteiten op " +
					aantalHitPlaatsen + " plaatsen in Nederland, of zelfs in het buitenland!</b></p>" +
					"<p>In " + jaar + " is het landelijke thema \"Waterproof\". Maar tijdens de HIT houden we het zeker droog. Door het hele land organiseren we een \"Dijk van een HIT!\" De HIT staat voor Hikes, Interessekampen en Trappersexpedities en wordt elk jaar gehouden tijdens de paasdagen." +
					" Meer dan tweeduizend scouts tussen de 7 en de 88 jaar beleven een fantastische Scoutingactiviteit waarin je alles kunt tegenkomen wat Scouting te bieden heeft.</p>" +
					"<p>In " + jaar + " vindt de HIT plaats tussen " + hitdagen +
					". Vanaf " + startInschrijving + " kun je weer inschrijven. Lees snel verder in deze HIT-courant of kijk op de website welke te gekke HIT voor jou en je Scoutingvrienden er dit jaar weer bij zit!</p>" +
					"<h1>Hoe kan ik me inschrijven?</h1>" +
					"<p>Inschrijven doe je de HIT-website <a href=\"https://hit.scouting.nl\">https://hit.scouting.nl</a>.</p>" +
					"<p>Ga naar de pagina van het HIT-onderdeel van je keuze, en klik onderaan op \"direct inschrijven\" om het inschrijfformulier te openen. Zorg wel dat je je Scouts Online inloggegevens bij de hand hebt.</p>" +
					"<p>Ook dit jaar is het zo dat je meteen moet betalen met iDEAL via Internetbankieren. Op de HIT-website vind je een uitgebreide handleiding. Ook kun je via de website contact opnemen met de HIT-helpdesk.</p>" +
					"<p><b>Inschrijven kan vanaf <span style=\"color:green\">" + startInschrijving + "</span>. Maar wacht er niet te lang mee, want sommige HIT-onderdelen zitten al heel snel vol. De inschrijving sluit op <span style=\"color:red\">" + eindeInschrijving + "</span>!</b></p>" +
					"</body></html>";
			webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", null);
		}
		return view;
	}

	private HitProjectContainable getHitContainer() {
		return ((HitProjectContainable) getActivity());
	}

	private HitProject getHitProject() {
		return getHitContainer().getHitProject();
	}
}


