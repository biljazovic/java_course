package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.dao.jpa.JPADAOImpl;

/**
 * Singleton class that provides the Data Access Object.
 * 
 * @author Bruno Iljazović
 */
public class DAOProvider {

	/** The DAO. */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Gets the DAO
	 *
	 * @return the DAO
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}