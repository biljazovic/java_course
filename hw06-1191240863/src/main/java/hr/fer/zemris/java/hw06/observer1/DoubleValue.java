package hr.fer.zemris.java.hw06.observer1;

/**
 * Observer that prints the double of the new value when it changes, but only
 * for the n times since its registration with the subject, where n is provided
 * in the constructor.
 * <p>
 * After printing the double value for the n-th time, it de-registers itself
 * from the subject.
 * 
 * @author Bruno Iljazović
 */
public class DoubleValue implements IntegerStorageObserver {
	
	/** Number of times left to print double value */
	private int count;
	
	/**
	 * Instantiates a new double value observer.
	 *
	 * @param count the maximum number of changes this observer will observer
	 */
	public DoubleValue(int count) {
		this.count = count;
	}

	/**
	 * Prints double the new value. If the number of calls of this method reached
	 * count, this observer de-registers itself from the subject.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.printf("Double value: %d%n", istorage.getValue() * 2);
		if (--count == 0) {
			istorage.removeObserver(this);
		}
	}
}
