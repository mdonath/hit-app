package nl.scouting.hit.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.scouting.hit.app.components.ExpandableHeightListView;
import nl.scouting.hit.app.courant.KampOnItemClickListener;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.model.HitKampRO;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.style.PlaatsStyle;

/**
 * Shows the welcome information.
 */
public class Welcome extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflate = inflater.inflate(R.layout.fragment_welcome, container, false);
		final HitProject hitProject = ((HitProjectContainable) this.getActivity()).getHitProject();
		setKampen(inflater, hitProject, inflate);
		return inflate;
	}

	private void setKampen(LayoutInflater inflater, HitProject hitProject, View view) {

		final List<HitKamp> list = new ArrayList<HitKamp>();
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

			private String strip(String s) {
				return s.replace("\"", "");
			}
		});
		final ArrayAdapter<HitKamp> adapter = new HitKampArrayAdapter(
				getActivity().getApplicationContext()
				, list
		);
		final ExpandableHeightListView listview = (ExpandableHeightListView) view.findViewById(R.id.kampen);
		listview.setExpanded(true);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new KampOnItemClickListener(this));
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
