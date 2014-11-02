package nl.scouting.hit.app.nav;

import android.view.LayoutInflater;
import android.view.View;

import nl.scouting.hit.app.R;

public class IcoontjesItem extends AbstractIndexedItem {

	public IcoontjesItem(int index) {
		super(index);
	}

	@Override
	public HitEntityArrayAdapter.RowType getViewType() {
		return HitEntityArrayAdapter.RowType.ICOONTJES_ITEM;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.navdraw_icoontjes, null);
		} else {
			view = convertView;
		}
		return view;
	}
}