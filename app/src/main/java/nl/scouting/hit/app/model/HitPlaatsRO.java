package nl.scouting.hit.app.model;

import java.util.List;

/**
 * Created by martijn on 22-10-14.
 */
public class HitPlaatsRO extends HitPlaats {
	private HitPlaats wrapped;

	public HitPlaatsRO(HitPlaats wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean equals(Object o) {
		return this.wrapped.equals(o);
	}

	@Override
	public String getLabel() {
		return "HIT " + getNaam() + " (" + getId() + ")";
	}

	public long getId() {
		return wrapped.getId();
	}

	public String getNaam() {
		return wrapped.getNaam();
	}

	public String getHitCourantTekst() {
		return wrapped.getHitCourantTekst();
	}

	public List<HitKamp> getKampen() {
		return wrapped.getKampen();
	}

	public HitProject getProject() {
		return wrapped.getProject();
	}
}
