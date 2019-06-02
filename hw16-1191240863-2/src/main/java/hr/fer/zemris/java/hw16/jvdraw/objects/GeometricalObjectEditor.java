package hr.fer.zemris.java.hw16.jvdraw.objects;

import javax.swing.JPanel;

/**
 * Editor for {@link GeometricalObject} checkEditing can throw
 * NumberFormatException which means that illegal values were inputed.
 * 
 * @author Bruno Iljazović
 */
public abstract class GeometricalObjectEditor extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Checks edited values.
	 */
	public abstract void checkEditing();

	/**
	 * Accepts edited values and possibly changes the object.
	 */
	public abstract void acceptEditing();
}