package nl.scouting.hit.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martijn on 17-9-14.
 */
public class HitPlaats extends AbstractHitEntity {

	private HitProject project;
	private String naam;
	private String hitCourantTekst;
	private List<HitKamp> kampen;

	public void add(HitKamp hitKamp) {
		if (this.kampen == null) {
			this.kampen = new ArrayList<HitKamp>();
		}
		hitKamp.setPlaats(this);
		this.kampen.add(hitKamp);
	}

	public void setKampen(List<HitKamp> kampen) {
		for (HitKamp kamp : kampen) {
			kamp.setPlaats(this);
		}
		this.kampen = kampen;
	}

	@Override
	public HitEntityEnum getType() {
		return HitEntityEnum.PLAATS;
	}

	@Override
	public String getLabel() {
		return "HIT " + naam + " (" + getId() + ")";
	}

	@Override
	public String getShareText() {
		return "Mijn favoriete HIT plaats is: HIT " + getNaam() + ". Kijk voor meer info op https://hit.scouting.nl/hits-in-" + getNaam().toLowerCase() + "-" + project.getJaar();
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getHitCourantTekst() {
		return hitCourantTekst;
	}

	public void setHitCourantTekst(String hitCourantTekst) {
		this.hitCourantTekst = hitCourantTekst;
	}

	public List<HitKamp> getKampen() {
		return kampen;
	}

	public HitProject getProject() {
		return project;
	}

	public void setProject(HitProject project) {
		this.project = project;
	}
}
