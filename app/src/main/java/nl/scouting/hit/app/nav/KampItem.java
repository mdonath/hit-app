package nl.scouting.hit.app.nav;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;
import nl.scouting.hit.app.style.PlaatsStyle;

public class KampItem implements Item {

	private HitKamp kamp;

	public KampItem(HitKamp kamp) {

		this.kamp = kamp;
	}

	@Override
	public HitEntityArrayAdapter.RowType getViewType() {
		return HitEntityArrayAdapter.RowType.KAMP_ITEM;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.navdraw_kamp, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		PlaatsStyle style = PlaatsStyle.by(kamp);

		TextView tv = (TextView) view.findViewById(R.id.naam);
		tv.setText(kamp.getNaam());

		return view;
	}
}