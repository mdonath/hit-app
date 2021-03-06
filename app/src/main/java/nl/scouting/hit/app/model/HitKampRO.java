package nl.scouting.hit.app.model;

import android.view.Menu;

import java.util.Date;
import java.util.List;

/**
 * Created by martijn on 22-9-14.
 */
public class HitKampRO extends HitKamp {
	private final HitKamp wrapped;

	public HitKampRO(HitKamp wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean equals(Object o) {
		return this.wrapped.equals(o);
	}

	@Override
	public String getShareText() {
		return this.wrapped.getShareText();
	}

	@Override
	public HitPlaats getPlaats() {
		return wrapped.getPlaats();
	}

	@Override
	public String getNaam() {
		return wrapped.getNaam();
	}

	@Override
	public long getId() {
		return wrapped.getId();
	}

	@Override
	public long getShantiId() {
		return wrapped.getShantiId();
	}

	@Override
	public boolean isVol() {
		return wrapped.isVol();
	}

	@Override
	public String getVolTekst() {
		return wrapped.getVolTekst();
	}

    @Override
    public String getChecktijd() {
        return wrapped.getChecktijd();
    }

	@Override
	public Date getEindDatumTijd() {
		return wrapped.getEindDatumTijd();
	}

	@Override
	public Date getStartDatumTijd() {
		return wrapped.getStartDatumTijd();
	}

	@Override
	public HitEntityEnum getType() {
		return wrapped.getType();
	}

	@Override
	public int getDeelnamekosten() {
		return wrapped.getDeelnamekosten();
	}

	@Override
	public int getMaximumLeeftijd() {
		return wrapped.getMaximumLeeftijd();
	}

	@Override
	public int getMinimumLeeftijd() {
		return wrapped.getMinimumLeeftijd();
	}

	@Override
	public List<HitIcon> getIcoontjes() {
		return wrapped.getIcoontjes();
	}

	@Override
	public String getHitCourantTekst() {
		return wrapped.getHitCourantTekst();
	}

	@Override
	public String getLabel() {
		return wrapped.getLabel();
	}

	@Override
	public String getSubgroep() {
		return wrapped.getSubgroep();
	}

	@Override
	public String getHitnlUrl() {
		return wrapped.getHitnlUrl();
	}
}
