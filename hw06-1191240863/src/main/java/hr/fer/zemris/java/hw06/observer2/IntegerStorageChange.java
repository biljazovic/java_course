package hr.fer.zemris.java.hw06.observer2;

/**
 * Wrapper class for the value change. Contains old value and the new one.
 * 
 * @author Bruno Iljazović
 */
public class IntegerStorageChange {

	/** The integer storage containing the new value. */
	private IntegerStorage integerStorage;

	/** The old value. */
	private int oldValue;

	/** The new value. */
	private int newValue;

	/**
	 * Instantiates a new integer storage change.
	 *
	 * @param integerStorage the integer storage with the new value
	 * @param oldValue the old value
	 * @param newValue the new value
	 */
	public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
		super();
		this.integerStorage = integerStorage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Gets the integer storage.
	 *
	 * @return the integer storage
	 */
	public IntegerStorage getIntegerStorage() {
		return integerStorage;
	}

	/**
	 * Gets the old value.
	 *
	 * @return the old value
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Gets the new value.
	 *
	 * @return the new value
	 */
	public int getNewValue() {
		return newValue;
	}
	
}
