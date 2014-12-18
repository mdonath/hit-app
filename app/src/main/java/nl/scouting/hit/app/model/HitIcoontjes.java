package nl.scouting.hit.app.model;

/**
 * Created by martijn on 2-11-14.
 */
public class HitIcoontjes extends AbstractHitEntity {
	@Override
	public HitEntityEnum getType() {
		return HitEntityEnum.ICOONTJES;
	}

	@Override
	public String getLabel() {
		return "Icoontjes";
	}

	@Override
	public String getShareText() {
		// FIXME vervangen van jaartal door HitProject.getJaartal()
		return "Snap je niets van al die icoontjes? Kijk hier voor meer info: https://hit.scouting.nl/hit-activiteiten-2015/symbolen";
	}
}
