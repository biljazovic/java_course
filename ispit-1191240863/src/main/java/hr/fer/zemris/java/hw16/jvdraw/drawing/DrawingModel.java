package hr.fer.zemris.java.hw16.jvdraw.drawing;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Stores all drawn objects and provides methods for adding new objects,
 * removing them and changing their order.
 * 
 * @author Bruno Iljazović
 */
public interface DrawingModel {

	/**
	 * Gets the number of objects.
	 *
	 * @return the number of objects.
	 */
	int getSize();

	/**
	 * Gets the object at the given index.
	 *
	 * @param index the index
	 * @return the object
	 */
	GeometricalObject getObject(int index);

	/**
	 * Adds the new object.
	 *
	 * @param object the object
	 */
	void add(GeometricalObject object);

	/**
	 * Adds the drawing model listener.
	 *
	 * @param l the listener
	 */
	void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes the drawing model listener.
	 *
	 * @param l the listener
	 */
	void removeDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Removes the given object.
	 *
	 * @param object the object
	 */
	void remove(GeometricalObject object);
	
	/**
	 * Changes order by shifting the given object by offset. If the new position is
	 * illegal, or zero, nothing is changed.
	 *
	 * @param object
	 *            the object
	 * @param offset
	 *            the offset
	 */
	void changeOrder(GeometricalObject object, int offset);
}