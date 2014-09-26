package nl.scouting.hit.app.util;

import android.view.View;
import android.widget.TextView;

/**
 * Created by martijn on 21-9-14.
 */
public final class TextUtil {

	public static String cleanUp(String s) {
		return s
				.replace("\r\n\r\n", "<p>")
				.replace("\r\n", " ") // remove newlines
				.replace("  ", " ") // remove double spaces
				.replace("<p>", "\n\n")
				;
	}

	public static TextView setText(View view, int id, String value) {
		TextView textView = (TextView) view.findViewById(id);
		textView.setText(value);
		return textView;
	}

	/** Private constructor. */
	private TextUtil() {
		super();
	}
}
