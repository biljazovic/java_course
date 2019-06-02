package hr.fer.zemris.java.hw05.db;

/**
 * Filters the StudentRecords.
 * 
 * @author Bruno Iljazović
 */
public interface IFilter {

	/**
	 * Returns true if the records is accepted by the filter, false otherwise.
	 * @param record in question
	 * @return true iff the record is accepted by the filter
	 */
	public boolean accepts(StudentRecord record);
}
