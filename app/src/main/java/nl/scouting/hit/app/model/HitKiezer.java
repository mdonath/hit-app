package nl.scouting.hit.app.model;

/**
 * Created by martijn on 2-11-14.
 */
public class HitKiezer extends AbstractHitEntity {
	@Override
	public HitEntityEnum getType() {
		return HitEntityEnum.KIEZER;
	}

	@Override
	public String getLabel() {
		return "HitKiezer-label";
	}

	@Override
	public String getShareText() {
		// FIXME jaartal ophalen uit HitProject
		return "Maak ook een keuze met de HIT Kiezer. Kijk voor meer info op https://hit.scouting.nl/hit-activiteiten-2015/hit-kiezer-2015";
	}

}
