package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw16.jvdraw.colors.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Tool for drawing circles. First click sets the center and second determines the radius.
 * 
 * @author Bruno Iljazović
 */
public class CircleTool extends AbstractTool {
	
	/**
	 * Instantiates a new circle tool.
	 *
	 * @param drawingModel the drawing model
	 * @param fgColor the fg color
	 * @param bgColor the bg color
	 */
	public CircleTool(DrawingModel drawingModel, IColorProvider fgColor, IColorProvider bgColor) {
		super(drawingModel, fgColor, bgColor);
	}
	
	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	private int getRadius() {
		int radius = (int) Math.sqrt((startPoint.x - endPoint.x) * (startPoint.x - endPoint.x) + 
				(startPoint.y - endPoint.y) * (startPoint.y - endPoint.y));
		return radius;
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (startPoint == null || endPoint == null) return;
		g2d.setColor(fgColor.getCurrentColor());
		int radius = getRadius();
		g2d.drawOval(startPoint.x-radius, startPoint.y - radius, radius * 2, radius * 2);
	}

	@Override
	public GeometricalObject generateObject() {
		return new Circle(startPoint, getRadius(), fgColor.getCurrentColor());
	}

}
