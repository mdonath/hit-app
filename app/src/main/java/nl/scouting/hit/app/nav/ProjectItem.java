package nl.scouting.hit.app.nav;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitProject;

public class ProjectItem implements Item {

	private HitProject project;

	public ProjectItem(HitProject project) {

		this.project = project;
	}

	@Override
	public HitEntityArrayAdapter.RowType getViewType() {
		return HitEntityArrayAdapter.RowType.PLAATS_ITEM;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.navdraw_project, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		TextView text = (TextView) view.findViewById(R.id.naam);
		text.setText(project.getLabel());

		return view;
	}
}