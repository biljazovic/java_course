/*
 * 
 */
package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Popd command removes the top element of the 'cdstack' and changes the current
 * directory to the removed one. If the removed directory does not exist,
 * current directory remains unchanged. If the 'cdstack' was empty, it outputs
 * an error. It takes no arguments
 * 
 * @author Bruno Iljazović
 */
public class PopdShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "popd";
	
	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Popd command removes the top element of the 'cdstack' and changes the current "
			+ "directory to the removed one.",
			"If the removed directory does not exist, current directory remains unchanged.",
			"If the 'cdstack' was empty, it outputs an error.",
			"It takes no arguments"
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
		
		if (argumentList.size() > 0) {
			env.writeln(ShellUtil.errorMessage(this, "Too many arguments."));
			return ShellStatus.CONTINUE;
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
		
		if (stack == null || stack.size() == 0) {
			env.writeln(ShellUtil.errorMessage(this, "No stored directories. "));
			return ShellStatus.CONTINUE;
		}
		
		Path path = stack.pop();
		if (!Files.isDirectory(path)) {
			env.writeln(ShellUtil.errorMessage(this, "Directory from stack no longer exists."));
			return ShellStatus.CONTINUE;
		} 
		
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
