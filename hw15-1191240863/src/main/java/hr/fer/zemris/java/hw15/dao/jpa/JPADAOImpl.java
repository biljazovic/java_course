package hr.fer.zemris.java.hw15.dao.jpa;

import java.util.List;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Implementation of the DAO interface.
 * 
 * @author Bruno Iljazović
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		try {
			BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
			return blogEntry;
		} catch(Exception ex) {
			throw new DAOException("Error occurred while getting blog entry.", ex);
		}
	}

	@Override
	public List<BlogUser> getListOfBlogUsers() throws DAOException {
		try {
			return JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getAllUsers", BlogUser.class)
						.getResultList();
		} catch(Exception ex) {
			throw new DAOException("Error occurred while getting blog users.", ex);
		}
	}

	@Override
	public BlogUser getBlogUser(String nick) throws DAOException {
		List<BlogUser> users = null;
		try {
			users = (List<BlogUser>) 
					JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getUserByNick", BlogUser.class) 
						.setParameter("nick", nick) 
						.getResultList();
		} catch(Exception ex) {
			throw new DAOException("Error occurred while getting blog user.", ex);
		}
		if (users == null || users.isEmpty()) return null;
		return users.get(0);
	}

	@Override
	public void addBlogUser(BlogUser user) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(user);
		} catch(Exception ex) {
			throw new DAOException("Error occurred while adding blog user.", ex);
		}
	}
}