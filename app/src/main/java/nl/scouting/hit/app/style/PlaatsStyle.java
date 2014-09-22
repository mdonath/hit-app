package nl.scouting.hit.app.style;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitPlaats;

/**
 * Information about the branding of type HitPlaats.
 */
public enum PlaatsStyle {
	ALPHEN(
			HitColor.RED, HitColor.YELLOW,
			R.drawable.hit_alp_logo_h_small, R.drawable.hit_alp_logo_h_big),
	BAARN(
			HitColor.RED, HitColor.BLUE,
			R.drawable.hit_baa_logo_h_small, R.drawable.hit_baa_logo_h_big),
	DWINGELOO(
			HitColor.GREEN, HitColor.RED,
			R.drawable.hit_dwi_logo_h_small, R.drawable.hit_dwi_logo_h_big),
	HARDERWIJK(
			HitColor.BLUE, HitColor.YELLOW,
			R.drawable.hit_har_logo_h_small, R.drawable.hit_har_logo_h_big),
	MOOK(
			HitColor.BLUE, HitColor.RED,
			R.drawable.hit_moo_logo_h_small, R.drawable.hit_moo_logo_h_big),
	ZEELAND(
			HitColor.GREEN, HitColor.BLUE,
			R.drawable.hit_zee_logo_h_small, R.drawable.hit_zee_logo_h_big);

	public final HitColor background;
	public final HitColor text;
	public final int logoSmall;
	public final int logoBig;

	PlaatsStyle(HitColor background, HitColor text, int logoSmall, int logoBig) {
		this.background = background;
		this.text = text;
		this.logoSmall = logoSmall;
		this.logoBig = logoBig;
	}

	public static PlaatsStyle by(HitPlaats plaats) {
		return PlaatsStyle.valueOf(plaats.getNaam().toUpperCase());
	}

	public static PlaatsStyle by(HitKamp kamp) {
		return by(kamp.getPlaats());
	}

}
