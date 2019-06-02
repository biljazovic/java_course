package hr.fer.zemris.java.gui.layouts;

/**
 * Exception in the {@link CalcLayout} object.
 * 
 * @author Bruno Iljazović
 */
public class CalcLayoutException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new layout exception.
	 */
	public CalcLayoutException() {
		super();
	}
	
	/**
	 * Instantiates a new layout exception.
	 *
	 * @param message the message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new  layout exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public CalcLayoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new layout exception.
	 *
	 * @param cause the cause
	 */
	public CalcLayoutException(Throwable cause) {
		super(cause);
	}
}
