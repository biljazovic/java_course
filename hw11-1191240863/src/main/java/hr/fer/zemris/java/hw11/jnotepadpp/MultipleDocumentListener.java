package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Listener for change in multiple document model.
 * 
 * @author Bruno Iljazović
 */
public interface MultipleDocumentListener {
	
	/**
	 * Current document changed.
	 *
	 * @param previousModel
	 *            the previous document
	 * @param currentModel
	 *            the current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	/**
	 * Document added.
	 *
	 * @param model
	 *            the added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Document removed.
	 *
	 * @param model
	 *            the removed document
	 */
	void documentRemoved(SingleDocumentModel model);
}
