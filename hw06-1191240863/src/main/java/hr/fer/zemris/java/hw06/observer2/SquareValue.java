package hr.fer.zemris.java.hw06.observer2;

/**
 * This observer prints the squared new value every time the observed value is
 * changed.
 * 
 * @author Bruno Iljazović
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * Prints the squared new value when it changes.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		int value = istorageChange.getNewValue();
		
		System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
	}

}
