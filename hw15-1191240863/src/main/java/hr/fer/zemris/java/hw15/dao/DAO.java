package hr.fer.zemris.java.hw15.dao;

import java.util.List;

import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * The Data Access Object.
 */
public interface DAO {

	/**
	 * Gets the blog entry with the given id. If such entry doesn't exist, returns null.
	 * @param id id of the wanted blog entry
	 * @return the blog entry with the given id
	 * @throws DAOException if something went wrong while getting the entry
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Gets the list of blog users.
	 *
	 * @return the list of blog users
	 * @throws DAOException if something went wrong while getting the entry
	 */
	List<BlogUser> getListOfBlogUsers() throws DAOException;
	
	/**
	 * Gets the blog user with the given nickname, or null if such user doesn't exist.
	 *
	 * @param nick the nick
	 * @return the blog user
	 * @throws DAOException if something went wrong while getting the entry
	 */
	BlogUser getBlogUser(String nick) throws DAOException;
	
	/**
	 * Adds new blog user.
	 *
	 * @param user the user
	 * @throws DAOException if something went wrong while adding the user.
	 */
	void addBlogUser(BlogUser user) throws DAOException;
}