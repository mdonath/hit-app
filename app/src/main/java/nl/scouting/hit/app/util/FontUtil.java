package nl.scouting.hit.app.util;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import nl.scouting.hit.app.style.HitColor;

/**
 *
 */
public final class FontUtil {

	public static TextView setTypeface(View view, int header) {
		final TextView textView = (TextView) view.findViewById(header);
		textView.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/impact.ttf"));
		textView.setTextColor(HitColor.GREEN.getColorValue());
		return textView;
	}

	/** Private constructor. */
	private FontUtil() {
		super();
	}
}
