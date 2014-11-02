package nl.scouting.hit.app.model;

/**
 * Created by martijn on 17-9-14.
 */
public abstract class AbstractHitEntity {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract HitEntityEnum getType();

    public abstract String getLabel();

    public enum HitEntityEnum {
        PROJECT, PLAATS, KAMP, KIEZER, ICOONTJES;
    }
}
