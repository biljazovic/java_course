package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * Has some utility functions.
 * 
 * @author Bruno Iljazović
 */
public class Util {
	
	/**
	 * Creates the ImageIcon object from the given path
	 * 
	 * @param path
	 *            of the icon
	 * @return the ImageIcon object from the given path
	 * @throws IllegalArgumentException
	 *             if the given path is invalid path to image
	 */
	public static ImageIcon createImageIcon(String path) {
		InputStream is = Util.class.getResourceAsStream(path);
		if (is == null) {
			throw new IllegalArgumentException("Invalid path to icon file." + path);
		}
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot read icon file " + path + ".");
		}
		return new ImageIcon(bytes);
	}
}
