package nl.scouting.hit.app.nav;

import android.view.LayoutInflater;
import android.view.View;

import nl.scouting.hit.app.model.AbstractHitEntity;

public interface Item {
	HitEntityArrayAdapter.RowType getViewType();

	View getView(LayoutInflater inflater, View convertView);

	int getIndex();
}