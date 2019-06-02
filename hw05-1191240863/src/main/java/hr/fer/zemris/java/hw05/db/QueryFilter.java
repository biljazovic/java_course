package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Filters the given student records by choosing only those that pass all of the given 
 * conditions.
 * 
 * @see ConditionalExpression
 * 
 * @author Bruno Iljazović
 */
public class QueryFilter implements IFilter {
	
	/** The conditions. */
	private List<ConditionalExpression> conditions;
	
	/**
	 * Instantiates a new query filter with the given list of conditions.
	 *
	 * @param conditions the conditions
	 */
	public QueryFilter(List<ConditionalExpression> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Checks whether the given student record passes ALL of the conditions.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression condition : conditions) {
			if (!condition.satisfied(record)) return false;
		}
		return true;
	}

}
