package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Completes the implementation of the {@link ILocalizationProvider} Default
 * language is English. This is a singleton class. Supports changing the current
 * language.
 * 
 * @author Bruno Iljazović
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/** The singleton object. */
	private static final LocalizationProvider instance = new LocalizationProvider();

	/** The current language. */
	private String language;
	
	/** The current resource bundle. */
	private ResourceBundle bundle;
	
	/**
	 * Instantiates a new localization provider, with language set to English
	 */
	private LocalizationProvider() {
		setLanguage("en");
	}
	
	/**
	 * Gets the instance of this class
	 *
	 * @return the instance of this class
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	/**
	 * Sets the language.
	 *
	 * @param language
	 *            the new language
	 */
	public void setLanguage(String language) {
		if (language.equals(this.language)) return;
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.prijevodi",
				locale
		);
		JOptionPane.setDefaultLocale(locale);
		JFileChooser.setDefaultLocale(locale);
		fire();
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}
}
