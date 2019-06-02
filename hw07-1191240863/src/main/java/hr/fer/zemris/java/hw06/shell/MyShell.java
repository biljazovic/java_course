package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Simple shell program. Provides support for multi-line commands - if the user
 * inputs the morelines sybol - found by running 'symbol MORELINES' - at the end
 * of the line.
 * <p>
 * Available commands are defined in the package
 * {@link hr.fer.zemris.java.hw06.shell.commands}.
 * 
 * @author Bruno Iljazović
 */
public class MyShell {

	/**
	 * This method is called when the program is run.
	 *
	 * @param args the command line arguments, not used here.
	 */
	public static void main(String[] args) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		
		System.out.println("Welcome to MyShell v 1.0");

		Environment env = new EnvironmentImpl(reader, writer);
		
		ShellStatus status;
		
		try {
			do {
				String line = readLines(env);
				String[] lineArray = line.trim().split("\\s+", 2);

				String commandName = lineArray[0];
				String commandArguments = (lineArray.length > 1) ? lineArray[1] : "";
				
				ShellCommand command = env.commands().get(commandName);

				if (command == null) {
					env.writeln("Unknown command '" + commandName + "'.");
					status = ShellStatus.CONTINUE;
				} else {
					status = command.executeCommand(env, commandArguments);
				}
			} while (status != ShellStatus.TERMINATE);
		} catch(ShellIOException ex) {
			
		}
		
		try {
			reader.close();
			writer.close();
		} catch(IOException ignorable) { }
		
	}
	
	/**
	 * Reads a command from the given environment. If the command spans multiple lines,
	 * they are concatenated to one string.
	 *
	 * @param env
	 *            environment through which the command is read.
	 * @return the command string
	 */
	private static String readLines(Environment env) {
		env.write(env.getPromptSymbol() + " ");
		
		boolean moreLines = false;
		
		StringBuilder result = new StringBuilder();
		
		do {
			String line = env.readLine();
			if (line.endsWith(env.getMorelinesSymbol().toString())) {
				moreLines = true;
				line = line.substring(0, line.length() - 1);
				env.write(env.getMultilineSymbol() + " ");
				//System.out.print(env.getMultilineSymbol() + " ");
			} else {
				moreLines = false;
			}
			result.append(line);
		} while (moreLines);
		
		return result.toString();
	}
}
