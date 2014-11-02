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
}
