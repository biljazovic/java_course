package hr.fer.zemris.java.hw06.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class provides an iterator to the collection of prime numbers. Number of
 * primes in the collection is provided through a constructor.
 *
 * @author Bruno Iljazović
 */
public class PrimesCollection implements Iterable<Integer> {

	/** Number of prime numbers in the collection */
	private int count;

	/**
	 * Instantiates a new collection of prime numbers.
	 *
	 * @param count number of prime numbers.
	 */
	public PrimesCollection(int count) {
		this.count = count;
	}

	/**
	 * Iterator class for the Collection. Provides method for fetching next prime
	 * number and method to check if the next prime exists.
	 */
	private class IteratorImpl implements Iterator<Integer> {

		/** Last generated prime number. At the beginning is 0 */
		private int prime;
		
		/** Number of primes left in the collection. */
		private int myCount = count;
		
		/**
		 * Checks if the provided number is prime. 
		 *
		 * @param number candidate for prime number
		 * @return true, if, and only if, the provided number is prime
		 */
		private boolean isPrime(int number) {
			if (number < 2)
				return false;

			for (int i = 2; i * i <= number; ++i) {
				if (number % i == 0) {
					return false;
				}
			}

			return true;
		}

		@Override
		public boolean hasNext() {
			return myCount > 0;
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more prime numbers!");
			}
			--myCount;

			for (++prime; !isPrime(prime); ++prime);

			return Integer.valueOf(prime);
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl();
	}

}
