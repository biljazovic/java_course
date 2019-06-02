package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * If the help command is called with no arguments, it lists available commands.
 * If it's called with one argument - the command name, it outputs the
 * description of the command.
 * 
 * @author Bruno Iljazović
 */
public class HelpShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "help";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"If the help command is called with no arguments, it lists available commands.",
			"If it's called with one argument - the command name, it outputs the description of "
			+ "the command."
	));

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#executeCommand(hr.fer.zemris.java.hw06.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argumentList = null;
		
		try {
			argumentList = ShellUtil.splitArguments(arguments);
		} catch(IllegalArgumentException ex) {
			env.writeln("Invalid argument(s).");
			return ShellStatus.CONTINUE;
		}
		
		if (argumentList.size() == 0) {
			env.writeln("List of availabe commands:");
			env.commands().keySet().forEach(env::writeln);
		} else if (argumentList.size() == 1) {
			ShellCommand command = env.commands().get(argumentList.get(0));
			if (command == null) {
				env.writeln("Unknown command '" + argumentList.get(0) + "'.");
			} else {
				command.getCommandDescription().forEach(env::writeln);
			}
		} else {
			env.writeln(ShellUtil.errorMessage(this, "Too many arguments."));
		}
		
		return ShellStatus.CONTINUE;
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
