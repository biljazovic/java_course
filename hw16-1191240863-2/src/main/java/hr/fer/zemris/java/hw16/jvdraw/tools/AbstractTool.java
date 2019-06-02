/*
 * 
 */
package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.colors.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Implements all {@link Tool} methods except the {@link Tool#paint}.
 * 
 * @author Bruno Iljazović
 */
public abstract class AbstractTool implements Tool {
	
	/** The start point. */
	Point startPoint;
	
	/** The end point. */
	Point endPoint;
	
	/** The drawing model. */
	DrawingModel drawingModel;
	
	/** The fg color. */
	IColorProvider fgColor;
	
	/** The bg color. */
	IColorProvider bgColor;
	
	/**
	 * Generates object from the paramaters.
	 *
	 * @return the geometrical object
	 */
	public abstract GeometricalObject generateObject();
	
	/**
	 * Instantiates a new abstract tool.
	 *
	 * @param drawingModel the drawing model
	 * @param fgColor the fg color
	 * @param bgColor the bg color
	 */
	public AbstractTool(DrawingModel drawingModel, IColorProvider fgColor, IColorProvider bgColor) {
		this.drawingModel = drawingModel;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (startPoint == null) {
			startPoint = e.getPoint();
		} else {
			endPoint = e.getPoint();
			drawingModel.add(generateObject());
			startPoint = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (startPoint != null) {
			endPoint = e.getPoint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) { }
}
