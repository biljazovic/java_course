package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw16.jvdraw.colors.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * 
 * Tool for drawing filled circles. First click sets the center and second
 * determines the radius.
 * 
 * @author Bruno Iljazović
 */
public class FilledCircleTool extends AbstractTool {
	
	/**
	 * Instantiates a new filled circle tool.
	 *
	 * @param drawingModel the drawing model
	 * @param fgColor the fg color
	 * @param bgColor the bg color
	 */
	public FilledCircleTool(DrawingModel drawingModel, IColorProvider fgColor, IColorProvider bgColor) {
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
		g2d.setColor(bgColor.getCurrentColor());
		int radius = getRadius();
		g2d.fillOval(startPoint.x-radius, startPoint.y - radius, radius * 2, radius * 2);
		g2d.setColor(fgColor.getCurrentColor());
		g2d.drawOval(startPoint.x-radius, startPoint.y - radius, radius * 2, radius * 2);

	}

	@Override
	public GeometricalObject generateObject() {
		return new FilledCircle(startPoint, getRadius(), fgColor.getCurrentColor(),
				bgColor.getCurrentColor());
	}

}
