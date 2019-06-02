package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CptreeShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.RmtreeShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * Implementation of the {@link Environment} interface. Available commands:
 * exit, ls, mkdir, copy, symbol, cat, hexdump, tree, help, charset.
 * 
 * @author Bruno Iljazović
 */
public class EnvironmentImpl implements Environment {

	/**
	 * Instantiates a new environment;
	 *
	 * @param reader
	 *            the reader for reading from the console
	 * @param writer
	 *            the writer for writing to the console
	 */
	public EnvironmentImpl(BufferedReader reader, BufferedWriter writer) {

		promptSymbol = '>';
		moreLinesSymbol = '\\';
		multilineSymbol = '|';

		this.reader = reader;
		this.writer = writer;

		commands = new TreeMap<>();

		commands.put("exit", new ExitShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("charset", new CharsetShellCommand());
		commands.put("pwd", new PwdShellCommand());
		commands.put("cd", new CdShellCommand());
		commands.put("pushd", new PushdShellCommand());
		commands.put("dropd", new DropdShellCommand());
		commands.put("popd", new PopdShellCommand());
		commands.put("listd", new ListdShellCommand());
		commands.put("rmtree", new RmtreeShellCommand());
		commands.put("cptree", new CptreeShellCommand());
		commands.put("massrename", new MassrenameShellCommand());

		setCurrentDirectory(Paths.get("."));

		sharedData = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#readLine()
	 */
	@Override
	public String readLine() throws ShellIOException {
		try {
			return reader.readLine();
		} catch (IOException ex) {
			throw new ShellIOException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#write(java.lang.String)
	 */
	@Override
	public void write(String text) throws ShellIOException {
		try {
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {
			throw new ShellIOException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#writeln(java.lang.String)
	 */
	@Override
	public void writeln(String text) throws ShellIOException {
		try {
			writer.write(text + "\n");
			writer.flush();
		} catch (IOException ex) {
			throw new ShellIOException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#commands()
	 */
	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#getMultilineSymbol()
	 */
	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#setMultilineSymbol(java.lang.
	 * Character)
	 */
	@Override
	public void setMultilineSymbol(Character symbol) {
		multilineSymbol = symbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#getPromptSymbol()
	 */
	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#setPromptSymbol(java.lang.
	 * Character)
	 */
	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = symbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#getMorelinesSymbol()
	 */
	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw06.shell.Environment#setMorelinesSymbol(java.lang.
	 * Character)
	 */
	@Override
	public void setMorelinesSymbol(Character symbol) {
		moreLinesSymbol = symbol;
	}

	@Override
	public Path getCurrentDirectory() {
		return currentDirectory;
	}

	@Override
	public void setCurrentDirectory(Path path) {
		currentDirectory = path.toAbsolutePath().normalize();
	}

	@Override
	public Object getSharedData(String key) {
		return sharedData.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		sharedData.put(key, value);
	}

	/**
	 * Shared data, each piece of data can be accessed by its key.
	 */
	private Map<String, Object> sharedData;

	/**
	 * Path to the current working directory.
	 */
	private Path currentDirectory;

	/** The reader for reading from the console. */
	BufferedReader reader;

	/** The writer for writing to the console. */
	BufferedWriter writer;

	/** Unmodifiable sorted map of (commandName, command) pairs */
	private SortedMap<String, ShellCommand> commands;

	/** Symbol that represents system's readiness to perform the next command. */
	private Character promptSymbol;

	/**
	 * Symbol that is used at the end of the line to inform the shell that more
	 * lines are expected.
	 */
	private Character moreLinesSymbol;

	/**
	 * Symbol that is printed on the beginning of every new line after the morelines
	 * symbol.
	 */
	private Character multilineSymbol;

}