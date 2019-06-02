package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Provides the method for executing command and getting command name and
 * description.
 * 
 * @author Bruno Iljazović
 */
public interface ShellCommand {

	/**
	 * Executes this command interacting with the shell through the given
	 * environment and given command arguments, all concatenated in one string.
	 * 
	 * @param env
	 *            environment for interacting with the shell
	 * @param arguments
	 *            command arguments
	 * @return if the shell should terminate or not
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Returns the command name.
	 * 
	 * @return command name
	 */
	String getCommandName();

	/**
	 * Returns the unmodifiable list containing the command description.
	 * 
	 * @return the unmodifiable list containing the command description.
	 */
	List<String> getCommandDescription();
}
