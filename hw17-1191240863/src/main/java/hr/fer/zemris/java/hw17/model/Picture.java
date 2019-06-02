package hr.fer.zemris.java.hw17.model;

/**
 * Each object of this class contains information about one picture.
 * 
 * @author Bruno Iljazović
 */
public class Picture {
	
	/** The file name of the picture. */
	private String fileName;
	
	/** The description. */
	private String description;
	
	/** The array of tags from this picture. */
	private String[] tags;

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Instantiates a new picture.
	 *
	 * @param fileName the file name
	 * @param description the description
	 * @param tags the tags
	 */
	public Picture(String fileName, String description, String ... tags) {
		this.fileName = fileName;
		this.description = description;
		this.tags = tags;
	}
}
