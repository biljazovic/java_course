package hr.fer.zemris.java.hw06.demo2;

/**
 * Test program for the {@link PrimesCollection} class. It tests the
 * independence of the iterators in the collection.
 * 
 * @author Bruno Iljazović
 */
public class PrimeDemo2 {

	/**
	 * Main function that is called when the program is run.
	 * 
	 * @param args
	 *            Command line arguments. Not used in this program.
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.printf("Got prime pair: %d, %d%n", prime, prime2);
			}
		}

	}

}
