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
}
