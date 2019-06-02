package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Listens for the change in current language.
 * 
 * @author Bruno Iljazović
 */
public interface ILocalizationListener {
	
	/**
	 * Invoked when the current langeuage changes.
	 */
	void localizationChanged();
}
