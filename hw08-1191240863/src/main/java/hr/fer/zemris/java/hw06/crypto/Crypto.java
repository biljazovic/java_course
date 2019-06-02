package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This program allows user to encrypt/decrypt given file using the AES
 * crypto-agorithm and check the SHA-256 file digest. Available commands are:
 * <li>checksha [FILE]</li>
 * <li>encrypt [SOURCE] [DESTINATION]</li>
 * <li>decrypt [SOURCE] [DESTINATION]</li> <b>
 * <p>
 * Commands are given through arguments from command line.
 * 
 * @author Bruno Iljazović
 */
public class Crypto {

	/**
	 * This method is called when the program is run.
	 *
	 * @param args
	 *            the arguments from the command line.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		
		if (args.length == 0) {
			System.err.println("No arguemnts provided.");
			System.exit(-1);
		}
		
		switch (args[0]) {
		case "checksha":
			if (args.length != 2) {
				System.err.println("Checksha should get exactly one argument.");
				System.exit(-1);
			}

			SHADigest.checksha(args[1], reader);

			break;
		case "encrypt":
			AESEncryption.encryptDecrypt(true, args[1], args[2], reader);
			break;
		case "decrypt":
			AESEncryption.encryptDecrypt(false, args[1], args[2], reader);
			break;
		default:
			System.err.println("Valid commands are: checksha, encrypt, decrypt.");
			System.exit(-1);
		}
		
		reader.close();
			
	}
	
	
}
