package nl.scouting.hit.app.style;

/**
 * Colors used for branding.
 */
public enum HitColor {
	RED(0xFFed1c24),
	YELLOW(0xFFfddd04),
	BLUE(0xFF1c63b7),
	GREEN(0xFF00a650);

	/** ARGB value */
	private final int colorValue;

	HitColor(int colorValue) {
		this.colorValue = colorValue;
	}

	public int getColorValue() {
		return colorValue;
	}
}
