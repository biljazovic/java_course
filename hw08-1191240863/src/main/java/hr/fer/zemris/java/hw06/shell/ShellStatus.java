package hr.fer.zemris.java.hw06.shell;

/**
 * Enumeration containing possible values for return values of
 * {@link ShellCommand#executeCommand}.
 * 
 * @author Bruno Iljazović
 */
public enum ShellStatus {

	/**
	 * Suggests the shell to continue its runtime normally.
	 */
	CONTINUE,

	/**
	 * Suggests the shell to terminate.
	 */
	TERMINATE
}
