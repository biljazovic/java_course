package hr.fer.zemris.java.hw06.observer2;

import java.util.ArrayList;
import java.util.List;

/**
 * The 'Subject' class in the Observer design pattern. Stores one integer value.
 * Methods for adding and removing observers are provided. When the value is
 * changed, {@link IntegerStorageObserver#valueChanged} is called for every
 * observer currently stored.
 * 
 * @author Bruno Iljazović
 */
public class IntegerStorage {

	/** The value which is observed. */
	private int value;

	/** The current observers of the value. */
	private List<IntegerStorageObserver> observers;

	/**
	 * Instantiates a new integer storage.
	 *
	 * @param initialValue the initial value
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<>();
	}

	/**
	 * Adds the observer to the list of observers.
	 *
	 * @param observer
	 *            the observer to be added
	 * @throws NullPointerException
	 *             if the observer is null
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observers.contains(observer)) return;

		observers.add(observer);
	}

	/**
	 * Removes the observer from the observer list, if it exists.
	 *
	 * @param observer the observer to be removed
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Empties the observer list.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Gets the stored value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value to the provided one. Notifies the observers in the observer
	 * list if the value changed. Observers get the old value and the new one.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			IntegerStorageChange iChange = new IntegerStorageChange(this, this.value, value);

			this.value = value;
			
			if (observers != null) {
				for (IntegerStorageObserver observer : new ArrayList<>(observers)) {
					observer.valueChanged(iChange);
				}
			}
		}
	}
}
