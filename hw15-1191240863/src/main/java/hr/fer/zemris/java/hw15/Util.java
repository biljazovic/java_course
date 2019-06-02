package hr.fer.zemris.java.hw15;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Contains some utility functions.
 * 
 * @author Bruno Iljazović
 */
public class Util {

	/** Used for converting the digested bytes into the hex string. */
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**
	 * Gets the password hash from the password. Hash is generated using SHA-1
	 * algorithm.
	 *
	 * @param password the password
	 * @return the password hash
	 */
	public static String getPasswordHash(String password) {
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) { }
		
		md.update(password.getBytes());
		byte[] digest = md.digest();
		
		char[] hexDigest = new char[digest.length * 2];
		for (int i = 0; i < digest.length; i++) {
			int v = digest[i] & 0xFF;
	        hexDigest[i * 2] = hexArray[v >>> 4];
	        hexDigest[i * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexDigest);
	}
}
