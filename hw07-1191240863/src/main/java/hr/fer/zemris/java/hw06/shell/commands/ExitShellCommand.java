package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * The exit command will cause the shell to exit. Passing any number of
 * arguments doesn't change its behavior.
 * 
 * @author Bruno Iljazović
 */
public class ExitShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "exit";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"The exit command will cause the shell to exit.",
			"Passing any number of arguments doesn't change its behaviour."
	));

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#executeCommand(hr.fer.zemris.java.hw06.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return commandName;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#getCommandDescription()
	 */
	@Override
	public List<String> getCommandDescription() {
		return description;
	}
}
