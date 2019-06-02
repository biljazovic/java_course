package hr.fer.zemris.java.hw16.jvdraw.drawing;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObjectListener;

/**
 * Implements the {@link DrawingModel} interface.
 * 
 * @author Bruno Iljazović
 */
public class DrawingModelImpl implements DrawingModel, GeometricalObjectListener {
	
	/** The objects. */
	private List<GeometricalObject> objects;
	
	/** The listeners. */
	private List<DrawingModelListener> listeners;
	
	/**
	 * Instantiates a new drawing model.
	 */
	public DrawingModelImpl() {
		objects = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		if (index < 0 || index >= objects.size()) 
			throw new IndexOutOfBoundsException(
					"Index was " + index + ", but size is " + objects.size() + ".");
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, objects.size()-1, objects.size()-1);
		}
		object.addGeometricalObjectListener(this);
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		if (listeners.contains(l)) return;
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void remove(GeometricalObject object) {
		if (object == null) return;
		int index = objects.indexOf(object);
		if (index > -1) {
			for (DrawingModelListener l : listeners) {
				l.objectsRemoved(this, index, index);
			}
			objects.remove(index);
		}
		object.removeGeometricalObjectListener(this);
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		if (object == null) return;
		int index = objects.indexOf(object);
		if (index == -1) return;
		int index1 = index + offset;
		if (index1 < 0 || index1 >= objects.size()) return;
		objects.remove(index);
//		if (offset > 0) index1--;
		objects.add(index1, object);
		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, Math.min(index, index1), Math.max(index, index1));
		}
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = objects.indexOf(o);
		if (index > -1) {
			for (DrawingModelListener l : listeners) {
				l.objectsChanged(this, index, index);
			}
		}
	}

}
