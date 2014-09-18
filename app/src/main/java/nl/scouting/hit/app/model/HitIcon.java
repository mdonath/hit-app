package nl.scouting.hit.app.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by martijn on 17-9-14.
 */
public class HitIcon {
    private static final Map<String, HitIcon> CACHE = new TreeMap<String, HitIcon>();

    private String naam;
    private String tekst;

    public static final HitIcon create(String naam, String tekst) {
        if (CACHE.containsKey(naam)) {
            return CACHE.get(naam);
        }
        return new HitIcon(naam, tekst);
    }

    public static final HitIcon getByNaam(String naam) {
        if (CACHE.containsKey(naam)) {
            return CACHE.get(naam);
        }
        return null;
    }

    public HitIcon(String naam, String tekst) {
        this.naam = naam;
        this.tekst = tekst;
        CACHE.put(naam, this);
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
}
