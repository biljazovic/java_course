package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Stores data required for drawing the bar chart.
 * 
 * @author Bruno Iljazović
 */
public class BarChart {
	
	/** The values. */
	private List<XYValue> values;
	
	/** The x label. */
	private String xLabel;
	
	/** The y label. */
	private String yLabel;
	
	/** Minimum y value */
	private int yMin;
	
	/** Maximum y value */
	private int yMax;
	
	/** Gap between two y values */
	private int yDelta;

	/**
	 * Instantiates a new bar chart.
	 *
	 * @param values the values
	 * @param xLabel the x label
	 * @param yLabel the y label
	 * @param yMin minimum y
	 * @param yMax maximum y
	 * @param yDelta gap between two y's
	 */
	public BarChart(List<XYValue> values, String xLabel, String yLabel, int yMin, int yMax,
			int yDelta) {
		super();
		this.values = values;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.yMin = yMin;
		this.yMax = yMax;
		this.yDelta = yDelta;
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Gets the x label.
	 *
	 * @return the x label
	 */
	public String getxLabel() {
		return xLabel;
	}

	/**
	 * Gets the y label.
	 *
	 * @return the y label
	 */
	public String getyLabel() {
		return yLabel;
	}

	/**
	 * Gets the minimum y
	 *
	 * @return the minimum y
	 */
	public int getyMin() {
		return yMin;
	}

	/**
	 * Gets the maximum y
	 *
	 * @return the maximum y
	 */
	public int getyMax() {
		return yMax;
	}

	/**
	 * Gets the gap between two y's
	 *
	 * @return the gap between two y's
	 */
	public int getyDelta() {
		return yDelta;
	}
}
