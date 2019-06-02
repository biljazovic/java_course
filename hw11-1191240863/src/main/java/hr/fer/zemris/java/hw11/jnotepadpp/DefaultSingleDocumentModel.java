package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * Implementation of the {@link SingleDocumentModel}. Creates a JTextArea object
 * for text content.
 * 
 * @author Bruno Iljazović
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/** The text component. */
	private JTextArea textComponent;
	
	/** The file path. */
	private Path filePath;
	
	/** Modified flag. */
	private boolean modified;
	
	/** The listeners. */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * Instantiates a new document.
	 *
	 * @param filePath
	 *            the file path, can be null
	 * @param textContent
	 *            the text content of the document
	 */
	public DefaultSingleDocumentModel(Path filePath, String textContent) {
		textComponent = new JTextArea(textContent);
		this.filePath = filePath;
		listeners = new ArrayList<>();
		
		textComponent.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) { }
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return textComponent;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		filePath = Objects.requireNonNull(path);
		notifyOfPathChange();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * Notify of modify change.
	 */
	private void notifyOfModifyChange() {
		for (SingleDocumentListener listener : listeners) {
			listener.documentModifyStatusUpdated(this);
		}
	}
	
	/**
	 * Notify of path change.
	 */
	private void notifyOfPathChange() {
		for (SingleDocumentListener listener : listeners) {
			listener.documentFilePathUpdated(this);
		}
	}

	@Override
	public void setModified(boolean modified) {
		if (this.modified == modified) return;
		this.modified = modified;
		notifyOfModifyChange();
	}
	

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		if (listeners.contains(Objects.requireNonNull(l))) return;
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
}
