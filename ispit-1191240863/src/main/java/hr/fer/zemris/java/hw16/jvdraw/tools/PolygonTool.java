package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw16.jvdraw.Util;
import hr.fer.zemris.java.hw16.jvdraw.colors.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.Polygon;

public class PolygonTool implements Tool {
	
	IColorProvider fgColor;
	IColorProvider bgColor;
	DrawingModel drawingModel;
	Container parent;
	
	public static final double minDistanceSq = 9.0;
	
	public PolygonTool(Container parent, IColorProvider fgColor, IColorProvider bgColor, DrawingModel drawingModel) {
		this.parent = parent;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.drawingModel = drawingModel;
		points = new ArrayList<>();
		tempPoint = null;
	}
	
	List<Point> points;

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (!points.isEmpty() && Util.distanceSq(point, points.get(points.size() - 1)) < minDistanceSq) {
				if (points.size() >= 3) {
					if (Util.distanceSq(points.get(points.size()-1), points.get(0)) < minDistanceSq) {
						return;
					}
					drawingModel.add(new Polygon(points, fgColor.getCurrentColor(), bgColor.getCurrentColor()));
					tempPoint = null;
					points.clear();
				}
				return;
			}
			if (!points.isEmpty() && Util.distanceSq(point, points.get(0)) < minDistanceSq) {
				return;
			}
			points.add(point);
			System.out.println("Added point!");
			if (!Util.isConvex(points)) {
				JOptionPane.showMessageDialog(parent, "Not convex!", "Error", JOptionPane.ERROR_MESSAGE);
				points.remove(points.size() - 1);
				return;
			}
			tempPoint = null;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			points.clear();
			tempPoint = null;
		}
	}
	
	Point tempPoint;

	@Override
	public void mouseMoved(MouseEvent e) {
		tempPoint = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void paint(Graphics2D g2d) {
		if (points == null || points.isEmpty()) return;
		int n = points.size();
		if (tempPoint != null) {
			n++;
		}
		g2d.setColor(bgColor.getCurrentColor());
		int[] xPoints = new int[n]; 
		int[] yPoints = new int[n];
		for (int i = 0; i < points.size(); i++) {
			xPoints[i] = points.get(i).x;
			yPoints[i] = points.get(i).y;
		}
		if (tempPoint != null) {
			xPoints[n-1] = tempPoint.x;
			yPoints[n-1] = tempPoint.y;
			
		}
		g2d.fillPolygon(xPoints, yPoints, n);
		g2d.setColor(fgColor.getCurrentColor());
		g2d.drawPolygon(xPoints, yPoints, n);
	}

}
