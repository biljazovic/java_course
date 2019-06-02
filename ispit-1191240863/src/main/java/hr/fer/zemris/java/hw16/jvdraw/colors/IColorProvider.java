package hr.fer.zemris.java.hw16.jvdraw.colors;

import java.awt.Color;

/**
 * Provider of a color.
 * 
 * @author Bruno Iljazović
 */
public interface IColorProvider {

	/**
	 * Gets the current color.
	 *
	 * @return the current color
	 */
	Color getCurrentColor();
	
	/**
	 * Adds the color change listener.
	 *
	 * @param l the listener
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Removes the color change listener.
	 *
	 * @param l the listener
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
