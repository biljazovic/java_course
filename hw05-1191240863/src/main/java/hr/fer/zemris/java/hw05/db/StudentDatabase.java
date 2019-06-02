package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.collections.SimpleHashtable;

/**
 * This class gets list of Strings which represent database file and then creates an internal list
 * of student records.
 * <p> It also creates an index for fast retrieval of student records by jmbag.
 * 
 * @author Bruno Iljazović
 */
public class StudentDatabase {
	
	/** The list of student records */
	private List<StudentRecord> records;
	
	/** Records hashed by JMBAG 
	 * @see SimpleHashtable */
	private SimpleHashtable<String, StudentRecord> recordsByJMBAG;
	
	/** Minimum finalGrade allowed */
	private final int MIN_GRADE = 1;
	
	/** Maximum finalGrade allowed */
	private final int MAX_GRADE = 5;
	
	/**
	 * Instantiates a new student database from the given database file rows.
	 * 
	 * <p> List can contain empty rows, they are ignored.
	 *
	 * @param lines list of database file rows
	 * 
	 * @throws IllegalArgumentException if the database file was invalid.
	 */
	public StudentDatabase(List<String> lines) {
		records = new ArrayList<StudentRecord>();

		recordsByJMBAG = new SimpleHashtable<String, StudentRecord>();

		for (String line : lines) {
			if (line.equals("")) continue;

			String[] array = line.split("\\s+");

			if (!array[0].matches("\\d{10}")) {
				throw new IllegalArgumentException("Invalid jmbag. Was " + array[0] + ".");
			}
			
			if (array.length < 4) {
				throw new IllegalArgumentException("Each line must contain at least 4 elements");
			}
			
			StringBuilder lastName = new StringBuilder();
			for (int i = 1; i < array.length - 2; ++i) {
				if (lastName.length() != 0) {
					lastName.append(" ");
				}
				lastName.append(array[i]);
			}
			
			Integer grade;
			try {
				grade = Integer.parseInt(array[array.length - 1]);
				if (grade < MIN_GRADE || grade > MAX_GRADE) throw new NumberFormatException();
			} catch(NumberFormatException ex) {
				throw new IllegalArgumentException("Last element in line must be integer "
						+ "in range from " + MIN_GRADE + " to " + MAX_GRADE + ".");
			}
			
			StudentRecord record = new StudentRecord(
					array[0],
					lastName.toString(),
					array[array.length - 2],
					grade);
			
			records.add(record);
			recordsByJMBAG.put(array[0], record);
		}
	}
	
	/**
	 * Return sub-list of those student records that the given filter accepts.
	 *
	 * @param filter the filter
	 * @return the sub-list of accepted records
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> result = new ArrayList<StudentRecord>();
		
		for (StudentRecord record : records) {
			if (filter.accepts(record)) {
				result.add(record);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the student record that matches given jmbag. Uses the Hashtable, so this method's
	 * complexity is O(1).
	 *
	 * @param jmbag the jmbag
	 * @return the student record that matches given jmbag
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return recordsByJMBAG.get(jmbag);
	}
	
	
}
