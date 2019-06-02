package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Interface through which the shell command interacts with the shell.
 * 
 * @author Bruno Iljazović
 */
public interface Environment {

	/**
	 * Reads one line from console.
	 *
	 * @return the read line.
	 * @throws ShellIOException
	 *             the shell IO exception
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes a string to the console.
	 *
	 * @param text
	 *            the text to be written
	 * @throws ShellIOException
	 *             the shell IO exception
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes a string to the console with the end line at the end.
	 *
	 * @param text
	 *            the text to be written
	 * @throws ShellIOException
	 *             the shell IO exception
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Returns an unmodifiable sorted map where key is the name of the command and
	 * the value is the instance of ShellCommand interface.
	 *
	 * @return sorted map of (commandName, command) pairs
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Gets the multiline symbol.
	 *
	 * @return the multiline symbol
	 */
	Character getMultilineSymbol();

	/**
	 * Sets the multiline symbol.
	 *
	 * @param symbol
	 *            the new multiline symbol
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Gets the prompt symbol.
	 *
	 * @return the prompt symbol
	 */
	Character getPromptSymbol();

	/**
	 * Sets the prompt symbol.
	 *
	 * @param symbol
	 *            the new prompt symbol
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Gets the morelines symbol.
	 *
	 * @return the morelines symbol
	 */
	Character getMorelinesSymbol();

	/**
	 * Sets the morelines symbol.
	 *
	 * @param symbol
	 *            the new morelines symbol
	 */
	void setMorelinesSymbol(Character symbol);

	/**
	 * Returns the absolute normalized path of the current working directory.
	 * 
	 * @return absolute normalized path of the current working directory
	 */
	Path getCurrentDirectory();

	/**
	 * Sets the current working directory to the given one, if it exists.
	 * 
	 * @param path
	 *            new directory path
	 * @throws IllegalArgumentException
	 *             if the directory doesn't exist
	 */
	void setCurrentDirectory(Path path);

	/**
	 * Returns the data to which the given key is mapped to, or null if key doesn't
	 * map to anything
	 * 
	 * @param key
	 *            key of the wanted data
	 * @return data the given key is mapped to
	 */
	Object getSharedData(String key);

	/**
	 * Associates the given value(data) with the given key. If there was some data
	 * already mapped to the given key, it is replaced with the new one.
	 * 
	 * @param key
	 *            the key of the data
	 * @param value
	 *            the data
	 */
	void setSharedData(String key, Object value);
}
