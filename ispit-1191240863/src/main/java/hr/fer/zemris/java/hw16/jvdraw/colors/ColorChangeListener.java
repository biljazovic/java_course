package hr.fer.zemris.java.hw16.jvdraw.colors;

import java.awt.Color;

/**
 * Listener for change in color in {@link IColorProvider}.
 * 
 * @author Bruno Iljazović
 */
public interface ColorChangeListener {

	/**
	 * New color selected.
	 *
	 * @param source the source color provider
	 * @param oldColor the old color
	 * @param newColor the new color
	 */
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
