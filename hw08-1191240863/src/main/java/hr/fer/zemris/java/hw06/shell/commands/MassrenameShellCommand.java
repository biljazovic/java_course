package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.NameBuilder;
import hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfo;
import hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfoImpl;
import hr.fer.zemris.java.hw06.shell.parser.NameBuilderParser;

/**
 * Massrename command moves/renames files directly in the source directory (not
 * in its whole tree). there are 4 subcommands:
 * <p>
 * 'massrename dir1 dir2 filter mask' outputs files in the dir1 that match the
 * regular expression given in mask
 * <p>
 * 'massrename dir1 dir2 groups mask' outputs for each file matched by mask from
 * dir1 all capturing groups from the regex mask, including the 0th group that
 * is the entire file name.
 * <p>
 * 'massrename dir1 dir2 show mask expression' outputs for each file matched by
 * mask from dir1 new name that is described in expression. expression can
 * contain sequnces like ${2} which is replaced with 2nd capturing group from
 * mask. it can also contain sequnces like ${2, 3} (or ${2, 03}) which are
 * replaced with 2nd capturing group but left-padded with spaces (or zeros).
 * <p>
 * 'massrename dir1 dir2 execute mask expression' moves files and renames them
 * as described by show command
 * 
 * @author Bruno Iljazović
 */
public class MassrenameShellCommand implements ShellCommand {

	/** The command name. */
	private  static final String commandName = "massrename";

	/** The  description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"Massrename command moves/renames files directly in the source directory (not in "
			+ "its whole tree).",
			"there are 4 subcommands:",
			"'massrename dir1 dir2 filter mask' outputs files in the dir1 that match the regular "
			+ "expression given in mask",
			"'massrename dir1 dir2 groups mask' outputs for each file matched by mask from dir1 all "
			+ "capturing groups from the regex mask, including the 0th group that is the entire "
			+ "file name.",
			"'massrename dir1 dir2 show mask expression' outputs for each file matched by mask from"
			+ " dir1 new name that is described in expression.",
			"expression can contain sequnces "
			+ "like ${2} which is replaced with 2nd capturing group from mask. it can also contain "
			+ "sequnces like ${2, 3} (or ${2, 03}) which are replaced with 2nd capturing group but "
			+ "left-padded with spaces (or zeros).",
			"'massrename dir1 dir2 execute mask expression' moves files and renames them as "
			+ "described by show command"
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
			env.writeln(ShellUtil.errorMessage(this, "Invalid argument(s)."));
			return ShellStatus.CONTINUE;
		}

		if (argumentList.size() < 4 || argumentList.size() > 5) {
			env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
			return ShellStatus.CONTINUE;
		}

		Path sourcePath = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0)))
				.normalize();
		Path destinationPath = env.getCurrentDirectory().resolve(Paths.get(argumentList.get(1)))
				.normalize();

		if (!Files.isDirectory(sourcePath) || !Files.isDirectory(destinationPath)) {
			env.writeln(ShellUtil.errorMessage(this, "Given directory does not exist."));
			return ShellStatus.CONTINUE;
		}

		try {
			switch (argumentList.get(2)) {
			case "filter":
				if (argumentList.size() != 4) {
					env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
					return ShellStatus.CONTINUE;
				}
				filterCommand(env, sourcePath, argumentList.get(3));
				break;
			case "groups":
				if (argumentList.size() != 4) {
					env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
					return ShellStatus.CONTINUE;
				}
				groupsCommand(env, sourcePath, argumentList.get(3));
				break;
			case "show":
				if (argumentList.size() != 5) {
					env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
					return ShellStatus.CONTINUE;
				}
				showCommand(env, sourcePath, destinationPath, argumentList.get(3),
						argumentList.get(4));
				break;
			case "execute":
				if (argumentList.size() != 5) {
					env.writeln(ShellUtil.errorMessage(this, "Missing/too many arguments."));
					return ShellStatus.CONTINUE;
				}
				executeCommand(env, sourcePath, destinationPath, argumentList.get(3),
						argumentList.get(4));
				break;
			default:
				env.writeln(
						ShellUtil.errorMessage(this, "Unknown subcommand " + argumentList.get(2)));

			}
		} catch (RuntimeException ex) {
			env.writeln("Invalid argument(s): " + ex);
		} catch (IOException ex) {
			env.writeln("There was a problem reading the directory content: " + ex);
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
	 * Filter subcommand.
	 *
	 * @param env the environment
	 * @param sourceDir the source directory
	 * @param mask regex for filtering files
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void filterCommand(Environment env, Path sourceDir, String mask) throws IOException {
		Pattern pattern = Pattern.compile(mask);
		for (Path file : Files.newDirectoryStream(sourceDir)) {
			if (Files.isDirectory(file))
				continue;
			Matcher matcher = pattern.matcher(file.getFileName().toString());
			if (!matcher.matches())
				continue;
			env.writeln(file.getFileName().toString());
		}
	}

	/**
	 * Groups subcommand.
	 *
	 * @param env the environment
	 * @param sourceDir the source directory
	 * @param mask regex for filtering files
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void groupsCommand(Environment env, Path sourceDir, String mask) throws IOException {
		Pattern pattern = Pattern.compile(mask);
		for (Path file : Files.newDirectoryStream(sourceDir)) {
			if (Files.isDirectory(file))
				continue;
			Matcher matcher = pattern.matcher(file.getFileName().toString());
			if (!matcher.matches())
				continue;

			StringBuilder sb = new StringBuilder();
			sb.append(file.getFileName().toString());

			for (int i = 0; i < matcher.groupCount() + 1; ++i) {
				sb.append(" " + i + ": " + matcher.group(i));
			}

			env.writeln(sb.toString());
		}
	}

	/**
	 * Show subcommand.
	 *
	 * @param env the environment
	 * @param sourceDir the source directory
	 * @param destinationPath the destination directory
	 * @param mask regex for filtering files
	 * @param expression expression for new file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void showCommand(Environment env, Path sourceDir, Path destinationPath, String mask,
			String expression) throws IOException {
		Pattern pattern = Pattern.compile(mask);

		NameBuilder builder = new NameBuilderParser(expression).getNameBuilder();

		for (Path file : Files.newDirectoryStream(sourceDir)) {
			if (Files.isDirectory(file))
				continue;
			Matcher matcher = pattern.matcher(file.getFileName().toString());
			if (!matcher.matches())
				continue;

			StringBuilder sb = new StringBuilder();
			sb.append(file.getFileName().toString() + " => ");

			NameBuilderInfo info = new NameBuilderInfoImpl(matcher);
			builder.execute(info);
			String newName = info.getStringBuilder().toString();

			sb.append(newName);

			env.writeln(sb.toString());
		}
	}

	/**
	 * Execute subcommand.
	 *
	 * @param env the environment
	 * @param sourceDir the source directory
	 * @param destinationPath the destination directory
	 * @param mask regex for filtering files
	 * @param expression expression for new file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void executeCommand(Environment env, Path sourceDir, Path destinationPath, String mask,
			String expression) throws IOException {
		Pattern pattern = Pattern.compile(mask);

		NameBuilder builder = new NameBuilderParser(expression).getNameBuilder();

		for (Path file : Files.newDirectoryStream(sourceDir)) {
			if (Files.isDirectory(file))
				continue;
			Matcher matcher = pattern.matcher(file.getFileName().toString());
			if (!matcher.matches())
				continue;

			StringBuilder sb = new StringBuilder();
			sb.append(file.normalize().toString() + " => ");

			NameBuilderInfo info = new NameBuilderInfoImpl(matcher);
			builder.execute(info);
			String newName = info.getStringBuilder().toString();
			Path newFilePath = destinationPath.resolve(newName).normalize();

			Files.move(file, newFilePath);

			sb.append(newFilePath.toString());
			env.writeln(sb.toString());
		}
	}
}
