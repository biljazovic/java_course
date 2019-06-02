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
 * Rmtree command deletes the given directory (if it exists) and its entire
 * content. It cannot be used to delete single file (non-directory). It takes
 * exactly one argument: valid directory path.
 * 
 * @author Bruno Iljazović
 */
public class RmtreeShellCommand implements ShellCommand {
	
	/** The command name. */
	private static final String commandName = "rmtree";
	
	/** The  description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Rmtree command deletes the given directory (if it exists) and its entire content.",
			"It cannot be used to delete single file (non-directory).",
			"It takes exactly one argument: valid directory path."
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
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0)));

		if (!Files.isDirectory(path)) {
			env.writeln(ShellUtil.errorMessage(this, "Given directory does not exist."));
			return ShellStatus.CONTINUE;
		} 
		
		try {
			
			Files.walkFileTree(path, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
						throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
						throws IOException {
					Files.delete(dir);
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
