package hr.fer.zemris.java.hw02;

/**
 * This class represents an unmodifiable complex number and provides methods of
 * doing basic arithmetic between them.
 * 
 * <p> It also provides a method to parse a string like "8-7i" to an instance of
 * this class and a method to convert an instance of this class into a string
 * like above.
 *
 * @author Bruno Iljazović
 */
public class ComplexNumber {

	/** The real part of the complex number. */
	private double real;
	
	/** The imaginary part of the complex number. */
	private double imaginary;
	
	/** The maximum difference between two numbers for which they are considered equal. */
	public static double THRESHOLD = 1E-6;
	
	/**
	 * Instantiates a new complex number from its real and imaginary component.
	 *
	 * @param real the real part
	 * @param imaginary the imaginary part
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Returns a new complex number from its real component. Imaginary component is set to 0.
	 *
	 * @param real the real part.
	 * @return the complex number from the real component.
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Returns a new complex number from its imaginary component. Real component is set to 0.
	 *
	 * @param imaginary the imaginary part.
	 * @return the complex number from the imaginary component.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * Returns the complex number from the given polar coordinates.
	 * 
	 * @param magnitude the magnitude of the complex number (distance to the origin in 
	 * the complex plane).
	 * @param angle the angle in radians.
	 * 
	 * @return the complex number from the given polar coordinates.
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		if (Math.abs(magnitude) < THRESHOLD) {
			return new ComplexNumber(0, 0);
		}
		if (magnitude < 0) throw new IllegalArgumentException("magnitude should be nonnegative, but was " + magnitude + ".");
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	

	/**
	 * Parses the given string and returns the complex number made from it. String should 
	 * be of the general form "a+bi" where a and b are decimal numbers.
	 * 
	 * <p> Acceptable strings are, for example: "i", "-1", "-1-i", "2+3i", "2.53+9e-3i"
	 * 
	 * <p> Unacceptable: "i-1", "1+2+i", "i+i"
	 *
	 * @param s the string which represents a complex number
	 * @return the complex number represented by a given string
	 */
	public static ComplexNumber parse(String s) {
		if (s == null) throw new NullPointerException("s was null.");

        s = s.replaceAll("\\s+","");
		try {
			if (s.charAt(s.length()-1) != 'i') {
				return fromReal(Double.parseDouble(s));
			}

			int positive = 1;
			//removing sign at the beginning if it exists
			if (s.charAt(0) == '+' || s.charAt(0) == '-') {
				positive = s.charAt(0) == '-' ? -1 : 1;
				s = s.substring(1);
			}

			int indexOfI = s.indexOf('i');
			if (indexOfI == 0 || s.charAt(indexOfI-1) == '+' || s.charAt(indexOfI-1) == '-') {
				s = s.substring(0, indexOfI) + "1i";
			}

			int indexOfPlus = s.indexOf('+');
			int indexOfMinus = s.indexOf('-');
			if (indexOfPlus == -1 && indexOfMinus == -1) {
				return fromImaginary(positive * Double.parseDouble(s.substring(0, s.length() - 1)));
			}
			
			if (indexOfPlus != -1 && indexOfMinus != -1) { // has both plus and minus inside 
				throw new NumberFormatException();
			}
			int indexOfOperand = (indexOfPlus == -1) ? indexOfMinus : indexOfPlus;

			return new ComplexNumber(
					positive * Double.parseDouble(s.substring(0, indexOfOperand)),
					Double.parseDouble(s.substring(indexOfOperand, s.length() - 1))
					);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("s is not valid complex number. Was '" + s + "'.");
		}
	}

	/**
	 * Gets the real part of the complex number
	 *
	 * @return the real part of the complex number.
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Gets the imaginary part of the complex number.
	 *
	 * @return the imaginary part of the complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Returns the magnitude of the complex number in polar coordiantes, i. e. 
	 * distance to the origin in the complex plane.
	 *
	 * @return the magnitude of the complex number.
	 */
	public double getMagnitude() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Returns the angle of the complex number in polar coordinates, in radians, 
	 * in range [0, 2*PI]
	 *
	 * @return the angle of the complex number in polar coordinates. Can range in 
	 * [0, 2*PI]
	 */
	public double getAngle() {
		double result = Math.atan2(imaginary, real);

		if (imaginary < 0) {
			result = 2 * Math.PI + result;
		}
		//if (Math.abs(result) < THRESHOLD) result = 2 * Math.PI;

		return result;
	}
	
	/**
	 * Adds the given complex number to the complex number that called add and 
	 * returns the result, without modifying the caller.
	 *
	 * @param c second operand in addition
	 * @return the result of addition
	 * 
	 * @throws NullPointerException if the parameter is null
	 */
	public ComplexNumber add(ComplexNumber c) {
		if (c == null) throw new NullPointerException("c was null.");

		return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
	}
		
	/**
	 * Subtracts the given complex number from the complex number that called sub 
	 * and returns the result, without modifying the caller.
	 *
	 * @param c second operand in subtraction
	 * @return the result of subtraction
	 * 
	 * @throws NullPointerException if the parameter is null
	 */
	public ComplexNumber sub(ComplexNumber c) {
		if (c == null) throw new NullPointerException("c was null.");

		return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
	}
	
	/**
	 * Multiplies the given complex number and the complex number that called mul
	 * and returns the result, without modifying the caller.
	 *
	 * @param c second operand in multiplication
	 * @return the result of multiplication
	 * 
	 * @throws NullPointerException if the parameter is null
	 */
	public ComplexNumber mul(ComplexNumber c) {
		if (c == null) throw new NullPointerException("c was null.");

		return fromMagnitudeAndAngle(
				this.getMagnitude() * c.getMagnitude(),
				this.getAngle() + c.getAngle()
				);
	}
	
	/**
	 * Divides the complex number that called div and the given complex number
	 * and returns the result, without modifying the caller.
	 * 
	 * <p> If the parameter equals zero determined by {@link equals} method, 
	 * exception is thrown.
	 *
	 * @param c second operand in division
	 * @return the result of division
	 * 
	 * @throws NullPointerException if the parameter is null
	 * @throws IllegalArgumentException if the parameter equals zero
	 */
	public ComplexNumber div(ComplexNumber c) {
		if (c == null) throw new NullPointerException("c was null.");
		if (c.getMagnitude() < THRESHOLD) throw new IllegalArgumentException("Division by zero is not possible.");

		return fromMagnitudeAndAngle(
				this.getMagnitude() / c.getMagnitude(),
				this.getAngle() - c.getAngle()
				);
	}
	
	/**
	 * Returns this complex number raised on the power of the given natural number.
	 * 
	 * @param n Power.
	 * @return complex number raised on the given power.
	 * 
	 * @throws IllegalArgumentException if the given number isn't natural.
	 */
	public ComplexNumber power(int n) {
		if (n < 0) throw new IllegalArgumentException("n should be nonnegative, but was " + n + ".");
		
		return fromMagnitudeAndAngle(
				Math.pow(getMagnitude(), n),
				getAngle() * n
				);
	}
	
	/**
	 * Returns the nth root of this complex number where n is given positive integer as an array
	 * of n ComplexNumbers. 
	 *
	 * @param n positive integer 
	 * @return the complex number
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) throw new IllegalArgumentException("n should be positive, but was " + n + ".");
		
		ComplexNumber[] resultArray = new ComplexNumber[n];
		double magnitude = Math.pow(getMagnitude(), 1.0 / n);
		double angle = getAngle() / n;
		
		for (int i = 0; i < n; ++i) {
			resultArray[i] = fromMagnitudeAndAngle(
					magnitude,
					angle + 2 * Math.PI * i / n
					);
		}
		return resultArray;
	}
	
	/**
	 * Returns String made from this complex number in the form "a+bi"
	 * @return String made from this complex number in the form "a+bi"
	 */
	@Override
	public String toString() {
		boolean hasReal = Math.abs(real) > THRESHOLD;
		boolean hasImaginary = Math.abs(imaginary) > THRESHOLD;
		
		String result = "";
		
		if (hasReal) {
			result += Double.valueOf(real).toString();
			if (hasImaginary && imaginary > 0) result += "+";
		}
		if (hasImaginary) {
			if (Math.abs(imaginary - 1) > THRESHOLD) {
				result += Double.valueOf(imaginary).toString();
			}
			result += "i";
		}
		if (result.length() == 0) result = "0";
		
		return result;
	}
	
	/**
	 * Checks if this complex number is equal to the given one. Equality is achieved if
	 * both real and imaginary parts differ at most by the value of THRESHOLD.
	 * 
	 * @return {@code true} if they are equal, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
	     if (obj == null) {
	         return false;
	     }
	     if (!(obj instanceof ComplexNumber)) {
	         return false;
	     }
	     ComplexNumber other = (ComplexNumber)obj;
	     
	     if (Math.abs(other.real - this.real) > THRESHOLD) {
	         return false;
	     }
	     if (Math.abs(other.imaginary - this.imaginary) > THRESHOLD) {
	         return false;
	     }
	     
	     return true;
	}
}	
