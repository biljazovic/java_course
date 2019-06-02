package hr.fer.zemris.java.hw16.jvdraw.drawing;

/**
 * Listener for changes in {@link DrawingModel}.
 * 
 * @author Bruno Iljazović
 */
public interface DrawingModelListener {

	/**
	 * Objects added.
	 *
	 * @param source the source
	 * @param index0 the index 0
	 * @param index1 the index 1
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Objects removed.
	 *
	 * @param source the source
	 * @param index0 the index 0
	 * @param index1 the index 1
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Objects changed.
	 *
	 * @param source the source
	 * @param index0 the index 0
	 * @param index1 the index 1
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}