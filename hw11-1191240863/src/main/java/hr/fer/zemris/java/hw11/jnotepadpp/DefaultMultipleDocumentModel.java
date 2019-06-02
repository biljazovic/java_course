package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Implementation of the {@link MultipleDocumentModel}. Stores the collection of
 * single documents. Tracks which document is currently showed to the user.
 *
 * @author Bruno Iljazović
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The documents. */
	private List<SingleDocumentModel> documents;
	
	/** The current document. */
	private SingleDocumentModel currentDocument;

	/** The listeners. */
	private List<MultipleDocumentListener> listeners;
	
	/** The modified icon. */
	private ImageIcon modifiedIcon;
	
	/** The unmodified icon. */
	private ImageIcon unmodifiedIcon;
	
	/** The localization provider. */
	FormLocalizationProvider flp;
	
	/**
	 * Instantiates a new multiple document model.
	 *
	 * @param flp the localization provider
	 */
	public DefaultMultipleDocumentModel(FormLocalizationProvider flp) {
		this.flp = flp;
		documents = new ArrayList<>();
		listeners = new ArrayList<>();
		
		modifiedIcon = Util.createImageIcon("icons/modified.png");
		unmodifiedIcon = Util.createImageIcon("icons/unmodified.png");
		
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		
		addChangeListener(e -> {
			SingleDocumentModel previousDocument = currentDocument;
			currentDocument = getSelectedIndex() == -1 ? null : documents.get(getSelectedIndex());
			for (MultipleDocumentListener listener : listeners) {
				listener.currentDocumentChanged(previousDocument, currentDocument);
			}
		});
		
		flp.addLocalizationListener(() -> {
			for (int i = 0; i < getTabCount(); i++) {
				if (documents.get(i).getFilePath() == null) {
					String title = getTitleAt(i);
					String newTitle = "";
					if (title.charAt(0) == '*') {
						newTitle += "*";
					}
					newTitle += flp.getString("Untitled");
					setTitleAt(i, newTitle);
				}
			}
		});
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return createNewDocument(null, "");
	}
	
	/**
	 * Creates the new document. Given filePath can be null. In that case, blank
	 * document is created.
	 *
	 * @param filePath
	 *            the file path, can be null
	 * @param textContent
	 *            the text content
	 * @return the created document
	 */
	private SingleDocumentModel createNewDocument(Path filePath, String textContent) {
		SingleDocumentModel newDocument = new DefaultSingleDocumentModel(filePath, textContent);
		documents.add(newDocument);
		
		addTab(
				filePath == null ? flp.getString("Untitled") : filePath.getFileName().toString(),
				unmodifiedIcon, 
				new JScrollPane(newDocument.getTextComponent()),
				filePath == null ? null : filePath.toAbsolutePath().normalize().toString()
		);
		
		newDocument.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				if (model.isModified()) {
					setTitleAt(getSelectedIndex(), "*" + getTitleAt(getSelectedIndex()));
					setIconAt(getSelectedIndex(), modifiedIcon);
				} else {
					setTitleAt(getSelectedIndex(), getTitleAt(getSelectedIndex()).substring(1));
					setIconAt(getSelectedIndex(), unmodifiedIcon);
				}
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setToolTipTextAt(
						getSelectedIndex(), 
						model.getFilePath().toAbsolutePath().normalize().toString()
				);
				setTitleAt(
						getSelectedIndex(),
						model.getFilePath().getFileName().toString()
				);
			}
		});
		
		setSelectedIndex(documents.size() - 1);
		for (MultipleDocumentListener listener : listeners) {
			listener.documentAdded(newDocument);
		}

		return newDocument;
	}
	
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "Path was null");
		
		for (int i = 0; i < documents.size(); i++) {
			if (path.equals(documents.get(i).getFilePath())) {
				setSelectedIndex(i);
				return getCurrentDocument();
			}
		}
		
		if (!Files.isReadable(path)) {
			JOptionPane.showMessageDialog(
					getParent(),
					MessageFormat.format(flp.getString("loadDocumentFail"), path.toString()),
					flp.getString("Error"),
					JOptionPane.ERROR_MESSAGE
			);
			return null;
		}
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(path);
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(
					getParent(),
					flp.getString("loadDocumentFail"),
					flp.getString("Error"),
					JOptionPane.ERROR_MESSAGE
			);
			return null;
		}
		String textContent = new String(bytes, StandardCharsets.UTF_8);

		return createNewDocument(path, textContent);
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if (newPath == null) {
			newPath = model.getFilePath();
			if (newPath == null) {
				//THIS SHOULDN'T HAPPEN
				throw new IllegalArgumentException("Cannot save untitled document with no path");
			}
		}

		for (int i = 0; i < documents.size(); i++) {
			SingleDocumentModel md = documents.get(i);
			if (model == md) continue;
			if (md.getFilePath().equals(newPath)) {
				JOptionPane.showMessageDialog(
						getParent(),
						MessageFormat.format(flp.getString("fileAlreadyOpened"), newPath.toString()),
						flp.getString("Error"),
						JOptionPane.ERROR_MESSAGE
				);
				return;
			}
		}
		
		byte[] textContent = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(newPath, textContent);
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(
					getParent(),
					MessageFormat.format(flp.getString("saveDocumentFail"), newPath.toString()),
					flp.getString("Error"),
					JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		model.setModified(false);
		model.setFilePath(newPath);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		if (index == -1) throw new IllegalArgumentException("Document isn't opened"); 

		documents.remove(index);
		removeTabAt(index);
		
		for (MultipleDocumentListener listener : listeners) {
			listener.documentRemoved(currentDocument);
		}
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		if (listeners.contains(Objects.requireNonNull(l))) return;
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

}
