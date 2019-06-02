package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;

/**
 * Wrapper for any object. If the wrapped object is null, Integer, Double or
 * String with correct number in it (further in javadoc - '<b>number</b>'),
 * methods for basic arithmetic are provided. If any of these methods are called
 * on the objects that aren't like above, exception is thrown.
 * <p>
 * In arithmetics with other values, null-reference is interpreted as Integer
 * with value 0, and number-strings are interpreted as Integers if they can be
 * such, and Double otherwise.
 * 
 * <p>
 * If any of the operands in the operations are Double (after conversion of
 * string-numbers and null) then the whole operation is done with Doubles, and
 * result is double. If both are integers, result is integer, and
 * ArithmeticException can be thrown.
 *
 * @author Bruno Iljazović
 */
public class ValueWrapper {
	
	/**
	 * Instantiates a new value wrapper.
	 *
	 * @param value the value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Adds the given value to this one if both of them are numbers. This value
	 * is changed as a result. It can also change its type.
	 *
	 * @param incValue
	 *            value to be added to this one.
	 * @throws RuntimeException
	 *             if the given value, and/or this one aren't numbers
	 */
	public void add(Object incValue) {
		value = operate(
				transform(value),
				transform(incValue),
				(v1, v2) -> v1 + v2,
				(v1, v2) -> v1 + v2
		);
	}

	/**
	 * Subtracts the given value from this one if both of them are numbers. This
	 * value is changed as a result. It can also change its type.
	 *
	 * @param decValue
	 *            value to be subtracted from this one
	 * @throws RuntimeException
	 *             if the given value, and/or this one aren't numbers
	 */
	public void subtract(Object decValue) {
		value = operate(
				transform(value),
				transform(decValue),
				(v1, v2) -> v1 - v2,
				(v1, v2) -> v1 - v2
		);
	}

	/**
	 * Multiplies the given value with this one if both of them are numbers. This
	 * value is changed as a result. It can also change its type.
	 *
	 * @param mulValue
	 *            value to be multiplied with this one
	 * @throws RuntimeException
	 *             if the given value, and/or this one aren't numbers
	 */
	public void multiply(Object mulValue) {
		value = operate(
				transform(value),
				transform(mulValue),
				(v1, v2) -> v1 * v2,
				(v1, v2) -> v1 * v2
		);
	}

	/**
	 * Divides this value with the given one if both of them are numbers. This value
	 * is changed as a result. It can also change its type.
	 *
	 * @param divValue
	 *            divisor
	 * @throws RuntimeException
	 *             if the given value, and/or this one aren't numbers
	 * @throws ArithmeticException
	 *             if both this value and the given one are integers (after
	 *             conversion), and the given one is zero
	 */
	public void divide(Object divValue) {
		value = operate(
				transform(value),
				transform(divValue),
				(v1, v2) -> v1 / v2,
				(v1, v2) -> v1 / v2 //integer division, cannot divide by zero
		);
	}

	/**
	 * Compares this value with the given one, if both of them are numbers. Returns
	 * negative integer if this value is less then the given one, 0 if they are
	 * equal, and positive integer otherwise.
	 *
	 * @param withValue
	 *            value with which this one is to be compared
	 * @return negative integer if this value is less then the given one, 0 if they
	 *         are equal, positive if this one is greater
	 * @throws RuntimeException
	 *             if the given value, and/or this one aren't numbers
	 */
	public int numCompare(Object withValue) {
		return numCompare(
				transform(value),
				transform(withValue)
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueWrapper other = (ValueWrapper) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * Wrapped value.
	 */
	private Object value;

	/**
	 * Compares two number values. Returns negative integer if the first one is
	 * lesser, 0 if they are equal and positive if the first one is greater.
	 *
	 * @param value1
	 *            first value number
	 * @param value2
	 *            second value number
	 * @return negative integer if the first value is lesser, 0 if they are equal
	 *         and positive if the first one is greater
	 */
	private static int numCompare(Number value1, Number value2) {
		if (value1 instanceof Double || value2 instanceof Double) {
			return Double.compare(
					(value1).doubleValue(),
					(value2).doubleValue()
			);
		}
		return Integer.compare(
				(Integer)value1,
				(Integer)value2
		);
	}

	/**
	 * If the given value is Integer or Double, it is returned without modifying it.
	 * If the given value is null, Integer with value zero is returned. If the given
	 * value is string that can be parsed as integer, Integer with appropriate value
	 * is returned. If it can be parsed as Double, Double is returned. Otherwise,
	 * exception is thrown.
	 *
	 * @param value
	 *            the value to be transformed
	 * @return Instance of Integer or Double, result of transformation of the given
	 *         value
	 * @throws RuntimeException
	 *             if the given value is not a 'number'
	 */
	public static Number transform(Object value) {
		if (value == null) {
			return Integer.valueOf(0);
		}
		if (value instanceof String) {
			try {
				return Integer.parseInt((String)value);
			} catch(NumberFormatException ex) {
				try {
					return Double.parseDouble((String)value);
				} catch (NumberFormatException ex2) {
					throw new RuntimeException(
							"String " + value + " cannot be interpreted as a number");
				}
			}
		} else if (!(value instanceof Double || value instanceof Integer)) {
			throw new RuntimeException(value.toString() + " cannot be interpreted as a number.");
		}
		return (Number)value;
	}
	
	/**
	 * Returns the result of applying the provided operator to the two provided
	 * values. If the values are Integers, provided integer operator is used, and if
	 * at least one of them is Double, double operator is used.
	 *
	 * @param value1
	 *            first value
	 * @param value2
	 *            second value
	 * @param operatorDouble
	 *            double operator
	 * @param operatorInteger
	 *            integer operator
	 * @return result of operation
	 */
	private static Number operate(Number value1, Number value2, DoubleBinaryOperator operatorDouble,
			IntBinaryOperator operatorInteger) {
		if (value1 instanceof Double || value2 instanceof Double) {
			return Double.valueOf(operatorDouble.applyAsDouble(
					(value1).doubleValue(),
					(value2).doubleValue()
			));
		}

		if (!(value1 instanceof Integer && value2 instanceof Integer)) {
			throw new IllegalArgumentException("values must be Integer or Double");
		}
		return Integer.valueOf(operatorInteger.applyAsInt(
				((Integer)value1).intValue(),
				((Integer)value2).intValue()
		));
	}
	
}
