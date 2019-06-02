package hr.fer.zemris.math;

/**
 * Represents an unmodifiable complex polynomial that is constructed from its
 * coefficients.
 * 
 * @author Bruno Iljazović
 */
public final class ComplexPolynomial {
	
	/** The factors. */
	private final Complex[] factors;
	
	/**
	 * Instantiates a new complex polynomial from the coefficients.
	 *
	 * @param factors the coefficients of the polynomial
	 * @throws IllegalArgumentException if the number of coefficients is zero
	 */
	public ComplexPolynomial(Complex ...factors) {
		if (factors.length == 0) {
			throw new IllegalArgumentException("Number of factors must be non-zero.");
		}
		this.factors = factors;
	}
	
	/**
	 * Returns the order of this polynomial.
	 *
	 * @return the short
	 */
	public short order() {
		return (short)(factors.length - 1);
	}
	
	/**
	 * Multiplies this polynomial with the given one and returns the resulting
	 * polynomial.
	 *
	 * @param p
	 *            the multiplier
	 * @return the result of multiplication of polynomials
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int n = order(), m = p.order();
		Complex[] newFactors = new Complex[n + m + 1];
		for (int i = 0; i < n + m + 1; ++i) {
			newFactors[i] = Complex.ZERO;
		}

		for (int i = 0; i <= n; ++i) {
			for (int j = 0; j <= m; ++j) {
				newFactors[i+j] = newFactors[i+j].add(factors[i].multiply(p.factors[j]));
			}
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Returns the derived polynomial. 
	 *
	 * @return the derived polynomial
	 */
	public ComplexPolynomial derive() {
		int n = order();
		Complex[] newFactors = new Complex[n];
		for (int i = n; i > 0; --i) {
			newFactors[i-1] = factors[i].multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Returns the result of applying this polynomial to the given complex number.
	 *
	 * @param z the argument complex number
	 * @return the result of applying this polynomial to the given argument
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		for (int i = 0, n = order(); i <= n; ++i) {
			result = result.add(z.power(i).multiply(factors[i]));
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = factors.length-1; i >= 0; --i) {
			if (sb.length() != 0) {
				sb.append("+");
			}
			sb.append(String.format("(%s)*z^%d", factors[i].toString(), i));
		}
		
		return sb.toString();
	}
}
