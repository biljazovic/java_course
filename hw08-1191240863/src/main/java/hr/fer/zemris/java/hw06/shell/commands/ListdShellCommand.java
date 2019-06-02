package hr.fer.zemris.java.hw06.shell.commands;

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
 * Listd command outputs the paths from the 'cdstack', each in new line. It
 * takes no arguments.
 * 
 * @author Bruno Iljazović
 */
public class ListdShellCommand implements ShellCommand {
	
	/** The command name. */
	private  static final String commandName = "listd";
	
	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Listd command outputs the paths from the 'cdstack', each in new line.",
			"It takes no arguments."
	));

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
		
		Path[] stackArray = stack.toArray(new Path[stack.size()]);
		
		for (int i = stackArray.length - 1; i >= 0; --i) {
			env.writeln(stackArray[i].toString());
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

}
