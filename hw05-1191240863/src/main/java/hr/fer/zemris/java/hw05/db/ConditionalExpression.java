package hr.fer.zemris.java.hw05.db;

/**
 * Represents one conditional expression which can tell whether some student record passes the
 * condition.
 * 
 * @see IComparisonOperator
 * @see IFieldValueGetter
 * 
 * @author Bruno Iljazović
 */
public class ConditionalExpression {
	
	/** The field getter. Takes a record and returns the requested field */
	private IFieldValueGetter fieldGetter;
	
	/** The string used in comparison operator */
	private String stringLiteral;
	
	/** The comparison operator. Compares two strings */
	private IComparisonOperator comparisonOperator;

	/**
	 * Instantiates a new conditional expression.
	 *
	 * @param fieldGetter the field getter
	 * @param stringLiteral the string literal
	 * @param comparisonOperator the comparison operator
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Checks whether the given records satisfied the condition modeled by this object.
	 *
	 * @param record the record in question
	 * @return true, iff the records passes the condition.
	 */
	public boolean satisfied(StudentRecord record) {
		if (record == null) return false;
		return comparisonOperator.satisfied(fieldGetter.get(record), stringLiteral);
	}
	
	/**
	 * Gets the field getter.
	 *
	 * @return the field getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Gets the string literal.
	 *
	 * @return the string literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Gets the comparison operator.
	 *
	 * @return the comparison operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
}
