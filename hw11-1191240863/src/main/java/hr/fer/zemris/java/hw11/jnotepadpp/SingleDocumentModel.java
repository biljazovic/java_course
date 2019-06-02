package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Single document model. Supports listener management.
 * Stores the information whether it was modified or not.
 * 
 * @author Bruno Iljazović
 */
public interface SingleDocumentModel {

	/**
	 * Gets the text component.
	 *
	 * @return the text component
	 */
	JTextArea getTextComponent();
	
	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	Path getFilePath();

	/**
	 * Sets the file path.
	 *
	 * @param path the new file path
	 */
	void setFilePath(Path path);
	
	/**
	 * Checks if it is modified.
	 *
	 * @return true, iff it is modified
	 */
	boolean isModified();
	
	/**
	 * Sets the modified flag.
	 *
	 * @param modified the new modified flag
	 */
	void setModified(boolean modified);
	
	/**
	 * Adds the single document listener.
	 *
	 * @param l the listener
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Removes the single document listener.
	 *
	 * @param l the listener
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
