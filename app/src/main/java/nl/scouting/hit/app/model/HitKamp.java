package nl.scouting.hit.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by martijn on 17-9-14.
 */
public class HitKamp extends AbstractHitEntity {

	private HitPlaats plaats;
	private String naam;
	private Date startDatumTijd;
	private Date eindDatumTijd;
	private int minimumLeeftijd;
	private int maximumLeeftijd;
	private String subgroep;
	private int deelnamekosten;
	private String hitCourantTekst;
	private List<HitIcon> icoontjes;
	private int kampIndex;

	@Override
	public HitEntityEnum getType() {
		return HitEntityEnum.KAMP;
	}

	public final String formatLeeftijd() {
		return new StringBuilder()
				.append(getMinimumLeeftijd())
				.append(" - ")
				.append(getMaximumLeeftijd())
				.toString();
	}

	public final String formatPeriode() {
		String vanFormat;
		if (isSameMonth(this.startDatumTijd, this.eindDatumTijd)) {
			vanFormat = "dd";
		} else {
			vanFormat = "dd MMMM";
		}
		return new StringBuilder(new SimpleDateFormat(vanFormat).format(this.startDatumTijd))
				.append(new SimpleDateFormat(" - dd MMMM").format(this.eindDatumTijd))
				.toString();
	}

	private boolean isSameMonth(final Date startDatumTijd, final Date eindDatumTijd) {
		return startDatumTijd.getMonth() == eindDatumTijd.getMonth();
	}

	@Override
	public String getLabel() {
		return getNaam() + " (" + getId() + ")";
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public Date getStartDatumTijd() {
		return startDatumTijd;
	}

	public void setStartDatumTijd(Date startDatumTijd) {
		this.startDatumTijd = startDatumTijd;
	}

	public Date getEindDatumTijd() {
		return eindDatumTijd;
	}

	public void setEindDatumTijd(Date eindDatumTijd) {
		this.eindDatumTijd = eindDatumTijd;
	}

	public int getMinimumLeeftijd() {
		return minimumLeeftijd;
	}

	public void setMinimumLeeftijd(int minimumLeeftijd) {
		this.minimumLeeftijd = minimumLeeftijd;
	}

	public int getMaximumLeeftijd() {
		return maximumLeeftijd;
	}

	public void setMaximumLeeftijd(int maximumLeeftijd) {
		this.maximumLeeftijd = maximumLeeftijd;
	}

	public String getSubgroep() {
		return subgroep;
	}

	public void setSubgroep(String subgroep) {
		this.subgroep = subgroep;
	}

	public int getDeelnamekosten() {
		return deelnamekosten;
	}

	public void setDeelnamekosten(int deelnamekosten) {
		this.deelnamekosten = deelnamekosten;
	}

	public String getHitCourantTekst() {
		return hitCourantTekst;
	}

	public void setHitCourantTekst(String hitCourantTekst) {
		this.hitCourantTekst = hitCourantTekst;
	}

	public List<HitIcon> getIcoontjes() {
		return icoontjes;
	}

	public void setIcoontjes(List<HitIcon> icoontjes) {
		this.icoontjes = icoontjes;
	}

	public HitPlaats getPlaats() {
		return plaats;
	}

	public void setPlaats(HitPlaats plaats) {
		this.plaats = plaats;
	}

	public int getKampIndex() {
		return kampIndex;
	}

	public void setKampIndex(final int kampIndex) {
		this.kampIndex = kampIndex;
	}
}
