package nl.scouting.hit.app.nav;

/**
 * Created by martijn on 2-11-14.
 */
public abstract class AbstractIndexedItem implements Item {
	private int index;

	public AbstractIndexedItem(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
