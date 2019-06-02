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
 * Cptree command copies the entire file tree of one directory to some other
 * directory. Example: cptree dir1/dir2 dir3/dir4 : if dir3/dir4 eixsts,
 * dir3/dir4/dir2 is created and content of dir1/dir2/ is copied to the
 * dir3/dir4/dir2/. If dir3/dir4 does not exist, but dir3 exists, content of the
 * dir1/dir2 is copied to the dir3/dir4/. If dir3 does not exist an error is
 * written to console. Command takes exactly two arguments: source directory
 * path and destination directory path
 * 
 * @author Bruno Iljazović
 */
public class CptreeShellCommand implements ShellCommand {

	/** The Constant commandName. */
	private static final String commandName = "cptree";

	/** The Constant description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Cptree command copies the entire file tree of one directory to some other directory.",
			"Example: cptree dir1/dir2 dir3/dir4 : if dir3/dir4 eixsts, dir3/dir4/dir2 is created "
			+ "and content of dir1/dir2/ is copied to the dir3/dir4/dir2/.",
			"If dir3/dir4 does not exist, but dir3 exists, content of the dir1/dir2 is copied "
			+ "to the dir3/dir4/.",
			"If dir3 does not exist an error is written to console.",
			"Command takes exactly two arguments: source directory path and destination directory"
			+ " path"
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
		
		if (argumentList.size() != 2) {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
			return ShellStatus.CONTINUE;
		}
		
		Path sourcePath = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0)))
				.normalize();
		Path destinationPath = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(1)))
				.normalize();

		if (!Files.isDirectory(sourcePath)) {
			env.writeln(ShellUtil.errorMessage(this, "Given directory does not exist."));
			return ShellStatus.CONTINUE;
		} 
		
		if (Files.isDirectory(destinationPath)) {
			destinationPath = destinationPath.resolve(sourcePath.getFileName());
		} else if (!Files.isDirectory(destinationPath.getParent())) {
			env.writeln(ShellUtil.errorMessage(this, "Invalid destination directory path."));
			return ShellStatus.CONTINUE;
		}
		
		try {
			
			Files.walkFileTree(sourcePath, new CopyFileVisitor(sourcePath, destinationPath));
		} catch(IOException ex) {
			env.writeln("Error while copying files: " + ex);
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

	/**
	 * Helper class that visits every file in source directory tree
	 */
	private class CopyFileVisitor implements FileVisitor<Path> {

		/** The destination path. */
		private Path destinationPath;

		/** The source path. */
		private Path sourcePath;
		
		/**
		 * Instantiates a new file visitor.
		 *
		 * @param sourcePath the source path
		 * @param destinationPath the destination path
		 */
		public CopyFileVisitor(Path sourcePath, Path destinationPath) {
			this.destinationPath = destinationPath;
			this.sourcePath = sourcePath;
		}

		/* (non-Javadoc)
		 * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				throws IOException {
			Files.createDirectories(destinationPath.resolve(sourcePath.relativize(dir)));
			return FileVisitResult.CONTINUE;
		}

		/* (non-Javadoc)
		 * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
		 */
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.copy(file, destinationPath.resolve(sourcePath.relativize(file)));
			return FileVisitResult.CONTINUE;
		}

		/* (non-Javadoc)
		 * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
		 */
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			return FileVisitResult.CONTINUE;
		}

		/* (non-Javadoc)
		 * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			return FileVisitResult.CONTINUE;
		}

	}
}
