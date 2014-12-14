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
					"h1 {font-family: \"Impact\"; color: rgb(0,176,80); font-weight: normal;} " +
					"</style>" +
					"</head><body>" +
					"<h1>60 x HIT: Feest in de tent!</h1>" +
					"<p><b>Heb jij wel zin in een feestje, samen met ruim 2.000 andere scouts uit heel Nederland? Kom dan in het Paasweekend naar de HIT! Want meer dan zestig totaal verschillende, super spannende activiteiten wachten op jouw deelname. En dit jaar wordt het extra speciaal, want de HIT wordt voor de \t\t\t60ste keer gehouden. Dat mag je natuurlijk niet missen!</b></p>" +
					"<p>De HIT staat voor Hikes, Interessekampen en Trappersexpedities en wordt elk jaar gehouden tijdens de paasdagen. Een paar duizend scouts tussen de 7 en de 88 jaar beleven een ultieme Scoutingactiviteit waarin je alles kunt tegenkomen wat Scouting te bieden heeft.</p>" +
					"<p>In " + jaar + " vindt de HIT plaats tussen " + hitdagen + ".</p>" +
					"<p>Vanaf " + startInschrijving + " kun je inschrijven. In deze HIT-courant kun je alvast een kijkje nemen welke te gekke HIT-onderdelen er dit jaar voor jou en je scoutingvrienden georganiseerd worden. Wacht niet te lang met inschrijven, want elk HIT-onderdeel heeft maar een beperkt aantal plaatsen.</p>" +
					"<h1>Hoe kan ik me inschrijven?</h1>" +
					"<p>Vanaf " + startInschrijving + " is op de HIT-website (<a href=\"https://hit.scouting.nl\">https://hit.scouting.nl</a>) meer informatie te vinden over elke hike en elk kamp dat in deze HIT-courant genoemd staat.</p>" +
					"<p>Vanaf de site vind je ook meteen een link naar het inschrijfformulier. Log voor het inschrijven wel eerst even in op de website van Scouting Nederland. Kom je er niet uit? Een handleiding vind je op de website, en neem anders gerust contact op met de HIT-helpdesk.</p>" +
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


