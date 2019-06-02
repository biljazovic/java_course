package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Listener for change in a document.
 * 
 * @author Bruno Iljazović
 */
public interface SingleDocumentListener {

	/**
	 * Invoked when document modify status update occurs.
	 *
	 * @param model the updated document
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Invoked when document file path update occurs.
	 *
	 * @param model the updated document
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
