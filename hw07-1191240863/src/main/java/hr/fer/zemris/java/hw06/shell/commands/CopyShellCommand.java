package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * Copy command copies the content of the source file to the destination file.
 * if the destination file exists, user is asked to overwrite it. If the
 * destination path is a directory, new file with the same name as the source is
 * created in the directory. It takes exactly two arguments: source file path
 * and destination file/directory path
 * 
 * @author Bruno Iljazović
 */
public class CopyShellCommand implements ShellCommand {

	/** The command name. */
	private static final String commandName = "copy";
	
	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Copy command copies the content of the source file to the destination file.",
			"if the destination file exists, user is asked to overwrite it. If the destination"
			+ " path is a directory, new file with the same name as the source is created in "
			+ "the directory.",
			"It takes exactly two arguments: source file path and destination file/directory "
			+ "path"
	));

	/** buffer size */
	private static final int BUFF_SIZE = 4 * 1024;

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.ShellCommand#executeCommand(hr.fer.zemris.java.hw06.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argumentList = null;
		
		try {
			argumentList = ShellUtil.splitArguments(arguments);
		} catch(IllegalArgumentException ex) {
			env.writeln("Invalid argument(s).");
			return ShellStatus.CONTINUE;
		}
		
		if (argumentList.size() != 2) {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
			return ShellStatus.CONTINUE;
		}
		
		Path filePath = Paths.get(argumentList.get(0));
		Path fileDirPath = Paths.get(argumentList.get(1));
		
		File file1 = filePath.toFile();
		File file2 = fileDirPath.toFile();
		
		if (file1.isDirectory()) {
			env.writeln(ShellUtil.errorMessage(this, "Cannot copy directory."));
			return ShellStatus.CONTINUE;
		}
		
		if (file2.isDirectory()) {
			fileDirPath = fileDirPath.resolve(filePath.getFileName());
			file2 = fileDirPath.toFile();
		}
		
		if (file2.exists()) {
			
			env.write("File '" + fileDirPath + "' already exists. Proceed with "
					+ "copying? [y/N] ");
			String input = env.readLine().trim().toLowerCase();
			if (!input.equals("y")) {
				env.writeln("Copying aborted.");
				return ShellStatus.CONTINUE;
			}
		}
		
		try (
				InputStream is = new BufferedInputStream(
						Files.newInputStream(filePath));
				OutputStream os = new BufferedOutputStream(
						Files.newOutputStream(fileDirPath));
		) {
			byte[] buff = new byte[BUFF_SIZE];

			int bytesRead;
			while ((bytesRead = is.read(buff)) > 0) {
				os.write(buff, 0, bytesRead);
			}
		} catch(IOException ex) {
			env.writeln("There was a problem reading the files: " + ex);
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Successfully copied \'" + filePath + "' to '" + 
				fileDirPath + "'");
		
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
