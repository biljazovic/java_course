package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Command cat outputs the given file's content using the provided charset to
 * interpret chars from bytes. It takes one or two arguments: first argument is
 * the file path and it's mandatory, and second argument is charset name which,
 * if not provided, defaults to the system default. List of available charsets
 * can be found by running 'charset' command. *
 * 
 * @author Bruno Iljazović
 */
public class CatShellCommand implements ShellCommand {

	/** The command name. */
	private static final String commandName = "cat";
	
	/** buffer size */
	private static final int BUFF_SIZE = 4 * 1024;
	
	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Command cat outputs the given file's content using the provided charset to interpret "
			+ "chars from bytes.",
			"It takes one or two arguments: first argument is the file path and it's mandatory, "
			+ "and second argument is charset name which, if not provided, defaults to "
			+ Charset.defaultCharset().toString() + ".",
			"List of available charsets can be found by running 'charset' command."
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
		
		Charset charset = Charset.defaultCharset();
		
		if (argumentList.size() == 2) {
			try {
			     charset = Charset.forName(argumentList.get(1));
			} catch(IllegalArgumentException ex) {
				env.writeln(ShellUtil.errorMessage(this, ex.toString()));
				return ShellStatus.CONTINUE;
			}
		} else if (argumentList.size() != 1) {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
			return ShellStatus.CONTINUE;
		}
		
		try (
			BufferedReader reader = Files.newBufferedReader(
					env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0))),
					charset
			);
		) {
			char[] buff = new char[BUFF_SIZE];
			int readChars;
			while ((readChars = reader.read(buff)) > 0) {
				env.write((new String(buff)).substring(0, readChars));
			}
		} catch(IOException | InvalidPathException ex) {
			env.writeln("There was a problem reading the file: " + ex);
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
