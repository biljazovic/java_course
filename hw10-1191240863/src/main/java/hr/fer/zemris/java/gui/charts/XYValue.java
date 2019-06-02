package hr.fer.zemris.java.gui.charts;

/**
 * Represents one data element in BarChart.
 * 
 * @author Bruno Iljazović
 */
public class XYValue {
	
	/** The x value. */
	int x;

	/** The y value. */
	int y;

	/**
	 * Instantiates a new XY value.
	 *
	 * @param x the x value
	 * @param y the y value
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x value
	 *
	 * @return the x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y value
	 *
	 * @return the y value
	 */
	public int getY() {
		return y;
	}
}
