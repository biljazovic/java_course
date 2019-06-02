package hr.fer.zemris.java.hw05.db;

/**
 * Represents one student record.
 * 
 * @author Bruno Iljazović
 */
public class StudentRecord {

	/** The jmbag. Contains 10 digits */
	private String jmbag;
	
	/** The last name. */
	private String lastName;
	
	/** The first name. */
	private String firstName;
	
	/** The final grade. */
	private Integer finalGrade;

	/**
	 * Instantiates a new student record.
	 *
	 * @param jmbag the jmbag
	 * @param lastName the last name
	 * @param firstName the first name
	 * @param finalGrade the final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, Integer finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Gets the jmbag.
	 *
	 * @return the jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the final grade.
	 *
	 * @return the final grade
	 */
	public Integer getFinalGrade() {
		return finalGrade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	/**
	 * Checks whether this object is equal to the given one based on jmbag.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}
	
}
