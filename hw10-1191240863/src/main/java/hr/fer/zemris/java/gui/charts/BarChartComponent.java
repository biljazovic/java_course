package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * This component draws the entire bar chart whose data is given through the
 * constructor.
 * 
 * @author Bruno Iljazović
 */
public class BarChartComponent extends JComponent {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bar chart data. */
	BarChart barChart;

	/**
	 * Instantiates a new bar chart component.
	 *
	 * @param barChart the bar chart data
	 */
	public BarChartComponent(BarChart barChart) {
		this.barChart = barChart;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

		computeXY(g2);
		drawLines(g2);
		drawCoords(g2);
		drawLabels(g2);
		drawRectangles(g2);
	}
	
	/**  Fill Color of the rectangles. */
	private final Color PURPLE = new Color(150, 0, 205);
	
	/** The fixed gap used in painting. */
	private final int GAP = 20;
	
	/** The font size. */
	private final int FONT_SIZE = 14;
	
	/** y coordinate of the top line. */
	private int yStart;
	
	/** y coordinate of the bottom line. */
	private int yEnd;
	
	/** distance between two horizontal lines. */
	private int yGap;

	/** x coordinate of the left vertical line. */
	private int xStart;
	
	/** x coordinate of the right vertical line . */
	private int xEnd;
	
	/** distance between two vertical lines. */
	private int xGap;
	
	/** Maximum y value of bar chart. */
	private int yMax;
	
	/** The font height. */
	private int fontHeight;
	
	/** List of y coordinate labels. */
	List<String> yCoords;
	
	/**
	 * Computes needed variables.
	 *
	 * @param g2 the graphics object
	 */
	private void computeXY(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics();
		int maxWidth = 0;
		fontHeight = fm.getAscent();
		yCoords = new ArrayList<>();

		int yDelta = barChart.getyDelta();
		int yMin = barChart.getyMin();
		yMax = barChart.getyMax();
		yMax = ((yMax - 1 - yMin) / yDelta + 1) * yDelta + yMin;
		for (int i = yMin; i <= yMax; i += yDelta) {
			String s = Integer.toString(i);
			yCoords.add(s);
			maxWidth = Math.max(maxWidth, fm.stringWidth(s));
		}
		
		Insets ins = getInsets();

		xStart = ins.left + GAP + fontHeight + GAP + maxWidth + GAP;
		xEnd = getSize().width - ins.right - GAP;
		xGap = (xEnd - xStart) / barChart.getValues().size();
		xEnd = (xEnd - xStart) / xGap * xGap + xStart;
		
		yStart = ins.top + 2 * GAP;
		yEnd = getSize().height - (ins.bottom + fontHeight + GAP + fontHeight + GAP);
		yGap = (yEnd - yStart) / (yCoords.size() - 1);
		yStart = yEnd - (yEnd - yStart) / yGap * yGap;
		
	}
	
	/**
	 * Draws vertical and horizontal lines.
	 *
	 * @param g2 the graphics object
	 */
	private void drawLines(Graphics2D g2) {
		//draw yOs
		g2.setColor(Color.GRAY);
		for (int x = xStart; x <= xEnd; x += xGap) {
			if (x == xStart) {
				g2.setStroke(new BasicStroke(2));
			}
			g2.drawLine(x, yStart - GAP / 2, x, yEnd + GAP / 2);
			if (x == xStart) {
				g2.setStroke(new BasicStroke(1));
			}

		}
		//draw xOs
		for (int y = yStart; y <= yEnd; y += yGap) {
			if (y == yEnd) {
				g2.setStroke(new BasicStroke(2));
			}
			g2.drawLine(xStart - GAP / 2, y, xEnd + GAP / 2, y);
			if (y == yEnd) {
				g2.setStroke(new BasicStroke(1));
			}
		}
		
		//draw arrowheads
		g2.fillPolygon(
				new int[] {xStart, xStart - GAP / 4, xStart + GAP / 4}, 
				new int[] {yStart - GAP, yStart - GAP / 2, yStart - GAP / 2},
				3
		);
		g2.fillPolygon(
				new int[] {xEnd + GAP, xEnd + GAP / 2, xEnd + GAP / 2}, 
				new int[] {yEnd, yEnd - GAP / 4, yEnd + GAP / 4},
				3
		);
		
	}
	
	/**
	 * Draws coordinate numbers
	 *
	 * @param g2 the graphics object
	 */
	private void drawCoords(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics();

		//draw yCoords
		g2.setColor(Color.BLACK);
		for (int i = 0; i < yCoords.size(); i++) {
			String s = yCoords.get(i);
			g2.drawString(s, xStart - GAP - fm.stringWidth(s), yEnd - i * yGap + fontHeight / 2);
		}
		//draw xCoords
		for (int i = 0; i < barChart.getValues().size(); i++) {
			String s = Integer.toString(barChart.getValues().get(i).getX());
			g2.drawString(s, xStart + i * xGap + xGap / 2 - fm.stringWidth(s) / 2, yEnd + GAP / 2 + fontHeight);
		}
	}
		
	/**
	 * Draws x and y labels
	 *
	 * @param g2 the graphics object
	 */
	private void drawLabels(Graphics2D g2) {
		//draw labels
		FontMetrics fm = g2.getFontMetrics();
		String xLabel = barChart.getxLabel();
		String yLabel = barChart.getyLabel();

		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
		g2.drawString(xLabel, (xStart + xEnd) / 2 - fm.stringWidth(xLabel) / 2, getSize().height - getInsets().bottom - GAP);
		
		AffineTransform defaultAt = g2.getTransform();
		g2.setTransform(AffineTransform.getQuadrantRotateInstance(3));
		g2.drawString(yLabel, -(yStart + yEnd) / 2 - fm.stringWidth(yLabel) / 2, GAP + fontHeight);
		g2.setTransform(defaultAt);
	}
	
	/**
	 * Draws rectangles (main components of the chart)
	 *
	 * @param g2 the graphics object.
	 */
	private void drawRectangles(Graphics2D g2) {
		//draw rectangles
		g2.setColor(PURPLE);
		int yMin = barChart.getyMin();
		for (int i = 0; i < barChart.getValues().size(); i++) {
			int height = barChart.getValues().get(i).getY();
			if (height < yMin) continue;
			height = (int)((double)(height - yMin) / (yMax - yMin) * (yEnd - yStart));
			if (height > yEnd - yStart) height = yEnd - yStart + GAP;
			g2.fillRect(xStart + i * xGap + 1, yEnd - height, xGap - 2, height);
		}
	}
	
}
