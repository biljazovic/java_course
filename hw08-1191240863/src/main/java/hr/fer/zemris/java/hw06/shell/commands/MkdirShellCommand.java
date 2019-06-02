package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * The mkdir command creates the directory, if it does not already exist. It
 * will try to create all directories leading up to the given one that do not
 * already exist. If this command failed, it still may have succeded in creating
 * some of the parent directories It takes exactly one argument: path to the
 * directory.
 * 
 * @author Bruno Iljazović
 */
public class MkdirShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "mkdir";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"The mkdir command creates the directory, if it does not already exist.",
			"It will try to create all directories leading up to the given one that do not already"
			+ " exist.",
			"If this command failed, it still may have succeded in creating some of the parent "
			+ "directories",
			"It takes exactly one argument: path to the directory."
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
			env.writeln(ShellUtil.errorMessage(this, "Invalid argument(s)."));
			return ShellStatus.CONTINUE;
		}
		
		if (argumentList.size() != 1) {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
			return ShellStatus.CONTINUE;
		}
		
		File dir = env.getCurrentDirectory().resolve(argumentList.get(0)).toFile();
		
		if (dir.exists()) {
			env.writeln("Directory " + argumentList.get(0) + " already exists.");
			return ShellStatus.CONTINUE;
		}
		
		if (!dir.mkdirs()) {
			env.writeln("Cannot create directory " + argumentList.get(0) + ".");
		}
		
		env.writeln("Successfully created directory " + argumentList.get(0));
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
