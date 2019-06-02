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
 * A line with variable end coordinates and line color.
 * 
 * @author Bruno Iljazović
 */
public class Line extends GeometricalObject {
	
	/** The start. */
	Point start;
	
	/** The end. */
	Point end;
	
	/**
	 * Instantiates a new line.
	 *
	 * @param start the start
	 * @param end the end
	 * @param color the color
	 */
	public Line(Point start, Point end, Color color) {
		this.start = start;
		this.end = end;
		this.fgColor = color;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public Point getEnd() {
		return end;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return fgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor();
	}
	
	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", start.x, start.y, end.x, end.y);
	}
	
	/**
	 * Extends {@link GeometricalObjectEditor}. Shows the form for changing end
	 * coordinates coordinates and line color.
	 */
	private class LineEditor extends GeometricalObjectEditor {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The x start. */
		JFormattedTextField xStart;
		
		/** The y start. */
		JFormattedTextField yStart;
		
		/** The x end. */
		JFormattedTextField xEnd;
		
		/** The y end. */
		JFormattedTextField yEnd;
		
		/** The line color. */
		JColorChooser lineColor;
		
		/**
		 * Instantiates a new line editor.
		 */
		public LineEditor() {
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(0);

			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

			xStart = new JFormattedTextField(numberFormat);
			xStart.setFocusLostBehavior(JFormattedTextField.PERSIST);
			xStart.setValue(start.x);
			yStart = new JFormattedTextField(numberFormat);
			yStart.setFocusLostBehavior(JFormattedTextField.PERSIST);
			yStart.setValue(start.y);

			xEnd = new JFormattedTextField(numberFormat);
			xEnd.setFocusLostBehavior(JFormattedTextField.PERSIST);
			xEnd.setValue(end.x);
			yEnd = new JFormattedTextField(numberFormat);
			yEnd.setFocusLostBehavior(JFormattedTextField.PERSIST);
			yEnd.setValue(end.y);

			JPanel coordEdit = new JPanel();
			coordEdit.setLayout(new BoxLayout(coordEdit, BoxLayout.LINE_AXIS));
			
			JPanel startEdit = new JPanel(new GridLayout(2, 2));
			startEdit.setBorder(BorderFactory.createTitledBorder("Edit start point coordinates"));
			startEdit.add(new JLabel("start.x:", SwingConstants.RIGHT));
			startEdit.add(xStart);
			startEdit.add(new JLabel("start.y:", SwingConstants.RIGHT));
			startEdit.add(yStart);

			JPanel endEdit = new JPanel(new GridLayout(2, 2));
			endEdit.setBorder(BorderFactory.createTitledBorder("Edit end point coordinates"));
			endEdit.add(new JLabel("end.x:", SwingConstants.RIGHT));
			endEdit.add(xEnd);
			endEdit.add(new JLabel("end.y:", SwingConstants.RIGHT));
			endEdit.add(yEnd);
			
			coordEdit.add(startEdit);
			coordEdit.add(endEdit);
			
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
				xEnd.commitEdit();
				yEnd.commitEdit();
			} catch (ParseException e) {
				throw new NumberFormatException();
			}
		}

		@Override
		public void acceptEditing() {
			start.x = ((Long)xStart.getValue()).intValue();
			start.y = ((Long)yStart.getValue()).intValue();
			end.x = ((Long)xEnd.getValue()).intValue();
			end.y = ((Long)yEnd.getValue()).intValue();
			Color newColor = lineColor.getColor();
			if (newColor != null) fgColor = newColor;
			for (GeometricalObjectListener l : listeners) {
				l.geometricalObjectChanged(Line.this);
			}
		}
	}
	
	/**
	 * Returns new Line from .jvd entry.
	 *
	 * @param lineArr the split .jvd entry.
	 * @return the line
	 */
	public static Line fromJVDEntry(String[] lineArr) {
		int x0, y0, x1, y1, r, g, b;
		try {
			x0 = Util.parsePositiveInteger(lineArr[1]);
			y0 = Util.parsePositiveInteger(lineArr[2]);
			x1 = Util.parsePositiveInteger(lineArr[3]);
			y1 = Util.parsePositiveInteger(lineArr[4]);
			r = Util.parsePositiveInteger(lineArr[5]);
			g = Util.parsePositiveInteger(lineArr[6]);
			b = Util.parsePositiveInteger(lineArr[7]);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException();
		}
		return new Line(new Point(x0, y0), new Point(x1, y1), new Color(r, g, b));
	}

	@Override
	public String toJVDEntry() {
		return String.format("LINE %d %d %d %d %d %d %d", start.x, start.y, end.x, end.y,
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
	}

}
