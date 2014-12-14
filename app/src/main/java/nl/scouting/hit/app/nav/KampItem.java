package nl.scouting.hit.app.nav;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitKamp;

public class KampItem extends AbstractIndexedItem {

	private HitKamp kamp;

	public KampItem(HitKamp kamp, int index) {
		super(index);
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

		TextView tv = (TextView) view.findViewById(R.id.naam);
		tv.setText(kamp.getNaam());

		ImageView volIndicator = (ImageView) view.findViewById(R.id.vol_indicator);
		volIndicator.setVisibility(kamp.isVol() ? View.VISIBLE : View.INVISIBLE);

		return view;
	}
}