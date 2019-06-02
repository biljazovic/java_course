package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.hw16.jvdraw.Util;

/**
 * Filled circle. it has variable line color, radius and center coordinates.
 * 
 * @author Bruno Iljazović
 */
public class FilledCircle extends GeometricalObject {

	/** The center. */
	private Point center;
	
	/** The radius. */
	private int radius;
	
	/**
	 * Instantiates a new filled circle.
	 *
	 * @param center the center
	 * @param radius the radius
	 * @param fgColor the fg color
	 * @param bgColor the bg color
	 */
	public FilledCircle(Point center, int radius, Color fgColor, Color bgColor) {
		this.center = center;
		this.radius = radius;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
	
	/**
	 * Gets the line color.
	 *
	 * @return the line color
	 */
	public Color getLineColor() {
		return fgColor;
	}
	
	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public Color getFillColor() {
		return bgColor;
	}

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor();
	}

	@Override
	public String toString() {
		return String.format("Filled circle (%d,%d), %d, #%06X", center.x, center.y, radius,
				bgColor.getRGB() & 0xffffff);
	}
	
	@Override
	public String toJVDEntry() {
		return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", center.x, center.y, radius,
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}

	/**
	 * Extends {@link GeometricalObjectEditor}. Shows the form for changing center
	 * coordinates, radius, line color and the fill color.
	 */
	private class FilledCircleEditor extends GeometricalObjectEditor {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The x of center. */
		JFormattedTextField xStart;
		
		/** The y of center. */
		JFormattedTextField yStart;
		
		/** The radius. */
		JFormattedTextField radius;
		
		/** The line color. */
		JColorChooser lineColor;
		
		/** The fill color. */
		JColorChooser fillColor;
		
		/**
		 * Instantiates a new filled circle editor.
		 */
		public FilledCircleEditor() {
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(0);

			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

			xStart = new JFormattedTextField(numberFormat);
			xStart.setFocusLostBehavior(JFormattedTextField.PERSIST);
			xStart.setValue(center.x);
			yStart = new JFormattedTextField(numberFormat);
			yStart.setFocusLostBehavior(JFormattedTextField.PERSIST);
			yStart.setValue(center.y);
			radius = new JFormattedTextField(numberFormat);
			radius.setFocusLostBehavior(JFormattedTextField.PERSIST);
			radius.setValue((FilledCircle.this).radius);

			JPanel coordEdit = new JPanel();
			coordEdit.setLayout(new BoxLayout(coordEdit, BoxLayout.LINE_AXIS));
			
			JPanel startEdit = new JPanel(new GridLayout(2, 2));
			startEdit.setBorder(BorderFactory.createTitledBorder("Edit center point coordinates"));
			startEdit.add(new JLabel("center.x:", SwingConstants.RIGHT));
			startEdit.add(xStart);
			startEdit.add(new JLabel("center.y:", SwingConstants.RIGHT));
			startEdit.add(yStart);

			JPanel radiusEdit = new JPanel(new GridLayout(1, 2));
			radiusEdit.setBorder(BorderFactory.createTitledBorder("Edit radius"));
			radiusEdit.add(new JLabel("radius:", SwingConstants.RIGHT));
			radiusEdit.add(radius);
			
			coordEdit.add(startEdit);
			coordEdit.add(radiusEdit);
			
			add(coordEdit);
			
			lineColor = new JColorChooser(fgColor);
			lineColor.setBorder(BorderFactory.createTitledBorder("Choose line color"));
			fillColor = new JColorChooser(bgColor);
			fillColor.setBorder(BorderFactory.createTitledBorder("Choose fill color"));

			add(lineColor);
			add(fillColor);
		}
		
		@Override
		public void checkEditing() {
			try {
				xStart.commitEdit();
				yStart.commitEdit();
				radius.commitEdit();
			} catch (ParseException e) {
				throw new NumberFormatException();
			}
		}

		@Override
		public void acceptEditing() {
			center.x = ((Long)xStart.getValue()).intValue();
			center.y = ((Long)yStart.getValue()).intValue();
			(FilledCircle.this).radius = ((Long)radius.getValue()).intValue();
			Color newColor1 = lineColor.getColor();
			if (newColor1 != null) fgColor = newColor1;
			Color newColor2 = fillColor.getColor();
			if (newColor2 != null) bgColor = newColor2;

			for (GeometricalObjectListener l : listeners) {
				l.geometricalObjectChanged(FilledCircle.this);
			}
		}
	}
	
	/**
	 * Returns new FilledCircle from the .jvd file entry.
	 *
	 * @param lineArr the split .jvd entry
	 * @return the filled circle
	 */
	public static FilledCircle fromJVDEntry(String[] lineArr) {
		int x0, y0, radius, r, g, b, r2, g2, b2;
		try {
			x0 = Util.parsePositiveInteger(lineArr[1]);
			y0 = Util.parsePositiveInteger(lineArr[2]);
			radius = Util.parsePositiveInteger(lineArr[3]);
			r = Util.parsePositiveInteger(lineArr[4]);
			g = Util.parsePositiveInteger(lineArr[5]);
			b = Util.parsePositiveInteger(lineArr[6]);
			r2 = Util.parsePositiveInteger(lineArr[7]);
			g2 = Util.parsePositiveInteger(lineArr[8]);
			b2 = Util.parsePositiveInteger(lineArr[9]);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Values should be integers!");
		}
		return new FilledCircle(new Point(x0, y0), radius, new Color(r, g, b), new Color(r2, g2, b2));
	}

}
