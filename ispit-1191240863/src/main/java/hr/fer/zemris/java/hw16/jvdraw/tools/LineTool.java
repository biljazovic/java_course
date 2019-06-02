package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw16.jvdraw.colors.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Tool for drawing colored lines. First click determines the start poitn and
 * second one end point.
 * 
 * @author Bruno Iljazović
 */
public class LineTool extends AbstractTool {

	/**
	 * Instantiates a new line tool.
	 *
	 * @param drawingModel the drawing model
	 * @param fgColor the fg color
	 * @param bgColor the bg color
	 */
	public LineTool(DrawingModel drawingModel, IColorProvider fgColor, IColorProvider bgColor) {
		super(drawingModel, fgColor, bgColor);
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (startPoint == null || endPoint == null) return;
//		g2d.setStroke(new BasicStroke(5));
		g2d.setColor(fgColor.getCurrentColor());
		g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	@Override
	public GeometricalObject generateObject() {
		return new Line(startPoint, endPoint, fgColor.getCurrentColor());
	}

}
