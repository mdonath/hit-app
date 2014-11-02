package nl.scouting.hit.app.courant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitIcon;
import nl.scouting.hit.app.model.HitProject;
import nl.scouting.hit.app.model.HitProjectContainable;
import nl.scouting.hit.app.util.FontUtil;

/**
 * Shows the welcome information.
 */
public class IcoontjesFragment extends Fragment {

	private HitProject getHitProject() {
		return getHitContainer().getHitProject();
	}

	private HitProjectContainable getHitContainer() {
		return ((HitProjectContainable) this.getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_icons, container, false);

		FontUtil.setTypeface(view, R.id.header);

		LinearLayout listview = (LinearLayout) view.findViewById(R.id.iconlist);

		List<HitIcon> list = new ArrayList(getHitProject().getGebruikteIconen());
		Collections.sort(list);

		for (HitIcon icon : list) {
			View iconView = inflater.inflate(R.layout.list_icons, null);

			ImageView iv = (ImageView) iconView.findViewById(R.id.icon);
			iv.setImageResource(icon.getResId());

			TextView tv = (TextView) iconView.findViewById(R.id.omschrijving);
			tv.setText(icon.getTekst());

			listview.addView(iconView);
		}
		return view;
	}
}
