package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Pushd command pushes the current directory on the 'cdstack' and then changes the
 * working directory to the given one. It takes exactly one argument: valid
 * directory path. If the given directory does not exist, neither current
 * directory, nor the stack are changed.
 * 
 * @author Bruno Iljazović
 */
public class PushdShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "pushd";
	
	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Pushd command pushes the current directory on the 'cdstack' and then changes the "
			+ "working directory to the given one.",
			"It takes exactly one argument: valid directory path.",
			"If the given directory does not exist, neither current directory, nor the stack are "
			+ "changed."
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
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0)));

		if (!Files.isDirectory(path)) {
			env.writeln(ShellUtil.errorMessage(this, "Given directory does not exist."));
			return ShellStatus.CONTINUE;
		} 
		
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
		
		if (stack == null) {
			stack = new Stack<>();
			env.setSharedData("cdstack", stack);
		}
		
		stack.push(env.getCurrentDirectory());
		
		env.setCurrentDirectory(path);
		
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
