package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides a method that interacts with user and encrypts/decrypts
 * user provided files with user provided keys.
 * 
 * @author Bruno Iljazović
 */
public class AESEncryption {

	/** buffer size */
	private static final int BUFF_SIZE = 4 * 1024;
	
	/** encryption key length in bytes*/
	private static final int AES_BYTE_LENGTH = 16;

	/**
	 * Encrypts/Decrypts provided files. AES keys are provided through the given
	 * buffered reader.
	 *
	 * @param encrypt
	 *            true, if the encryption is desired, false, if the decryption is
	 *            desired.
	 * @param file1
	 *            file to be encrypted/decrypted
	 * @param file2
	 *            destination file
	 * @param reader
	 *            the reader, for interacting with the user
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void encryptDecrypt(boolean encrypt, 
			String file1, String file2, BufferedReader reader) throws IOException {
		System.out.println( "Please provide password as hex-encoded text "
				+ "(16 bytes, i.e. 32 hex-digits):");
		System.out.print("> ");
		
		String keyText = reader.readLine().trim();
		
		byte[] keyBytes = Util.checkByteLength(keyText, AES_BYTE_LENGTH);
		if (keyBytes == null) {
			System.err.println("Invalid password format!");
			System.exit(-1);
		}
		
		System.out.println( "Please provide initialization vector as hex-encoded text "
				+ "(16 bytes, i.e. 32 hex-digits):");
		System.out.print("> ");
		
		String ivText = reader.readLine().trim();
		
		byte[] ivBytes = Util.checkByteLength(ivText, AES_BYTE_LENGTH);
		if (ivBytes == null) {
			System.err.println("Invalid initialization vector format!");
			System.exit(-1);
		}
		
		encryptDecrypt(encrypt, file1, file2, keyBytes, ivBytes);
		
	}
	
	/**
	 * Helper method that encrypts/decrypts the given file with given AES keys.
	 *
	 * @param encrypt
	 *            true, iff encryption is desired
	 * @param file1
	 *            file to be encrypted/decrypted
	 * @param file2
	 *            destination file
	 * @param keyBytes
	 *            the key
	 * @param ivBytes
	 *            the initialization vector
	 */
	private static void encryptDecrypt(boolean encrypt, String file1, String file2, 
			byte[] keyBytes, byte[] ivBytes) {
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivBytes);
		
		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch(NoSuchPaddingException | InvalidAlgorithmParameterException |
				InvalidKeyException | NoSuchAlgorithmException ex) {
			System.err.println("Something went wrong: " + ex);
		}
		
		try (InputStream inputStream = new BufferedInputStream(
				Files.newInputStream(Paths.get(file1), StandardOpenOption.READ));
			OutputStream outputStream = new BufferedOutputStream(
					Files.newOutputStream(Paths.get(file2)));
		){
			byte[] buff = new byte[BUFF_SIZE];
			int read;
			while ((read = inputStream.read(buff)) > -1) {
				outputStream.write(cipher.update(buff, 0, read));
			}
			outputStream.write(cipher.doFinal());
		} catch(IOException | BadPaddingException | IllegalBlockSizeException ex) {
			System.err.println("There was a problem reading and/or writing the files.");
			System.exit(-1);
		}

		System.out.printf("%s completed. Generated file %s based on file %s.%n", 
				encrypt ? "Encryption" : "Decryption", file2, file1);
	}
	
}
