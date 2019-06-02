package hr.fer.zemris.java.hw05.db;

import java.util.regex.Pattern;

/**
 * Contains several implementations of the {@link IComparisonOperator} interface, that define
 * concrete strategies to compare two Strings.
 * <p>Order of letters is determined by current locale settings from operating system
 * 
 * @author Bruno Iljazović
 */
public class ComparisonOperators {

	/** Returns true iff the first string comes before the second in the alphabetical order */
	public static final IComparisonOperator LESS;

	/** Returns true iff the first string comes before the second in the alphabetical order or
	 * if the two strings are equal. */
	public static final IComparisonOperator LESS_OR_EQUALS;
	
	/** Returns true iff the first string comes after the second in the alphabetical order */
	public static final IComparisonOperator GREATER;
	
	/** Returns true iff the first string comes after the second in the alphabetical order or
	 * if the two strings are equal. */
	public static final IComparisonOperator GREATER_OR_EQUALS;
	
	/** Returns true iff the two strings are equal */
	public static final IComparisonOperator EQUALS;
	
	/** Returns true iff the two strings are not equal */
	public static final IComparisonOperator NOT_EQUALS;
	
	/** Returns true iff the first string matches the second string. In the second string there 
	 * can exist at most one wildcard character (*) which matches any string. */
	public static final IComparisonOperator LIKE;
	
	static {
		LESS = (value1, value2) -> value1.compareTo(value2) < 0;

		LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
		
		GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
		
		GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
		
		EQUALS = (value1, value2) -> value1.equals(value2);
		
		NOT_EQUALS = (value1, value2) -> !value1.equals(value2);
		
		LIKE = (value1, value2) -> {
			String[] array = value2.split("\\*", -1);
			if (array.length > 2) {
				throw new IllegalArgumentException(
						"At most one * character is allowed in the pattern for operator LIKE");
			}

			String regex = Pattern.quote(array[0]);
			if (array.length == 2) {
				regex += ".*" + Pattern.quote(array[1]);
			}

			return value1.matches(regex);
		};
	}
}

