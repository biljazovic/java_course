package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Command tree produces a depth indented listing of files and directories. It
 * takes exactly one argument: a valid directory path.
 * 
 * @author Bruno Iljazović
 */
public class TreeShellCommand implements ShellCommand {

	/** The command name. */
	private static final String commandName = "tree";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Command tree produces a depth indented listing of files and directories.",
			"It takes exactly one argument: a valid directory path."
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
		
		if (!Files.isDirectory(Paths.get(argumentList.get(0))) || 
				!Files.isReadable(Paths.get(argumentList.get(0)))) {
			env.writeln(ShellUtil.errorMessage(this, "Argument must be valid, readable directory."));
			return ShellStatus.CONTINUE;
		}
		
		try {
			
			Files.walkFileTree(Paths.get(argumentList.get(0)), new FileVisitor<Path>() {

				private StringBuilder indent = new StringBuilder();
				
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

					if (indent.length() == 0) {
						env.writeln(dir.toFile().getAbsolutePath());
					} else {
						env.writeln(indent + dir.toFile().getName());
					}

					indent.append("  ");
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					env.writeln(indent + file.toFile().getName());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
					indent.setLength(indent.length() - 2);
					return FileVisitResult.CONTINUE;
				}
			
			});
		} catch(IOException ex) {
			env.writeln("Error while reading directory content: " + ex);
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
