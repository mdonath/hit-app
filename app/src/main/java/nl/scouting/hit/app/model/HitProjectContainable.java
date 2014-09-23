package nl.scouting.hit.app.model;

/**
 * Interface to prevent coupling with Main Activity.
 */
public interface HitProjectContainable {

	/**
	 * @return the current HitProject
	 */
	HitProject getHitProject();
}
