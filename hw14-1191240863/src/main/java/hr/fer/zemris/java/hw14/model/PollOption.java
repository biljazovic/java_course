package hr.fer.zemris.java.hw14.model;

import java.util.Comparator;

/**
 * Represents one poll option.
 * 
 * @author Bruno Iljazović
 */
public class PollOption {
	
	/** Compares by ids ascending. */
	public static Comparator<PollOption> COMPARATOR_BY_ID = (e, f) -> Long.compare(e.id, f.id);

	/** Compares by votes count descending. */
	public static Comparator<PollOption> COMPARATOR_BY_VOTES = (e, f) -> Long.compare(f.votesCount, e.votesCount);
	
	/** The id. */
	private long id;
	
	/** The title. */
	private String title;
	
	/** The link. */
	private String link;
	
	/** The poll id. */
	private long pollId;
	
	/** Number of votes. */
	private long votesCount;

	/**
	 * Instantiates a new poll option.
	 *
	 * @param id
	 *            the id
	 * @param title
	 *            the title
	 * @param link
	 *            the link
	 * @param pollId
	 *            the poll id
	 * @param votesCount
	 *            number of votes
	 */
	public PollOption(long id, String title, String link, long pollId, long votesCount) {
		super();
		this.id = id;
		this.title = title;
		this.link = link;
		this.pollId = pollId;
		this.votesCount = votesCount;
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
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Gets the poll id.
	 *
	 * @return the poll id
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * Gets the votes count.
	 *
	 * @return the votes count
	 */
	public long getVotesCount() {
		return votesCount;
	}
}
