package hr.fer.zemris.java.hw05.db;

/**
 * Provides a method to compare two strings using some strategy.
 * @author Bruno Iljazović
 */
public interface IComparisonOperator {
	
	/**
	 * Checks whether some condition is met between two given Strings
	 * 
	 * @param value1 first string 
	 * @param value2 second string
	 * @return true iff the condition is met.
	 */
	public boolean satisfied(String value1, String value2);

}
