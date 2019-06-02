/*
 * 
 */
package hr.fer.zemris.java.hw16.jvdraw.drawing;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Used for JList in the main program. This is an adapter for {@link DrawingModel}. 
 * 
 * @author Bruno Iljazović
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject>
		implements DrawingModelListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The adapted drawing model. */
	private DrawingModel drawingModel;

	/**
	 * Instantiates a new drawing object list model.
	 *
	 * @param drawingModel the drawing model
	 */
	public DrawingObjectListModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		drawingModel.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return drawingModel.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return drawingModel.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(this, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(this, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(this, index0, index1);
	}
}
