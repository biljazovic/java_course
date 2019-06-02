package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;
import hr.fer.zemris.java.hw16.jvdraw.objects.Polygon;

/**
 * Calculates the bounding box of objects that this visits.
 * 
 * @author Bruno Iljazović
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	
	/**
	 * Gets the calculated bounding box.
	 *
	 * @return the bounding box
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(minx, miny, maxx - minx, maxy - miny);
	}
	
	/**
	 * Instantiates a new bounding box calculator.
	 */
	public GeometricalObjectBBCalculator() {
		minx = miny = Integer.MAX_VALUE;
		maxx = maxy = Integer.MIN_VALUE;
	}
	
	/** The minx. */
	int minx;
	
	/** The miny. */
	int miny;
	
	/** The maxx. */
	int maxx;
	
	/** The maxy. */
	int maxy;
	
	@Override
	public void visit(Line line) {
		minx = Math.min(line.getStart().x, minx);
		minx = Math.min(line.getEnd().x, minx);
		maxx = Math.max(line.getStart().x, maxx);
		maxx = Math.max(line.getEnd().x, maxx);
		miny = Math.min(line.getStart().y, miny);
		miny = Math.min(line.getEnd().y, miny);
		maxy = Math.max(line.getStart().y, maxy);
		maxy = Math.max(line.getEnd().y, maxy);
	}

	@Override
	public void visit(Circle circle) {
		int r = circle.getRadius();
		Point center = circle.getCenter();
		minx = Math.min(center.x - r, minx);
		miny = Math.min(center.y - r, miny);
		maxx = Math.max(center.x + r, maxx);
		maxy = Math.max(center.y + r, maxy);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		int r = filledCircle.getRadius();
		Point center = filledCircle.getCenter();
		minx = Math.min(center.x - r, minx);
		miny = Math.min(center.y - r, miny);
		maxx = Math.max(center.x + r, maxx);
		maxy = Math.max(center.y + r, maxy);
	}

	@Override
	public void visit(Polygon polygon) {
		List<Point> points = polygon.getPoints();
		for (Point point : points) {
			minx = Math.min(minx, point.x);
			miny = Math.min(miny, point.y);
			maxx = Math.max(maxx, point.x);
			maxy = Math.max(maxy, point.y);
		}
	}

}
