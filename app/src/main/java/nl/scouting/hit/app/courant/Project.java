package nl.scouting.hit.app.courant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.components.ExpandableHeightListView;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitKampRO;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.style.PlaatsStyle;

/**
 * Shows the welcome information.
 */
public class Project extends Fragment implements AdapterView.OnItemClickListener {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflate = inflater.inflate(R.layout.fragment_welcome, container, false);
		setKampen(inflater, inflate);
		return inflate;
	}

	private HitProject getHitProject() {
		return getHitContainer().getHitProject();
	}

	private HitProjectContainable getHitContainer() {
		return ((HitProjectContainable) this.getActivity());
	}

	private void setKampen(final LayoutInflater inflater, final View view) {
		final ExpandableHeightListView listview = (ExpandableHeightListView) view.findViewById(R.id.kampen);
		listview.setExpanded(true);
		listview.setAdapter(new HitKampArrayAdapter(
				getActivity().getApplicationContext()
				, getHitKampsOnly()
		));
		listview.setOnItemClickListener(this);
	}

	/**
	 * Returns only items of type HitKamp, sorted by name.
	 *
	 * @return
	 */
	private List<HitKamp> getHitKampsOnly() {
		final List<HitKamp> list = new ArrayList<HitKamp>();
		final HitProject hitProject = getHitProject();
		for (HitKamp kamp : hitProject.getKampen()) {
			list.add(new HitKampRO(kamp) {
				@Override
				public String toString() {
					return getNaam();
				}
			});
		}
		Collections.sort(list, new Comparator<HitKamp>() {
			@Override
			public int compare(final HitKamp lhs, final HitKamp rhs) {
				return strip(lhs.getNaam()).compareTo(strip(rhs.getNaam()));
			}

			// ignore quotes while sorting
			private String strip(String s) {
				return s.replace("\"", "");
			}
		});
		return list;
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		HitKamp kamp = (HitKamp) parent.getItemAtPosition(position);
		getHitContainer().show(kamp);
	}

	public static class HitKampArrayAdapter extends ArrayAdapter<HitKamp> {
		private LayoutInflater mInflater;

		public HitKampArrayAdapter(Context context, List<HitKamp> items) {
			super(context, 0, items);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HitKamp kamp = getItem(position);
			View view;
			if (convertView == null) {
				view = (View) mInflater.inflate(R.layout.list_plaats_kampen_met_plaats, null);
				// Do some initialization
			} else {
				view = convertView;
			}

			PlaatsStyle style = PlaatsStyle.by(kamp);

			TextView tv = (TextView) view.findViewById(R.id.naam);
			tv.setText(kamp.getNaam());

			ImageView iv = (ImageView) view.findViewById(R.id.plaats);
			iv.setImageResource(style.logoSmall);

			return view;
		}
	}
}
