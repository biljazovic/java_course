package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an unmodifiable complex number and provides methods of
 * doing basic arithmetic between them.
 * 
 * <p>
 * It also provides a method to parse a string like "8-i7" to an instance of
 * this class and a method to convert an instance of this class into a string
 * like above.
 * 
 * @author Bruno Iljazović
 */
public final class Complex {
	
	/** The real part. */
	private final double real;
	
	/** The imaginary part. */
	private final double imaginary;
	
	/** The Constant 0. */
	public static final Complex ZERO = new Complex(0, 0);

	/** The Constant 1. */
	public static final Complex ONE = new Complex(1, 0);

	/** The Constant -1. */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/** The Constant i. */
	public static final Complex IM = new Complex(0, 1);

	/** The Constant -i. */
	public static final Complex IM_NEG = new Complex(0, -1);
	
	/**
	 * Default constructor. Constructs a complex number 0.
	 */
	public Complex() {
		real = imaginary = 0;
	}

	/**
	 * Instantiates a new complex number from its real and imaginary component.
	 *
	 * @param real
	 *            the real part
	 * @param imaginary
	 *            the imaginary part
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Returns the module of the complex number, i.e. distance to the origin in the
	 * complex plane.
	 *
	 * @return the module of the complex number.
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}

	/**
	 * Multiplies the given complex number and the complex number that called mul
	 * and returns the result, without modifying the caller.
	 *
	 * @param other
	 *            operand in multiplication
	 * @return the result of multiplication
	 * 
	 * @throws NullPointerException
	 *             if the parameter is null
	 */
	public Complex multiply(Complex other) {
		if (other ==  null) throw new NullPointerException("other was null.");
		return new Complex(
				real * other.real - imaginary * other.imaginary,
				real * other.imaginary + imaginary * other.real
		);
	}
	
	/**
	 * Divides the complex number that called div and the given complex number and
	 * returns the result, without modifying the caller. Division by zero is
	 * possible (result will have inf).
	 *
	 * @param other
	 *            operand in division
	 * @return the result of division
	 * 
	 * @throws NullPointerException
	 *             if the parameter is null
	 */
	public Complex divide(Complex other) {
		if (other ==  null) throw new NullPointerException("other was null.");
		double denominator = other.real * other.real + other.imaginary * other.imaginary;
		return new Complex(
				(real * other.real + imaginary * other.imaginary) / denominator,
				(imaginary * other.real - real * other.imaginary) / denominator
		);
	}
	
	/**
	 * Adds the given complex number to the complex number that called add and
	 * returns the result, without modifying the caller.
	 *
	 * @param other
	 *            operand in addition
	 * @return the result of addition
	 * 
	 * @throws NullPointerException
	 *             if the parameter is null
	 */
	public Complex add(Complex other) {
		if (other ==  null) throw new NullPointerException("other was null.");
		return new Complex(
				real + other.real,
				imaginary + other.imaginary
		);
	}

	/**
	 * Subtracts the given complex number from the complex number that called sub
	 * and returns the result, without modifying the caller.
	 *
	 * @param other
	 *            operand in subtraction
	 * @return the result of subtraction
	 * 
	 * @throws NullPointerException
	 *             if the parameter is null
	 */
	public Complex sub(Complex other) {
		if (other ==  null) throw new NullPointerException("other was null.");
		return new Complex(
				real - other.real,
				imaginary - other.imaginary
		);
	}
	
	/**
	 * Returns -this.
	 *
	 * @return -this
	 */
	public Complex negate() {
		return new Complex(
				-real,
				-imaginary
		);
	}
	
	/**
	 * Returns this complex number raised to the power of the given natural number.
	 * 
	 * @param n
	 *            Power.
	 * @return complex number raised to the given power.
	 * 
	 * @throws IllegalArgumentException
	 *             if the given number isn't natural.
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new ArithmeticException("Exponent must be non-negatives. Was " + n);
		}
		
		double angle = Math.atan2(imaginary, real) * n;
		double magnitude = Math.pow(module(), n);
		
		return new Complex(
				magnitude * Math.cos(angle),
				magnitude * Math.sin(angle)
		);
	}
	
	/**
	 * Returns the nth root of this complex number where n is given positive integer
	 * as a list of n Complex numbers.
	 *
	 * @param n
	 *            positive integer
	 * @return the complex number
	 * 
	 * @throws IllegalArgumentException
	 *             if the given number isn't positive
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n should be positive, but was " + n);
		}
		
		double angle = Math.atan2(imaginary, real) / n;
		double magnitude = Math.pow(module(), 1.0 / n);
		
		List<Complex> result = new ArrayList<>(n);
		for (int i = 0; i < n; ++i) {
			result.add(new Complex(
					magnitude * Math.cos(angle),
					magnitude * Math.sin(angle)
			));
			angle += 2 * Math.PI / n;
		}
			
		return result;
	}
	
	/**
	 * Parses the given string and returns the complex number made from it. String
	 * should be of the general form "a+ib" where a and b are decimal numbers. Parts
	 * that are zero can be left out, but empty string is not allowed.
	 * 
	 * <p>
	 * Acceptable strings are, for example: "i", "-1", "-1-i", "2+i3", "2.53+i9e-3"
	 * 
	 * <p>
	 * Unacceptable: "i-1", "12+2i", "i+i"
	 *
	 * @param s
	 *            the string which represents a complex number
	 * @return the complex number represented by a given string
	 */
	public static Complex parse(String s) {
		if (s == null) throw new NullPointerException("s was null.");
		if (s.length() == 0) throw new IllegalArgumentException("s was empty string.");

		s = s.trim();

		//standardize form to be a+ib
		if (s.indexOf("i") == -1) {
			s = s + "+i0";
		}
		if (s.endsWith("i")) {
			s = s + "1";
		}
		if (s.startsWith("+i") || s.startsWith("-i")) {
			s = "0" + s;
		} else if (s.startsWith("i")) {
			s = "0+" + s;
		}
		
		Pattern pattern = Pattern.compile("(.*?[^eE])\\s*([+-])\\s*i([^+-].*)");
		Matcher matcher = pattern.matcher(s);
		
		if (!matcher.matches()) throw new IllegalArgumentException("Invalid complex number.");
		
		double realPart, imaginaryPart;
		
		try {
			realPart = Double.parseDouble(matcher.group(1));
			imaginaryPart = Double.parseDouble(matcher.group(3));
			if (matcher.group(2).equals("-")) {
				imaginaryPart = -imaginaryPart;
			}
			
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid complex number: " + ex);
		}
		
		return new Complex(realPart, imaginaryPart);
	}

	/**
	 * Returns true iff the given complex number's real and imaginary parts are
	 * within the distance of threshold from this complex number.
	 *
	 * @param other
	 *            the other complex number
	 * @param threshold
	 *            allowed distance between two decimal numbers to still be
	 *            considered equals
	 * @return true, iff the given number is equal to this one
	 */
	public boolean equals(Complex other, double threshold) {
		if (other == null) return false;
		if (Math.abs(real - other.real) > threshold) return false;
		if (Math.abs(imaginary - other.imaginary) > threshold) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";

		result += Double.valueOf(real).toString();
		if (imaginary >= 0)
			result += "+";
		result += Double.valueOf(imaginary).toString();
		result += "i";

		return result;
	}
}
