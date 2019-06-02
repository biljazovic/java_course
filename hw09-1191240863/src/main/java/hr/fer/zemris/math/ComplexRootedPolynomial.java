package hr.fer.zemris.math;

/**
 * Represents an unmodifiable complex polynomial that is constructed from its
 * roots.
 * <p>
 * Provides a method that transforms this polynomial to the
 * {@link ComplexPolynomial}
 * 
 * @author Bruno Iljazović
 */
public final class ComplexRootedPolynomial {
	
	/** The roots of the polynomial. */
	private final Complex[] roots;

	/**
	 * Instantiates a new complex polynomial from its roots.
	 *
	 * @param roots the roots of the polynomial
	 * @throws IllegalArgumentException if the given roots's count is 0.
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		if (roots.length == 0) {
			throw new IllegalArgumentException("Number of roots must be non-zero.");
		}
		this.roots = roots;
		
	}
	
	/**
	 * Applies this polynomial to the given complex number.
	 *
	 * @param z
	 *            the argument complex number
	 * @return the result of applying this polynomial to the argument
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;
		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}
		return result;
	}
	
	/**
	 * Returns the equivalent {@link ComplexPolynomial} to this one.
	 *
	 * @return the equivalent complex polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(Complex.ONE);
		for (Complex root : roots) {
			//multiply by (z - root)
			result = result.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Complex root : roots) {
			if (sb.length() != 0) {
				sb.append("*");
			}
			sb.append(String.format("(z-(%s))", root.toString()));
		}
		return sb.toString();
	}
	
	/**
	 * Returns index of the closest root to the given complex number, within the
	 * given threshold. If there are not roots within the given threshold, -1 is
	 * returned. Indices start from 1.
	 *
	 * @param z
	 *            the complex number for which the closest root is wanted
	 * @param threshold
	 *            the margin of error for proximity of the root
	 * @return the index of the closest root, or -1 if there are none.
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		int closest = -1;
		double minDistance = -1;
		for (int i = 0; i < roots.length; ++i) {
			double distance = z.sub(roots[i]).module();
			if (distance <= threshold) {
				if (closest == -1 || distance < minDistance) {
					closest = i;
					minDistance = distance;
				}
			}
		}
		return closest == -1 ? -1 : closest + 1;
	}
	
	/**
	 * Returns the order of this polynomial
	 *
	 * @return the order of the polynomial
	 */
	public int order() {
		return roots.length;
	}
}
