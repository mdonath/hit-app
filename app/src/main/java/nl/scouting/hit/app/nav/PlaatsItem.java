package nl.scouting.hit.app.nav;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitPlaats;
import nl.scouting.hit.app.style.PlaatsStyle;

public class PlaatsItem extends AbstractIndexedItem  {

	private HitPlaats plaats;

	public PlaatsItem(HitPlaats plaats, int index) {
		super(index);
		this.plaats = plaats;
	}

	@Override
	public HitEntityArrayAdapter.RowType getViewType() {
		return HitEntityArrayAdapter.RowType.PLAATS_ITEM;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.navdraw_plaats, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		PlaatsStyle style = PlaatsStyle.by(plaats);

		// Update textview
		TextView tv = (TextView) view.findViewById(R.id.naam);
		tv.setText(plaats.getNaam());
		tv.setBackgroundColor(style.background.getColorValue());
		tv.setTextColor(style.text.getColorValue());

		// Update logoSmall
		ImageView iv = (ImageView) view.findViewById(R.id.logo);
		if (iv != null) {
			iv.setImageResource(style.logoSmall);
		}
		return view;
	}
}