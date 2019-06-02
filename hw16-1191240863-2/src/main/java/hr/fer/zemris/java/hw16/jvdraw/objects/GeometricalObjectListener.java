package hr.fer.zemris.java.hw16.jvdraw.objects;

/**
 * Listener for {@link GeometricalObject}.
 * 
 * @author Bruno Iljazović
 */
public interface GeometricalObjectListener {

	/**
	 * Geometrical object changed.
	 *
	 * @param o the object changed.
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}