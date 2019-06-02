package hr.fer.zemris.java.hw06.observer1;

/**
 * This observer counts the number of times the observed value is changed (and
 * writes the number to the standard output) since the registration.
 * 
 * @author Bruno Iljazović
 */
public class ChangeCounter implements IntegerStorageObserver {
	
	/** Number of times the observed value was changed since the registration. */
	private int counter;

	/**
	 * Prints the number of times the observed value was changed.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.printf("Number of value changes since tracking: %d%n", ++counter);
	}

}
