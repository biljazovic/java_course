package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.hw16.jvdraw.Util;

public class Polygon extends GeometricalObject {
	
	List<Point> points;
	
	public int[] getXPoints() {
		int[] ret = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			ret[i] = points.get(i).x;
		}
		return ret;
	}
	
	public int[] getYPoints() {
		int[] ret = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			ret[i] = points.get(i).y;
		}
		return ret;
	}

	public List<Point> getPoints() {
		return points;
	}
	
	public Color getLineColor() {
		return fgColor;
	}
	
	public Color getFillColor() {
		return bgColor;
	}

	public Polygon(List<Point> points, Color fgColor, Color bgColor) {
		this.points = new ArrayList<>(points);
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}

	@Override
	public String toJVDEntry() {
		StringBuilder sb = new StringBuilder();
		sb.append("FPOLY ");
		for (int i = 0; i < points.size(); i++) {
			sb.append(points.get(i).x + " " + points.get(i).y + " ");
		}
		sb.append(String.format("%d %d %d %d %d %d", fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
		return sb.toString();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new PolygonEditor();
	}
	
	private class PolygonEditor extends GeometricalObjectEditor {
		
		JColorChooser lineColor;
		
		JColorChooser fillColor;
		
		List<JFormattedTextField> xfields;
		List<JFormattedTextField> yfields;

		public PolygonEditor() {
			
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(0);

			int n = points.size();
			xfields = new ArrayList<>();
			yfields = new ArrayList<>();
			JPanel panel = new JPanel(new GridLayout(n, 4));

			
			for (int i = 0; i < n; i++) {
				Point point = points.get(i);
				JFormattedTextField xField = new JFormattedTextField(numberFormat);
				xField.setFocusLostBehavior(JFormattedTextField.PERSIST);
				xField.setValue(point.x);
				JFormattedTextField yField = new JFormattedTextField(numberFormat);
				yField.setFocusLostBehavior(JFormattedTextField.PERSIST);
				yField.setValue(point.y);
				xfields.add(xField);
				yfields.add(yField);
				panel.add(new JLabel("x:", SwingConstants.RIGHT));
				panel.add(xField);
				panel.add(new JLabel("y:", SwingConstants.RIGHT));
				panel.add(yField);
			}
			
			lineColor = new JColorChooser(fgColor);
			lineColor.setBorder(BorderFactory.createTitledBorder("Choose line color"));
			fillColor = new JColorChooser(bgColor);
			fillColor.setBorder(BorderFactory.createTitledBorder("Choose fill color"));

			add(panel);
			add(lineColor);
			add(fillColor);
		}

		@Override
		public void checkEditing() {
			try {
				for (JFormattedTextField field : xfields) {
					field.commitEdit();
				}
				for (JFormattedTextField field : yfields) {
					field.commitEdit();
				}
			} catch (ParseException e) {
				throw new NumberFormatException();
			}
		}

		@Override
		public void acceptEditing() {
			List<Point> nPoints = new ArrayList<>();
			for (int i = 0; i < points.size(); i++) {
				int x = ((Long)(xfields.get(i).getValue())).intValue();
				int y = ((Long)(yfields.get(i).getValue())).intValue();
				nPoints.add(new Point(x, y));
			}
			if (!Util.isConvex(nPoints)) {
				throw new NumberFormatException();
			}
			for (int i = 0; i < nPoints.size(); i++) {
				Point p1 = nPoints.get(i);
				Point p2 = nPoints.get((i+1)%nPoints.size());
				if (Util.distanceSq(p1, p2) <= 9) throw new NumberFormatException();
			}
			Color newColor1 = lineColor.getColor();
			if (newColor1 != null) fgColor = newColor1;
			Color newColor2 = fillColor.getColor();
			if (newColor2 != null) bgColor = newColor2;
			points = nPoints;
			for (GeometricalObjectListener l : listeners) {
				l.geometricalObjectChanged(Polygon.this);
			}
		}
		
	}
	
	@Override
	public String toString() {
		return "Polygon";
	}

	public static Polygon fromJVDEntry(String[] lineArr) {
		int r, g, b, r2, g2, b2;
		int n = (lineArr.length - 7) / 2;
		List<Point> points = new ArrayList<>();
		try {
			for (int i = 0; i < n; i++) {
				points.add(new Point(Util.parsePositiveInteger(lineArr[i*2+1]),
						Util.parsePositiveInteger(lineArr[i*2+2])));
			}
			int i = n * 2 + 1;
			r = Util.parsePositiveInteger(lineArr[i++]);
			g = Util.parsePositiveInteger(lineArr[i++]);
			b = Util.parsePositiveInteger(lineArr[i++]);
			r2 = Util.parsePositiveInteger(lineArr[i++]);
			g2 = Util.parsePositiveInteger(lineArr[i++]);
			b2 = Util.parsePositiveInteger(lineArr[i++]);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Values should be integers!");
		}
		if (!Util.isConvex(points)) {
			throw new IllegalArgumentException("Polygon is not convex!");
		}
		for (int i = 0; i < points.size(); i++) {
			Point p1 = points.get(i);
			Point p2 = points.get((i+1)%points.size());
			if (Util.distanceSq(p1, p2) <= 9) throw new IllegalArgumentException("Points are too close");
		}
		return new Polygon(points, new Color(r, g, b), new Color(r2, g2, b2));
	}

}
