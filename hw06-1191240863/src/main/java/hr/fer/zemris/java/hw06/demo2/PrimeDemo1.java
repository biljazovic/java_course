package hr.fer.zemris.java.hw06.demo2;

/**
 * Test program for the {@link PrimesCollection} class. It tests basic
 * functionality of the collection.
 * 
 * @author Bruno Iljazović
 */
public class PrimeDemo1 {

	/**
	 * Main function that is called when the program is run.
	 * 
	 * @param args
	 *            Command line arguments. Not used in this program.
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5);
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}

	}

}
