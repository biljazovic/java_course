package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Localization provider which dynamically connects the given frame to the
 * localization provider.
 * 
 * @author Bruno Iljazović
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Instantiates a new localization provider.
	 *
	 * @param parent
	 *            the parent localization provider which handles the localization
	 * @param frame
	 *            the frame which is localized
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}

}
