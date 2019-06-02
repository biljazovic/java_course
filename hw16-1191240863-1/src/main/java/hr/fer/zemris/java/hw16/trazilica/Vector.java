/*
 * 
 */
package hr.fer.zemris.java.hw16.trazilica;

/**
 * Represents one vector of the size given in the constructor.
 * 
 * @author Bruno Iljazović
 */
public class Vector {
	
	/** The values. */
	private double[] values;
	
	/** The size. */
	private int size;
	
	/**
	 * Instantiates a new vector with the given size.
	 *
	 * @param size the size
	 */
	public Vector(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size was " + size + ", must be positive!");
		}
		this.size = size;
		values = new double[size];
	}

	/**
	 * Sets the value at the given index at the new given value.
	 *
	 * @param index the index
	 * @param value the new value
	 */
	public void set(int index, double value) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index was " + index);
		}
		values[index] = value;
	}
	
	/**
	 * Gets the the value at the given index.
	 *
	 * @param index the index
	 * @return the double
	 */
	public double get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index was " + index);
		}
		return values[index];
	}

	/**
	 * Returns the cosine of the angle between two vectors.
	 *
	 * @param a the first vector
	 * @param b the second vector
	 * @return the cosine of the angle between the vectors.
	 */
	public static double cos(Vector a, Vector b) {
		if (a.size != b.size) throw new IllegalArgumentException("Vector dimensions are not the same.");
		double scalar = 0;
		double absA = 0, absB = 0;
		for (int i = 0; i < a.size; i++) {
			double aV = a.values[i], bV = b.values[i];
			scalar += aV * bV;
			absA += aV * aV;
			absB += bV * bV;
		}
		return scalar / Math.sqrt(absA) / Math.sqrt(absB);
	}
}
