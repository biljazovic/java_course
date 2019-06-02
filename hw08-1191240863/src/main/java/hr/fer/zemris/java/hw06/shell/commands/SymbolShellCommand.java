package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Symbol command can be used to output or to change prompt symbol, more lines
 * symbol, and multiline symbol. If it is called with one argument, 'PROMPT',
 * 'MULTILINE', or 'MORELINES', it outputs the symbol currently in use. If it is
 * called with two arguments, it will set the corresponding symbol to the second
 * argument. 
 * 
 * @author Bruno Iljazović
 */
public class SymbolShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "symbol";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Symbol command can be used to output or to change prompt symbol, more lines symbol, "
			+ "and multiline symbol.",
			"If it is called with one argument, 'PROMPT', 'MULTILINE', or 'MORELINES', it outputs"
			+ " the symbol currently in use.",
			"If it is called with two arguments, it will set the corresponding symbol to the "
			+ "second argument."
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
		
		if (argumentList.size() == 1) {
			switch (argumentList.get(0)) {
			case "PROMPT":
				env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'.");
				break;
			case "MORELINES":
				env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'.");
				break;
			case "MULTILINE":
				env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'.");
				break;
			default:
				env.writeln(ShellUtil.errorMessage(this, "Invalid argument."));
			}
		} else if (argumentList.size() == 2) {
			if (argumentList.get(1).length() != 1) {
				env.writeln(ShellUtil.errorMessage(this, "Invalid symbol."));
			}
			Character newSymbol;
			switch (argumentList.get(0)) {
			case "PROMPT":
				newSymbol = argumentList.get(1).charAt(0);
				env.writeln("Symbol for PROMPT changed from '" + env.getPromptSymbol() + 
						"' to '" + newSymbol + "'.");
				env.setPromptSymbol(newSymbol);
				break;
			case "MORELINES":
				newSymbol = argumentList.get(1).charAt(0);
				env.writeln("Symbol for MORELINES changed from '" + env.getMorelinesSymbol() + 
						"' to '" + newSymbol + "'.");
				env.setMorelinesSymbol(newSymbol);
				break;
			case "MULTILINE":
				newSymbol = argumentList.get(1).charAt(0);
				env.writeln("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + 
						"' to '" + newSymbol + "'.");
				env.setMultilineSymbol(argumentList.get(1).charAt(0));
				break;
			default:
				env.writeln(ShellUtil.errorMessage(this, "Invalid argument."));
			}
		} else {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
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
