package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a geometrical object.
 *
 * @author Bruno Iljazović
 */
public abstract class GeometricalObject {

	/** The foreground color. */
	protected Color fgColor;
	
	/** The background color. */
	protected Color bgColor;
	
	/**
	 * Returns the entry of this geoemtrical object for the .jvd files.
	 *
	 * @return the string
	 */
	public abstract String toJVDEntry();
	
	/**
	 * Accepts the visitor
	 *
	 * @param v the visitor
	 */
	public abstract void accept(GeometricalObjectVisitor v);

	/**
	 * Creates the geometrical object editor.
	 *
	 * @return the geometrical object editor
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
	/** The listeners. */
	protected List<GeometricalObjectListener> listeners = new ArrayList<>();

	/**
	 * Adds the geometrical object listener, if it already isn't present.
	 *
	 * @param l the listener
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		if (listeners.contains(l)) return;
		listeners.add(Objects.requireNonNull(l, "Listener was null"));
	}

	/**
	 * Removes the geometrical object listener.
	 *
	 * @param l the listener
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
}
