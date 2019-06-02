package hr.fer.zemris.java.hw06.observer2;

/**
 * Example program that adds some observers to the integer storage and changes
 * the stored value several times.
 * 
 * @author Bruno Iljazović
 */
public class ObserverExample {

	/**
	 * Called when the program is run.
	 * @param args the command line arguments, not used in this program.
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);

		IntegerStorageObserver observer = new SquareValue();

		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(observer);
		
		
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);

		istorage.removeObserver(observer);
		
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}
