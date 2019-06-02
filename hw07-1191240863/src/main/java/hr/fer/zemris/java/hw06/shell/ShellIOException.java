package hr.fer.zemris.java.hw06.shell;

/**
 * Signals that an I/O exception of some sort has occurred in interacting with
 * the shell.
 * 
 * @author Bruno Iljazović
 */
public class ShellIOException extends RuntimeException {
	
	/**
	 * serialVersionUID
	 */
	static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new shell IO exception.
	 */
	public ShellIOException() {
	}

	/**
	 * Instantiates a new shell IO exception with the given message.
	 *
	 * @param message
	 *            the message
	 */
	public ShellIOException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new shell IO exception with the given cause.
	 *
	 * @param cause
	 *            the cause
	 */
	public ShellIOException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new shell IO exception with the given message and cause.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ShellIOException(String message, Throwable cause) {
		super(message, cause);
	}
}
