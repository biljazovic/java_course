package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility methods for MyShell commands.
 * 
 * @author Bruno Iljazović
 */
public class ShellUtil {

	/**
	 * Returns the expanded message given as argument. It is expanded by adding the
	 * message to check the description of the command by calling 'help'.
	 *
	 * @param command
	 *            the command which is raising an error
	 * @param message
	 *            the original error message
	 * @return the expanded error message
	 */
	public static String errorMessage(ShellCommand command, String message) {
		return message + " Try 'help " + command.getCommandName() + "' for more information.";
	}
	
	/**
	 * Splits the given text of command arguments into the list of individual
	 * arguments. One argument is the sequence of non-space character, or, if the
	 * arguments is surrounded by the quotes, sequence of any characters.
	 *
	 * @param text
	 *            all command arguments
	 * @return the list of individual arguments
	 */
	public static List<String> splitArguments(String text) {

		boolean inQuotes = false;
		boolean inEscape = false;
		boolean expectWhitespace = false;
		
		List<String> result = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		
		for (char c : text.toCharArray()) {
			if (expectWhitespace) {
				if (!Character.isWhitespace(c)) {
					throw new IllegalArgumentException();
				}
				expectWhitespace = false;
			} else if (inQuotes) {
				if (inEscape) {
					if (c != '"' && c != '\\') {
						sb.append('\\');
					}
					sb.append(c);
					inEscape = false;
				} else if (c == '"') {
					result.add(sb.toString());
					sb.setLength(0);
					inQuotes = false;
					expectWhitespace = true;
				} else if (c == '\\') {
					inEscape = true;
				} else {
					sb.append(c);
				}
			} else {
				if (Character.isWhitespace(c)) {
					if (sb.length() > 0) {
						result.add(sb.toString());
						sb.setLength(0);
					}
				} else if (c == '"') {
					if (sb.length() > 0) {
						throw new IllegalArgumentException();
					}
					inQuotes = true;
				} else {
					sb.append(c);
				}
			}
		}
		
		if (inQuotes) {
			throw new IllegalArgumentException();
		}
		
		if (sb.length() > 0) {
			result.add(sb.toString());
		}
		
		return result;
	}
}
