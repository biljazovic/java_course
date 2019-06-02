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
 * A circle with variable radius and line color. It is empty inside.
 * 
 * @author Bruno Iljazović
 */
public class Circle extends GeometricalObject {
	
	/** The center. */
	private Point center;
	
	/** The radius. */
	private int radius;
	
	/**
	 * Instantiates a new circle.
	 *
	 * @param center the center
	 * @param radius the radius
	 * @param color the color of the line
	 */
	public Circle(Point center, int radius, Color color) {
		this.center = center;
		this.radius = radius;
		fgColor = color;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return fgColor;
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
		return new CircleEditor();
	}

	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", center.x, center.y, radius);
	}
	
	@Override
	public String toJVDEntry() {
		return String.format("CIRCLE %d %d %d %d %d %d", center.x, center.y, radius,
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
	}

	/**
	 * Extends {@link GeometricalObjectEditor}. Shows the form for changing center
	 * coordinates, radius and line color.
	 */
	private class CircleEditor extends GeometricalObjectEditor {
		
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
		
		/**
		 * Instantiates a new circle editor.
		 */
		public CircleEditor() {
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
			radius.setValue((Circle.this).radius);

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
			add(lineColor);
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
			(Circle.this).radius = ((Long)radius.getValue()).intValue();
			Color newColor = lineColor.getColor();
			if (newColor != null) fgColor = newColor;
			for (GeometricalObjectListener l : listeners) {
				l.geometricalObjectChanged(Circle.this);
			}
		}
	}
	
	/**
	 * Returns a new Circle from the entry from a .jvd file.
	 *
	 * @param lineArr the split entry
	 * @return the circle from the entry
	 */
	public static Circle fromJVDEntry(String[] lineArr) {
		int x0, y0, radius, r, g, b;
		try {
			x0 = Util.parsePositiveInteger(lineArr[1]);
			y0 = Util.parsePositiveInteger(lineArr[2]);
			radius = Util.parsePositiveInteger(lineArr[3]);
			r = Util.parsePositiveInteger(lineArr[4]);
			g = Util.parsePositiveInteger(lineArr[5]);
			b = Util.parsePositiveInteger(lineArr[6]);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Values should be integers!");
		}
		return new Circle(new Point(x0, y0), radius, new Color(r, g, b));
	}
}
