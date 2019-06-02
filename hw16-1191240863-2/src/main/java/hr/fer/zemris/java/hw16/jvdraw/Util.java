package hr.fer.zemris.java.hw16.jvdraw;

/**
 * Provides some utility functions.
 * 
 * @author Bruno Iljazović
 */
public class Util {
	
	/**
	 * Parses the positive integer, or if the given string is invalid throws an
	 * exception.
	 *
	 * @param str
	 *            the str
	 * @return the integer
	 * @throws NumberFormatException if the given string doesn't represent positive integer.
	 */
	public static Integer parsePositiveInteger(String str) {
		Integer coordInt = null;
		coordInt = Integer.parseInt(str);
		if (coordInt < 0) throw new NumberFormatException();
		return coordInt;
	}

}
