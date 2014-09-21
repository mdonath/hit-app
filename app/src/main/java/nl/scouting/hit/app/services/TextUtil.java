package nl.scouting.hit.app.services;

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

	/** Private constructor. */
	private TextUtil() {
		super();
	}
}
