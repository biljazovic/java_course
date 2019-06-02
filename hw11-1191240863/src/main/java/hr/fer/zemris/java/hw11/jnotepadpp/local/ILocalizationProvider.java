package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Localization provider. Provides method for getting the string translated into
 * the current language, and manages the listeners for localization change.
 * 
 * @author Bruno Iljazović
 */
public interface ILocalizationProvider {

	/**
	 * Adds the localization listener.
	 *
	 * @param listener the listener to add
	 */
	void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Removes the localization listener.
	 *
	 * @param listener the listener to remove
	 */
	void removeLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Gets the string in the current language.
	 *
	 * @param key the key for localization
	 * @return the localized string
	 */
	String getString(String key);
	
	/**
	 * Gets the current language.
	 *
	 * @return the current language
	 */
	String getCurrentLanguage();
}
