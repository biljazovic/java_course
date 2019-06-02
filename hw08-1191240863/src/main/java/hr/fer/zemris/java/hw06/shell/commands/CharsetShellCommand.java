package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Charset command lists the available charsets' names on this machine. It takes
 * no arguments.
 * 
 * @author Bruno Iljazović
 */
public class CharsetShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "charset";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Charset command lists the available charsets' names on this machine",
			"It takes no arguments."
	));

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#executeCommand(hr.fer.zemris.java.hw06.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments.trim().length() > 0) {
			env.writeln(ShellUtil.errorMessage(this, "Too many arguments"));
			return ShellStatus.CONTINUE;
		}

		Charset.availableCharsets().keySet().forEach(env::writeln);
		
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
