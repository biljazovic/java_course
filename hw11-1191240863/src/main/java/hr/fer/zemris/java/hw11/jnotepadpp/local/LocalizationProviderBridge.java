package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A decorator for a localization provider. It manages a connection status to the provider through
 * connect() and disconnect() methods.
 * 
 * @author Bruno Iljazović
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider{
	
	/** The connected flag. */
	private boolean connected;
	
	/** decorated localization provided */
	ILocalizationProvider parent;
	
	/** The listener. */
	ILocalizationListener listener;

	/**
	 * Instantiates a new localization provider bridge.
	 *
	 * @param parent the localization provider.
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		listener = new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				fire();
			}
		};
	}
	
	/**
	 * Disconnects from the localization provider.
	 */
	public void disconnect() {
		if (!connected) return;
		parent.removeLocalizationListener(listener);
		connected = false;
	}
	
	/**
	 * Connects to the localization provider.
	 */
	public void connect() {
		if (connected) return;
		parent.addLocalizationListener(listener);
		connected = true;
	}
	
	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return parent.getCurrentLanguage();
	}

}
