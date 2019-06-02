package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Data access object.
 * 
 * @author Bruno Iljazović
 */
public interface DAO {

	/**
	 * Retrieves the list of available polls.
	 *
	 * @return the list of available polls
	 */
	List<Poll> getPolls();

	/**
	 * Retrieves the list of poll options for a given poll id. If the given poll id
	 * doesn't match any poll, empty list is returned.
	 *
	 * @param pollId
	 *            the poll id
	 * @return the list of poll options for a given poll id
	 */
	List<PollOption> getOptions(long pollId);

	/**
	 * Retrieves the poll with the given poll id. If the given id doesn't match any
	 * poll, null is returned.
	 * 
	 * @param pollId
	 *            id of the wanted poll
	 * @return poll that matches the given id, or null if there is none
	 */
	Poll getPoll(long pollId);

	/**
	 * Processes the vote for the given option id. Returns the poll id that matches
	 * the given option, or null if the option id doesn't match any option.
	 *
	 * @param optionId
	 *            the option id
	 * @return poll id of the given option, or null if the option id doesn't match
	 *         any option
	 */
	Long vote(long optionId);
}