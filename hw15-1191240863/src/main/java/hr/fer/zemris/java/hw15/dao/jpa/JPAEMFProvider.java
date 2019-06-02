package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider of the entity manager factory
 *
 * @author Bruno Iljazović
 */
public class JPAEMFProvider {

	/** The entity factor.. */
	public static EntityManagerFactory emf;
	
	/**
	 * Gets the emf.
	 *
	 * @return the emf
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Sets the emf.
	 *
	 * @param emf the new emf
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}