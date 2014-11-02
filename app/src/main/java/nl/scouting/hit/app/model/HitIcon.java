package nl.scouting.hit.app.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by martijn on 17-9-14.
 */
public class HitIcon implements Comparable<HitIcon> {
	private static final Map<String, HitIcon> CACHE = new TreeMap<String, HitIcon>();

	private final String naam;
	private final String tekst;
	private final int resId;
	private final int order;

	public static final HitIcon create(String naam, String tekst, final int resId) {
		if (CACHE.containsKey(naam)) {
			return CACHE.get(naam);
		}
		return new HitIcon(naam, tekst, resId);
	}

	public static final HitIcon getByNaam(String naam) {
		if (CACHE.containsKey(naam)) {
			return CACHE.get(naam);
		}
		return null;
	}

	public HitIcon(String naam, String tekst, final int resId) {
		this.naam = naam;
		this.tekst = tekst;
		this.resId = resId;
		CACHE.put(naam, this);
		this.order = CACHE.size();
	}

	public String getNaam() {
		return naam;
	}

	public String getTekst() {
		return tekst;
	}

	public int getResId() {
		return resId;
	}

	public int getOrder() {
		return order;
	}

	@Override
	public int compareTo(final HitIcon another) {
		return order - another.order;
	}
}
