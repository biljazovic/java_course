package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * This class provides method that interacts with user and checks whether the
 * given file's SHA digest matches the user's one.
 * 
 * @author Bruno Iljazović
 */
public class SHADigest {

	/** SHA key length in bytes */
	private static final int SHA_BYTE_LENGTH = 32;

	/** buffer size */
	private static final int BUFF_SIZE = 4 * 1024;
	
	/**
	 * Asks the user for the SHA key and then compares the key generated from the
	 * given file and the user's one.
	 *
	 * @param filepath
	 *            the file path
	 * @param reader
	 *            the reader of the user input
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void checksha(String filepath, BufferedReader reader) throws IOException {
		System.out.printf("Please provide expected sha-256 digest for %s:%n> ", filepath);
		
		String expectedKey = reader.readLine().trim();
		byte[] expectedKeyBytes = Util.checkByteLength(expectedKey, SHA_BYTE_LENGTH);
		
		if (expectedKeyBytes == null) {
			System.err.println("Invalid expected digest key!");
			System.exit(-1);
		}
		
		byte[] digested = digest(filepath);
		
		if (digested == null) {
			System.exit(-1);
		}
		
		if (Arrays.equals(expectedKeyBytes, digested)) {
			System.out.printf("Digesting completed. Digest of %s matches expected digest.%n",
					filepath);
		} else {
			System.out.printf("Digesting completed. Digest of %s does not match the expected "
					+ "digest. Digest was: %s%n", filepath, Util.bytetohex(digested));
		}
	}
	
	/**
	 * Generates the SHA key from the given file.
	 *
	 * @param filepath
	 *            the file path
	 * @return SHA key of the file
	 */
	private static byte[] digest(String filepath) {
		try (InputStream inputStream = new BufferedInputStream(
				Files.newInputStream(Paths.get(filepath), StandardOpenOption.READ)
		)) {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch(NoSuchAlgorithmException ex) {
				System.err.println("No SHA-256 algorithm on this system.");
				System.exit(-1);
			}

			byte[] buff = new byte[BUFF_SIZE];
			int read;
			while ((read = inputStream.read(buff)) > -1) {
				md.update(buff, 0, read);
			}
			
			return md.digest();

		} catch(IOException ex) {
			System.err.println("There was a problem reading the file.");
			return null;
		}
	}
}
