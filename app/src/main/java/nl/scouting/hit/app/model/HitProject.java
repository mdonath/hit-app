package nl.scouting.hit.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by martijn on 17-9-14.
 */
public class HitProject extends AbstractHitEntity {

	private int jaar;
	private Date vrijdag;
	private Date maandag;
	private Date inschrijvingStartdatum;
	private Date inschrijvingEinddatum;
	private List<HitIcon> gebruikteIconen;
	private List<HitPlaats> plaatsen;
	private List<AbstractHitEntity> orderedList;

	public void add(HitIcon hitIcon) {
		if (gebruikteIconen == null) {
			gebruikteIconen = new ArrayList();
		}
		gebruikteIconen.add(hitIcon);
	}

	public void add(HitPlaats hitPlaats) {
		if (plaatsen == null) {
			plaatsen = new ArrayList();
		}
		hitPlaats.setProject(this); // bidi
		plaatsen.add(hitPlaats);
	}

	public HitPlaats getHitPlaatsById(long id) {
		if (plaatsen != null) {
			for (HitPlaats plaats : plaatsen) {
				if (plaats.getId() == id) {
					return plaats;
				}
			}
		}
		return null;
	}

	public HitKamp getHitKampById(long id) {
		if (plaatsen != null) {
			for (HitPlaats plaats : plaatsen) {
				for (HitKamp kamp : plaats.getKampen()) {
					if (kamp.getId() == id) {
						return kamp;
					}
				}
			}
		}
		return null;
	}

	public AbstractHitEntity getByIndex(int position) {
		return getOrderedList().get(position);
	}

	public List<AbstractHitEntity> getOrderedList() {
		if (orderedList == null) {
			this.orderedList = createOrderedList();
		}
		return this.orderedList;
	}

	public List<HitKamp> getKampen() {
		final List<HitKamp> result = new ArrayList<HitKamp>();
		if (plaatsen != null) {
			for (HitPlaats plaats : plaatsen) {
				for (HitKamp kamp : plaats.getKampen()) {
					result.add(kamp);
				}
			}
		}
		return result;
	}

	private List<AbstractHitEntity> createOrderedList() {
		int kampIndex = 1;
		List<AbstractHitEntity> result = new ArrayList<AbstractHitEntity>();
		result.add(this);
		if (plaatsen != null) {
			for (HitPlaats plaats : plaatsen) {
				result.add(plaats);
				for (HitKamp kamp : plaats.getKampen()) {
					kamp.setKampIndex(kampIndex++);
					result.add(kamp);
				}
			}
		}
		return result;
	}

	public void setPlaatsen(List<HitPlaats> plaatsen) {
		for (HitPlaats plaats : plaatsen) {
			plaats.setProject(this); // bidi
		}
		this.plaatsen = plaatsen;
	}

	@Override
	public HitEntityEnum getType() {
		return HitEntityEnum.PROJECT;
	}

	@Override
	public String getLabel() {
		return "HIT " + jaar;
	}

	public int getJaar() {
		return jaar;
	}

	public void setJaar(int jaar) {
		this.jaar = jaar;
	}

	public Date getVrijdag() {
		return vrijdag;
	}

	public void setVrijdag(Date vrijdag) {
		this.vrijdag = vrijdag;
	}

	public Date getMaandag() {
		return maandag;
	}

	public void setMaandag(Date maandag) {
		this.maandag = maandag;
	}

	public Date getInschrijvingStartdatum() {
		return inschrijvingStartdatum;
	}

	public void setInschrijvingStartdatum(Date inschrijvingStartdatum) {
		this.inschrijvingStartdatum = inschrijvingStartdatum;
	}

	public Date getInschrijvingEinddatum() {
		return inschrijvingEinddatum;
	}

	public void setInschrijvingEinddatum(Date inschrijvingEinddatum) {
		this.inschrijvingEinddatum = inschrijvingEinddatum;
	}

	public List<HitIcon> getGebruikteIconen() {
		return gebruikteIconen;
	}

	public void setGebruikteIconen(List<HitIcon> gebruikteIconen) {
		this.gebruikteIconen = gebruikteIconen;
	}

	public List<HitPlaats> getPlaatsen() {
		return plaatsen;
	}
}
