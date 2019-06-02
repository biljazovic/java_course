package hr.fer.zemris.java.hw05.db;

/**
 * Defines which field is to be requested from the given student record.
 * 
 * @author Bruno Iljazović
 */
public interface IFieldValueGetter {

	/**
	 * Returns the requested field from the given student record.
	 * @param record record in question
	 * @return the requested field of the record
	 */
	public String get(StudentRecord record);
}
