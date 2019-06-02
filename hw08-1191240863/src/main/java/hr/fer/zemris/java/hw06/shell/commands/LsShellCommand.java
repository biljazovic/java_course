package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * The ls command outputs the contents of the directory, non-recursive, i.e.
 * with depth 1. It takes exactly one argument: path to the directory.
 * For each file/directory, following is printed:
 * <li>is it directory, readable, writable and executable</li>
 * <li>length in bytes</li>
 * <li>creation date</li>
 * <li>file/directory name</li>
 * <p>
 * 
 * @author Bruno Iljazović
 */
public class LsShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "ls";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"The ls command outputs the contents of the directory, non-recursive, i.e. "
			+ "with depth 1.",
			"It takes exactly one argument: path to the directory."
	));

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#executeCommand(hr.fer.zemris.java.hw06.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		List<String> argumentList = null;
		
		try {
			argumentList = ShellUtil.splitArguments(arguments);
		} catch (IllegalArgumentException ex) {
			env.writeln("Invalid argument(s).");
			return ShellStatus.CONTINUE;
		}
		
		if (argumentList.size() != 1) {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
			return ShellStatus.CONTINUE;
		}
		
		Path dirPath = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0)));
		
		File[] files;

		files = dirPath.toFile().listFiles();
		
		if (files == null) {
			env.writeln(ShellUtil.errorMessage(this, "Argument isn't a valid, readable directory."));
			return ShellStatus.CONTINUE;
		}
		
		for (File file : files) {
			try {
				env.writeln(generateListing(file));
			} catch (IOException ex) {
				env.writeln("There was a problem reading the file: " + ex.getMessage());
				return ShellStatus.CONTINUE;
			}
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Generates listing of the given file.
	 *
	 * @param file the file
	 * @return the generated listing
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String generateListing(File file) throws IOException {

		BasicFileAttributeView faView = Files.getFileAttributeView(file.toPath(),
				BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
	
		BasicFileAttributes attributes = faView.readAttributes();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return String.format(
				"%c%c%c%c %10d %s %s",
				(file.isDirectory() ? 'd' : '-'),
				(file.canRead() ? 'r' : '-'),
				(file.canWrite() ? 'w' : '-'),
				(file.canExecute() ? 'x' : '-'),
				attributes.size(),
				sdf.format(new Date(attributes.creationTime().toMillis())),
				file.getName()
		);
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
