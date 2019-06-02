package hr.fer.zemris.java.hw14.model;

/**
 * Represents one poll. Each poll has an unique id.
 * 
 * @author Bruno Iljazović
 */
public class Poll {
	
	/** The id. */
	private long id;
	
	/** The title of the poll. */
	private String title;
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new poll.
	 *
	 * @param id
	 *            the id
	 * @param title
	 *            the title of the poll
	 * @param message
	 *            the message
	 */
	public Poll(long id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
