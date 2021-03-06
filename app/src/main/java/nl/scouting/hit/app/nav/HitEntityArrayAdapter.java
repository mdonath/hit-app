package nl.scouting.hit.app.nav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import nl.scouting.hit.app.model.AbstractHitEntity;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitPlaats;
import nl.scouting.hit.app.model.HitProject;

public class HitEntityArrayAdapter extends ArrayAdapter<Item> {
	private LayoutInflater mInflater;

	public enum RowType {
		KAMP_ITEM, PLAATS_ITEM, PROJECT_ITEM, KIEZER_ITEM, ICOONTJES_ITEM;
	}

	public HitEntityArrayAdapter(Context context, List<AbstractHitEntity> items) {
		super(context, 0, convert(items));
		mInflater = LayoutInflater.from(context);
	}

	private static List<Item> convert(List<AbstractHitEntity> items) {
		int index = 0;
		List<Item> result = new ArrayList<Item>();
		for (AbstractHitEntity e : items) {
			switch (e.getType()) {
				case PROJECT:
					result.add(new ProjectItem((HitProject) e, index));
					break;
				case KIEZER:
					result.add(new KiezerItem(index));
					break;
				case ICOONTJES:
					result.add(new IcoontjesItem(index));
					break;
				case PLAATS:
					result.add(new PlaatsItem((HitPlaats) e, index));
					break;
				case KAMP:
					result.add(new KampItem((HitKamp) e, index));
					break;
			}
			index++;
		}
		return result;
	}

	@Override
	public int getViewTypeCount() {
		return RowType.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getViewType().ordinal();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position).getView(mInflater, convertView);
	}
}