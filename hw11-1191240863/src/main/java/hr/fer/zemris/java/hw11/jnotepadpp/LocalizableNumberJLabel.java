package hr.fer.zemris.java.hw11.jnotepadpp;

import java.text.MessageFormat;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Localizable label which also stores one number and has a setter for it.
 * Dynamically changes the text matching the current locale.
 * <p>Used for status bar, for example, "length: xx"
 * 
 * @author Bruno Iljazović
 */
public class LocalizableNumberJLabel extends JLabel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The key for localizaton. */
	private String key;
	
	/** The stored number. */
	private int number;
	
	/** The localization provider. */
	ILocalizationProvider lp;
	
	/**
	 * Instantiates a new localizable label.
	 *
	 * @param lp
	 *            the localization provider
	 * @param key
	 *            the key for localization
	 * @param number
	 *            the stored number
	 */
	public LocalizableNumberJLabel(ILocalizationProvider lp, String key, int number) {
		this.key = key;
		this.number = number;
		this.lp = lp;
		setHorizontalAlignment(SwingConstants.LEFT);
		updateText();
		lp.addLocalizationListener(() -> {
			updateText();
		});
	}
	
	/**
	 * Updates the text of the label.
	 */
	private void updateText() {
		setText(MessageFormat.format(
				lp.getString(key), 
				number
		));
	}
	
	/**
	 * Sets the stored number to the given one and updates the label.
	 *
	 * @param number
	 *            the new number
	 */
	public void setNumber(int number) {
		this.number = number;
		updateText();
	}
}
