package hr.fer.zemris.java.hw16.jvdraw.drawing;

import java.awt.Graphics2D;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Visitor that paints the objects that visits using the provided graphics object.
 * 
 * @author Bruno Iljazović
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {
	
	/** The graphics object. */
	Graphics2D g2d;
	
	/**
	 * Instantiates a new geometrical object painter.
	 *
	 * @param g2d the graphics object
	 */
	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void visit(Line line) {
		g2d.setColor(line.getColor());
		Point start = line.getStart();
		Point end = line.getEnd();
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}

	@Override
	public void visit(FilledCircle circle) {
		g2d.setColor(circle.getFillColor());
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		g2d.fillOval(center.x-radius, center.y - radius, radius * 2, radius * 2);
		g2d.setColor(circle.getLineColor());
		g2d.drawOval(center.x-radius, center.y - radius, radius * 2, radius * 2);
	}

	@Override
	public void visit(Circle circle) {
		g2d.setColor(circle.getColor());
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		g2d.drawOval(center.x-radius, center.y - radius, radius * 2, radius * 2);
	}

}
