package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Multiple document model. Supports adding new documents, saving them, loading
 * them from disk and closing them.
 * 
 * @author Bruno Iljazović
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Creates the blank document
	 *
	 * @return the document created
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Gets the current document.
	 *
	 * @return the current document
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Loads document from the disk.
	 *
	 * @param path
	 *            the path of the document, cannot be null
	 * @return the document created
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Saves the document to the path on disk. If the path is null, saves it to the
	 * current path.
	 *
	 * @param model
	 *            the document to save
	 * @param newPath
	 *            the new path to save to
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Closes the given document, without saving it.
	 *
	 * @param model the document to close
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Adds the multiple document listener.
	 *
	 * @param l the listener
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Removes the multiple document listener.
	 *
	 * @param l the listener
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Gets the number of documents.
	 *
	 * @return the number of documents
	 */
	int getNumberOfDocuments();
	
	/**
	 * Gets the document at the given index.
	 *
	 * @param index the index of the document
	 * @return the document at the given index.
	 */
	SingleDocumentModel getDocument(int index);

}
