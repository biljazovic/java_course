package hr.fer.zemris.java.hw06.demo4;

/**
 * Represents one student record - 7 read-only properties.
 * 
 * @author Bruno Iljazović
 */
public class StudentRecord {
	
	/** The number that uniquely identifies a student. */
	private String jmbag;
	
	/** The last name. */
	private String lastName;
	
	/** The first name. */
	private String firstName;
	
	/** The mid-term points. */
	private double midtermPoints;
	
	/** The final exam points. */
	private double finalPoints;
	
	/** The points from laboratory exercises. */
	private double labPoints;

	/** The final grade. */
	private int finalGrade;
	
	/**
	 * Instantiates a new student record.
	 *
	 * @param jmbag the jmbag
	 * @param lastName the last name
	 * @param firstName the first name
	 * @param midtermPoints the mid-term points
	 * @param finalPoints the final exam points
	 * @param labPoints points from the laboratory exercises
	 * @param finalGrade the final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, double midtermPoints,
			double finalPoints, double labPoints, int finalGrade) {
		super();
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midtermPoints = midtermPoints;
		this.finalPoints = finalPoints;
		this.labPoints = labPoints;
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
	 * Gets the mid-term points.
	 *
	 * @return the mid-term points
	 */
	public double getMidtermPoints() {
		return midtermPoints;
	}

	/**
	 * Gets the final exam points.
	 *
	 * @return the final points
	 */
	public double getFinalPoints() {
		return finalPoints;
	}

	/**
	 * Gets the points from laboratory exercises
	 *
	 * @return the points from laboratory exercises
	 */
	public double getLabPoints() {
		return labPoints;
	}

	/**
	 * Gets the final grade.
	 *
	 * @return the final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Gets the total points.
	 *
	 * @return the total points
	 */
	public double getTotalPoints() {
		return midtermPoints + finalPoints + labPoints;
	}
	
}
