package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements some methods of {@link ILocalizationProvider}, namely, for
 * listener management.
 * 
 * @author Bruno Iljazović
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	/** The listeners. */
	private List<ILocalizationListener> listeners = new ArrayList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if (listeners.contains(Objects.requireNonNull(listener))) return;
		listeners.add(listener);
	}
	
	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Notifies the listeners of language change.
	 */
	void fire() {
		listeners.forEach(ILocalizationListener::localizationChanged);
	}
}
