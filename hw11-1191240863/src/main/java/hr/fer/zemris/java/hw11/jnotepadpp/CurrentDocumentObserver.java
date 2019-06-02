/*
 * 
 */
package hr.fer.zemris.java.hw11.jnotepadpp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;

/**
 * A collection of listeners that want to listen to the currently shown
 * document. When a current document changes, it removes the listeners from the
 * old, and adds them to the new document.
 * 
 * @author Bruno Iljazović
 */
public class CurrentDocumentObserver {
	
	/** The single document listeners. */
	List<SingleDocumentListener> singleDocumentListeners;
	
	/** The caret listeners. */
	List<CaretListener> caretListeners;
	
	/** The document listeners. */
	List<DocumentListener> documentListeners;

	/**
	 * Instantiates the current document observer.
	 *
	 * @param doc the multiple document model
	 */
	public CurrentDocumentObserver(MultipleDocumentModel doc) {
		singleDocumentListeners = new ArrayList<>();
		caretListeners = new ArrayList<>();
		documentListeners = new ArrayList<>();

		doc.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) { }
			
			@Override
			public void documentAdded(SingleDocumentModel model) { }
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel,
					SingleDocumentModel currentModel) {
				if (previousModel != null) {
					for (SingleDocumentListener listener : singleDocumentListeners) {
						previousModel.removeSingleDocumentListener(listener);
					}
					for (CaretListener listener : caretListeners) {
						previousModel.getTextComponent().removeCaretListener(listener);
					}
					for (DocumentListener listener : documentListeners) {
						previousModel.getTextComponent().getDocument().removeDocumentListener(listener);
					}
				}
				if (currentModel != null) {
					for (SingleDocumentListener listener : singleDocumentListeners) {
						currentModel.addSingleDocumentListener(listener);
					}
					for (CaretListener listener : caretListeners) {
						currentModel.getTextComponent().addCaretListener(listener);
					}
					for (DocumentListener listener : documentListeners) {
						currentModel.getTextComponent().getDocument().addDocumentListener(listener);
					}
				}
				for (SingleDocumentListener listener : singleDocumentListeners) {
					listener.documentModifyStatusUpdated(currentModel);
				}
				for (CaretListener listener : caretListeners) {
					listener.caretUpdate(null);
				}
				for (DocumentListener listener : documentListeners) {
					listener.insertUpdate(null);
				}
			}
		});
	}
	
	/**
	 * Adds the caret listener.
	 *
	 * @param listener the listener to add
	 */
	public void addCaretListener(CaretListener listener) {
		if (caretListeners.contains(Objects.requireNonNull(listener))) return;
		caretListeners.add(listener);
	}
	
	/**
	 * Removes the caret listener.
	 *
	 * @param listener the listener to remove
	 */
	public void removeCaretListener(CaretListener listener) {
		caretListeners.remove(listener);
	}
	
	/**
	 * Adds the single document listener
	 *
	 * @param listener the listener to add
	 */
	public void addSingleDocumentListener(SingleDocumentListener listener) {
		if (singleDocumentListeners.contains(Objects.requireNonNull(listener))) return;
		singleDocumentListeners.add(listener);
	}
	
	/**
	 * Removes the single document listener
	 *
	 * @param listener the listener remove
	 */
	public void removeSingleDocumentListener(SingleDocumentListener listener) {
		singleDocumentListeners.remove(listener);
	}
	
	/**
	 * Adds the document litener
	 *
	 * @param listener the listener to add
	 */
	public void addDocumentListener(DocumentListener listener) {
		if (documentListeners.contains(Objects.requireNonNull(listener))) return;
		documentListeners.add(listener);
	}
		
	/**
	 * removes the document listener.
	 *
	 * @param listener the listener to remove
	 */
	public void removeDocumentListener(DocumentListener listener) {
		documentListeners.remove(listener);
	}
}
