package hr.fer.zemris.java.hw05.db;

/**
 * Contains several implementations of the {@link IFieldValueGetter} interface, that define 
 * which field is requested from a student records.
 * 
 * @author Bruno Iljazović
 */
public class FieldValueGetters {
	
	/** Returns the first name of the given record */
	public static final IFieldValueGetter FIRST_NAME;

	/** Returns the last name of the given record */
	public static final IFieldValueGetter LAST_NAME;

	/** Returns the JBMAG of the given record */
	public static final IFieldValueGetter JMBAG;
	
	static {
		FIRST_NAME = record -> record.getFirstName();
		
		LAST_NAME = record -> record.getLastName();
		
		JMBAG = record -> record.getJmbag();
	}

}
