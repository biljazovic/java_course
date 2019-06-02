package hr.fer.zemris.java.hw06.observer2;

/**
 * The 'Observer' interface in the observer design pattern. Subject class calls
 * the {@link IntegerStorageObserver#valueChanged} when the observed value is
 * changed.
 * 
 * @author Bruno Iljazović
 */
public interface IntegerStorageObserver {
	
	/**
	 * Called from the Subject class when the observed value is changed.
	 * @param istorageChange contains the new value and the old one
	 */
	public void valueChanged(IntegerStorageChange istorageChange);
}
