package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;

/**
 * The hexdump command outputs the content of the given file where each byte is
 * displayed in hexadecimal format. Also, for each byte, if it is in the [32,
 * 127] range, corresponding character is displayed. If it is not in this range,
 * '.' is displayed instead. It takes exactly one argument: path to the file.
 * 
 * @author Bruno Iljazović
 */
public class HexdumpShellCommand implements ShellCommand {

	/** The  command name. */
	private static final String commandName = "hexdump";

	/** The description. */
	private static final List<String> description = Collections.unmodifiableList(Arrays.asList(
			"The hexdump command outputs the content of the given file where each byte is "
			+ "displayed in hexadecimal format.",
			"Also, for each byte, if it is in the [32, 127] range, corresponding character is "
			+ "displayed. If it is not in this range, '.' is displayed instead.",
			"It takes exactly one argument: path to the file."
	));

	/** buffer size */
	private static final int BUFF_SIZE = 16;
	
	/** minimum standard character */
	private static final int MIN_STANDARD_CHAR = 32;
	
	/** maximum standard character */
	private static final int MAX_STANDARD_CHAR = 127;

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

		try (
				InputStream reader = new BufferedInputStream(Files.newInputStream(
						env.getCurrentDirectory().resolve(Paths.get(argumentList.get(0)))));
		) {
			byte[] buff = new byte[BUFF_SIZE];
			int bytesRead, byteCount = 0;
			while ((bytesRead = reader.read(buff)) > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%08x: |", byteCount));
				byteCount += BUFF_SIZE;
				
				String hex = Util.bytetohex(buff);
				for (int i = 0; i < BUFF_SIZE; i += 2) {
					sb.append(' ');
					if (i < bytesRead * 2) {
						sb.append(hex.substring(i, i + 2).toUpperCase());
					} else {
						sb.append("  ");
					}
				}
				sb.append('|');
				for (int i = BUFF_SIZE; i < 2 * BUFF_SIZE; i += 2) {
					if (i < bytesRead * 2) {
						sb.append(hex.substring(i, i + 2).toUpperCase());
					} else {
						sb.append("  ");
					}
					sb.append(" ");
				}
				sb.append("| ");
				for (int i = 0; i < bytesRead; ++i) {
					if (buff[i] < MIN_STANDARD_CHAR || buff[i] > MAX_STANDARD_CHAR) {
						sb.append('.');
					} else {
						sb.append((char)buff[i]);
					}
				}
				
				env.writeln(sb.toString());
			}
		} catch( IOException ex) {
			env.writeln("There was a problem reading the files: " + ex);
			return ShellStatus.CONTINUE;
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
