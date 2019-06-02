package hr.fer.zemris.java.hw06.observer1;

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
	public void valueChanged(IntegerStorage istorage) {
		int value = istorage.getValue();
		
		System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
	}

}
