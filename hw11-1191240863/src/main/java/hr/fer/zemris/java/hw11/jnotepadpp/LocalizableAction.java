package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Localizable action. It dynamically changes its name, description and mnemonic
 * key to match the current locale.
 * 
 * @author Bruno Iljazović
 */
public abstract class LocalizableAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new localizable action.
	 *
	 * @param lp
	 *            the localization provider
	 * @param key
	 *            the key for localization
	 * @param accelerator
	 *            the accelerator key, unchanged with localization
	 */
	public LocalizableAction(ILocalizationProvider lp, String key, String accelerator) {
		putValue(NAME, lp.getString(key + "Name"));
		
		String code = "VK_" + lp.getString(key + "Mnemonic");
		int keyEvent = getKeyEvent(code);
		
		putValue(MNEMONIC_KEY, keyEvent);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		putValue(SHORT_DESCRIPTION, lp.getString(key + "Description"));

		lp.addLocalizationListener(() -> {
			putValue(NAME, lp.getString(key + "Name"));
			putValue(MNEMONIC_KEY, getKeyEvent("VK_" + lp.getString(key + "Mnemonic")));
			putValue(SHORT_DESCRIPTION, lp.getString(key + "Description"));
		});
	}
	
	/**
	 * Gets the key event from the string.
	 *
	 * @param code the code
	 * @return the key event from the code
	 */
	int getKeyEvent(String code) {
		int keyEvent = 0;
		try {
			keyEvent = KeyEvent.class.getField(code).getInt(null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return keyEvent;
	}
	
	/**
	 * Instantiates a new localizable action with the given icon.
	 *
	 * @param lp the localization provider
	 * @param key the key for localization
	 * @param accelerator the accelerator key
	 * @param iconPath the icon path
	 */
	public LocalizableAction(ILocalizationProvider lp, String key, String accelerator, String iconPath) {
		this(lp, key, accelerator);
		putValue(LARGE_ICON_KEY, Util.createImageIcon(iconPath));
	}

}
