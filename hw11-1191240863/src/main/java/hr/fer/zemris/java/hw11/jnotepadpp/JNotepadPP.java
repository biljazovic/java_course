package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * A simple text file editor. Supports opening multiple documents,
 * copy/cut/paste operations, showing basic info about opened documents, sorting
 * selected lines, switching case of the selected text.
 * <p>
 * Supports three languages: English, Croatian and German.
 * <p>
 * For Croatian, dialogs and file choosers are shown in English instead.
 * 
 * @author Bruno Iljazović
 */
public class JNotepadPP extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The localization provider */
	FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	
	/** The application name. */
	private final String applicationName = "Jnotepad++";
	
	/** The file chooser.. */
	JFileChooser fc;
	
	/** The timer used for showing the clock. */
	Timer timer;

	/** Documents currently opened in the editor. */
	MultipleDocumentModel documents;
	
	/** Observer of the currently opened document */
	CurrentDocumentObserver observer;
	
	/**
	 * Instantiates a new JNotepad++
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(100, 100);
		setTitle(applicationName);
		setSize(1000, 700);
		setMinimumSize(new Dimension(600, 400));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}
		});
		
		initGUI();
	}

	/**
	 * Initalized the GUI, creates All Menus, toolbar and status bar.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		fc = new JFileChooser();
		
		flp.addLocalizationListener(() -> {
			updateTitle();
			fc = new JFileChooser();
		});
		
		DefaultMultipleDocumentModel defaultDocument = new DefaultMultipleDocumentModel(flp);
		documents = defaultDocument;
		observer = new CurrentDocumentObserver(documents);
		
		createActions();
		createMenus();
		createToolBar();
		createStatusBar();

		cp.add(defaultDocument, BorderLayout.CENTER);
	}

	/**
	 * The main method that is called when the program is run
	 *
	 * @param args
	 *            not used here
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			(new JNotepadPP()).setVisible(true);
		});
	}
	
	/** Preferred status bar height. */
	private final int STATUS_BAR_HEIGHT = 20;
	
	/**
	 * Creates the status bar.
	 */
	private void createStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		statusBar.setPreferredSize(new Dimension(getWidth(), STATUS_BAR_HEIGHT));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		
		LocalizableNumberJLabel length = new LocalizableNumberJLabel(flp, "length", 0);
		length.setVisible(false);
		
		observer.addDocumentListener(new DocumentListener() {
			
			private void contentChanged() {
				if (documents.getCurrentDocument() != null) {
					length.setVisible(true);
					length.setNumber(documents.getCurrentDocument().getTextComponent()
							.getDocument().getLength());
				} else {
					length.setVisible(false);
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				contentChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				contentChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) { }
		});
		
		LocalizableNumberJLabel lineNumber = new LocalizableNumberJLabel(flp, "lineNumber", 0);
		LocalizableNumberJLabel columnNumber = new LocalizableNumberJLabel(flp, "column", 0);
		LocalizableNumberJLabel selectionNumber = new LocalizableNumberJLabel(flp, "selection", 0);

		lineNumber.setVisible(false);
		columnNumber.setVisible(false);
		selectionNumber.setVisible(false);
		
		observer.addCaretListener(e -> {
			if (documents.getCurrentDocument() != null) {
				lineNumber.setVisible(true);
				columnNumber.setVisible(true);
				selectionNumber.setVisible(true);
				JTextArea jta = documents.getCurrentDocument().getTextComponent();
				int pos = jta.getCaretPosition();
				int line = 0, column = 0, selection = 0;
				try {
					line = jta.getLineOfOffset(pos);
					column = pos - jta.getLineStartOffset(line);
					selection = Math.abs(jta.getCaret().getDot() - jta.getCaret().getMark());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				lineNumber.setNumber(line+1);
				columnNumber.setNumber(column);
				selectionNumber.setNumber(selection);
			} else {
				lineNumber.setVisible(false);
			}
		});
		
		JLabel clock = new JLabel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		
		timer = new Timer(-500, e -> {
			clock.setText(sdf.format(new Date()));
		});
		timer.start();
		
		statusBar.add(length);
		statusBar.add(Box.createHorizontalGlue());
		statusBar.add(lineNumber);
		statusBar.add(Box.createRigidArea(new Dimension(10, 0)));
		statusBar.add(columnNumber);
		statusBar.add(Box.createRigidArea(new Dimension(10, 0)));
		statusBar.add(selectionNumber);
		statusBar.add(Box.createHorizontalGlue());
		statusBar.add(clock);

		
		getContentPane().add(statusBar, BorderLayout.PAGE_END);
	}

	/**
	 * Creates the tool bar.
	 */
	private void createToolBar() {
		JToolBar toolBar = new JToolBar(flp.getString("tools"));
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(loadDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveDocumentAsAction));
		toolBar.add(new JButton(closeDocumentAction));
		toolBar.add(new JButton(exitAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(pasteAction));
		toolBar.add(new JButton(statusAction));
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Initializes some action parameters.
	 */
	private void createActions() {
		saveDocumentAction.setEnabled(false);
		saveDocumentAsAction.setEnabled(false);
		closeDocumentAction.setEnabled(false);
		copyAction.setEnabled(false); 
		cutAction.setEnabled(false);
		pasteAction.setEnabled(false);
		statusAction.setEnabled(false);
		
		observer.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				if (model == null) {
					saveDocumentAction.setEnabled(false);
					saveDocumentAsAction.setEnabled(false);
				} else {
					saveDocumentAction.setEnabled(model.isModified());
					saveDocumentAsAction.setEnabled(true);
				}
				updateTitle();
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) { }
		});
		
		observer.addCaretListener(e -> {
			if (documents.getCurrentDocument() == null) {
				copyAction.setEnabled(false);
				cutAction.setEnabled(false);
				pasteAction.setEnabled(false);
				
			} else {
				Caret caret = documents.getCurrentDocument().getTextComponent().getCaret();
				copyAction.setEnabled(caret.getMark() != caret.getDot());
				cutAction.setEnabled(caret.getMark() != caret.getDot());
				pasteAction.setEnabled(true);
			}
		});
		
		documents.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) { }
			
			@Override
			public void documentAdded(SingleDocumentModel model) { }
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel,
					SingleDocumentModel currentModel) {
				if (previousModel == null) {
					closeDocumentAction.setEnabled(true);
					statusAction.setEnabled(true);
				} else if (currentModel == null) {
					closeDocumentAction.setEnabled(false);
					statusAction.setEnabled(false);
				}
				updateTitle();
			}
		});
	}
	
	/**
	 * JMenu which changes its title based on the current language.
	 */
	private class LocalizableJMenu extends JMenu {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new LocalizableJMenu
		 *
		 * @param key
		 *            the key for localization
		 */
		public LocalizableJMenu(String key) {
			super(flp.getString(key));
			flp.addLocalizationListener(() -> {
				setText(flp.getString(key));
			});
		}
	}

	/**
	 * Creates the menus and submenus.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new LocalizableJMenu("file");
		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(loadDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAsAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.add(new JMenuItem(exitAction));
		
		JMenu editMenu = new LocalizableJMenu("edit");
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.addSeparator();
		editMenu.add(new JMenuItem(statusAction));
		
		JMenu languageMenu = new LocalizableJMenu("language");
		String[] languages = {"en", "hr", "de"};
		for (String language : languages) {
			Action languageAction = new AbstractAction() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					LocalizationProvider.getInstance().setLanguage(language);
					
				}
			};
			languageAction.putValue(Action.NAME, language);
			languageMenu.add(new JMenuItem(languageAction));
		}
		
		JMenu toolMenu = new LocalizableJMenu("tools");

		JMenu changeCase = new LocalizableJMenu("changeCase");
		changeCase.add(new JMenuItem(
				new ChangeSelectedAction("toUpperCase", "control F5", String::toUpperCase)
		));
		changeCase.add(new JMenuItem(
				new ChangeSelectedAction("toLowerCase", "control F6", String::toLowerCase)
		));
		changeCase.add(new JMenuItem(
				new ChangeSelectedAction("toggleCase", "control F7", toggleCase)
		));
		
		JMenu sort = new LocalizableJMenu("sort");
		sort.add(new JMenuItem(
				new ChangeSelectedLinesAction("ascending", "control F2", sortAscending)
		));
		sort.add(new JMenuItem(
				new ChangeSelectedLinesAction("descending", "control F3", sortDescending)
		));
		sort.add(new JMenuItem(
				new ChangeSelectedLinesAction("unique", "control F4", e -> {
					return new ArrayList<>(new LinkedHashSet<>(e));
				})
		));

		toolMenu.add(changeCase);
		toolMenu.add(sort);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(toolMenu);
		menuBar.add(languageMenu);
		
		setJMenuBar(menuBar);
	}

	/**
	 * Updates title with the new current document's name.
	 */
	private void updateTitle() {
		SingleDocumentModel currentModel = documents.getCurrentDocument();
		StringBuilder newName = new StringBuilder();
		if (currentModel != null) {
			if (currentModel.isModified()) {
				newName.append("*");
			}
			Path filePath = currentModel.getFilePath();
			newName.append((filePath == null ? flp.getString("Untitled") : filePath.getFileName())
					+ " - ");
		}
		newName.append(applicationName);
		setTitle(newName.toString());
	}
	
	/**
	 * Sorts the given list of strings in ascending order with current locale in
	 * mind.
	 */
	UnaryOperator<List<String>> sortAscending = e -> {
		Locale locale = Locale
				.forLanguageTag(LocalizationProvider.getInstance().getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);
		Collections.sort(e, collator::compare);
		return e;
	};

	/**
	 * Sorts the given list of strings in descending order with current locale in
	 * mind.
	 */
	UnaryOperator<List<String>> sortDescending = e -> {
		Locale locale = Locale
				.forLanguageTag(LocalizationProvider.getInstance().getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);
		Collections.sort(e, (a, b) -> {
			return -collator.compare(a, b);
		});
		return e;
	};
	
	/**
	 * Localizable action that modifies currently selected lines in some way. If the
	 * selection spans only part of some line, whole line is affected.
	 */
	private class ChangeSelectedLinesAction extends LocalizableAction {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;
		
		/** Modifes the given list of strings in some way. */
		UnaryOperator<List<String>> op;
		
		/**
		 * Instantiates a new action.
		 *
		 * @param key
		 *            the key for localization
		 * @param accelerator
		 *            the accelerator key
		 * @param op
		 *            the operation on list of strings
		 */
		public ChangeSelectedLinesAction(String key, String accelerator, UnaryOperator<List<String>> op) {
			super(flp, key, accelerator);
			this.op = op;
			setEnabled(false);
			observer.addCaretListener(e -> {
				if (documents.getCurrentDocument() == null) {
					setEnabled(false);
				} else {
					Caret caret = documents.getCurrentDocument().getTextComponent().getCaret();
					setEnabled(caret.getMark() != caret.getDot());
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea jta = documents.getCurrentDocument().getTextComponent();
			
			try {
				int l1 = jta.getLineOfOffset(jta.getCaret().getDot());
				int l2 = jta.getLineOfOffset(jta.getCaret().getMark());
				if (l1 > l2) {
					int l3 = l1;
					l1 = l2;
					l2 = l3;
				}
				List<String> lines = new ArrayList<>();
				for (int l = l1; l <= l2; l++) {
					lines.add(jta.getDocument().getText(
							jta.getLineStartOffset(l),
							jta.getLineEndOffset(l) - jta.getLineStartOffset(l)
					));
				}

				if (!lines.get(lines.size() - 1).endsWith("\n")) {
					String lastLine = lines.get(lines.size()-1);
					lines.remove(lines.size() - 1);
					lines.add(lastLine + "\n");
				}

				lines = op.apply(lines);
				jta.getDocument().remove(
						jta.getLineStartOffset(l1), 
						jta.getLineEndOffset(l2) - jta.getLineStartOffset(l1)
				);
				StringBuilder text = new StringBuilder();
				for (String line : lines) {
					text.append(line);
				}
				jta.getDocument().insertString(jta.getLineStartOffset(l1), text.toString(), null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Modifies the given string by making upper case letters lower case and vice
	 * versa.
	 */
	private UnaryOperator<String> toggleCase = e -> {
		char[] result = e.toCharArray();
		for (int i = 0; i < result.length; i++) {
			char c = result[i];
			if (Character.isLowerCase(c)) {
				result[i] = Character.toUpperCase(c);
			} else if (Character.isUpperCase(c)) {
				result[i] = Character.toLowerCase(c);
			}
		}
		return new String(result);
	};
	
	/**
	 * Localizable action that modifies currently selected text in some way.
	 */
	private class ChangeSelectedAction extends LocalizableAction {
		
		/** Modifes the given string in some way. */
		UnaryOperator<String> op;

		/**
		 * Instantiates a new action.
		 *
		 * @param key
		 *            the key for localization
		 * @param accelerator
		 *            the accelerator key
		 * @param op
		 *            the peration on strings
		 */
		public ChangeSelectedAction(String key, String accelerator, UnaryOperator<String> op) {
			super(flp, key, accelerator);
			this.op = op;
			setEnabled(false);
			observer.addCaretListener(e -> {
				if (documents.getCurrentDocument() == null) {
					setEnabled(false);
				} else {
					Caret caret = documents.getCurrentDocument().getTextComponent().getCaret();
					setEnabled(caret.getMark() != caret.getDot());
				}
			});
		}
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea jta = documents.getCurrentDocument().getTextComponent();
			
			int len = Math.abs(jta.getCaret().getDot() - jta.getCaret().getMark());
			int offset = Math.min(jta.getCaret().getDot(), jta.getCaret().getMark());

			try {
				String text = jta.getText(offset, len);
				text = op.apply(text);
				jta.getDocument().remove(offset, len);
				jta.getDocument().insertString(offset, text, null);
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
		}
		
	};
	
	/**
	 * Shows basic information about current document: number of characters, number
	 * of non-blank characters and number of lines.
	 */
	private final Action statusAction = new LocalizableAction(flp, "status", "control U") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int numOfCharacters = documents.getCurrentDocument().getTextComponent().getDocument()
					.getLength();
			int numOfNonBlankCharacters = 0;
			try {
				numOfNonBlankCharacters = documents.getCurrentDocument().getTextComponent()
						.getDocument().getText(0, numOfCharacters).replaceAll("\\s+", "").length();
			} catch(BadLocationException ex) {
				ex.printStackTrace();
			}
			int numOfLines = documents.getCurrentDocument().getTextComponent().getLineCount();
			JOptionPane.showMessageDialog(
					JNotepadPP.this,
					MessageFormat.format(
							flp.getString("statusOutput"),
							numOfCharacters,
							numOfNonBlankCharacters,
							numOfLines
					),
					flp.getString("Information"),
					JOptionPane.INFORMATION_MESSAGE
			);
		}
		
	};
	
	/** Copies the currently selected text. */
	private final Action copyAction = new LocalizableAction(flp, "copy", "control C") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			documents.getCurrentDocument().getTextComponent().copy();
		}
	};

	/** Cuts currently selected text. */
	private final Action cutAction = new LocalizableAction(flp, "cut", "control X") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			documents.getCurrentDocument().getTextComponent().cut();
		}
	};

	/** Pastes the text from the clipboard to the current document. */
	private final Action pasteAction = new LocalizableAction(flp, "paste", "control V") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			documents.getCurrentDocument().getTextComponent().paste();
		}
	};
	
	/**
	 * If there are any unsaved documents, asks the user about them, then exits the editor.
	 */
	private void exitApplication() {
		//TEMPORARY LIST OF DOCUMENTS BECAUSE THEY CAN'T BE REMOVED WHILE ITERATING
		List<SingleDocumentModel> tempDocuments = new ArrayList<>();
		for (SingleDocumentModel document : documents) {
			tempDocuments.add(document);
		}
		for (SingleDocumentModel document : tempDocuments) {
			if (!closeDocument(document)) return;
		}
		dispose();
		timer.stop();
	}

	/** If there are any unsaved documents, asks the user about them, then exits the editor.*/
	private final Action exitAction = new LocalizableAction(flp, "exitApplication", "control E") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exitApplication();
		}
	};
	
	/**
	 * If the document is unsaved, asks the user about it. Then, if the user didn't
	 * cancel, closes the given document and its tab.
	 *
	 * @param model
	 *            the document to close
	 * @return true, iff the document was closed
	 */
	private boolean closeDocument(SingleDocumentModel model) {
		if (model.isModified()) {
			int confirmation = JOptionPane.showConfirmDialog(
					JNotepadPP.this, 
					MessageFormat.format(
							flp.getString("confirmSaveBeforeClose"),
							model.getFilePath() == null ? 
									flp.getString("Untitled") :
										model.getFilePath().getFileName().toString()
					)
			);
			if (confirmation == 0) {
				if (!saveDocument(model, model.getFilePath())) return false;
			} else if (confirmation == 2) {
				return false;
			}
		}
		documents.closeDocument(model);
		return true;
	}
	
	/** Closes the current document, after asking the user if it was unsaved. */
	private final Action closeDocumentAction = new LocalizableAction(flp, "closeDocument", 
			"control W") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			closeDocument(documents.getCurrentDocument());
		}
	};
	
	/**
	 * Saves document to the disk. If the given path is null, asks the user where to
	 * save.
	 *
	 * @param model
	 *            the document to save
	 * @param filePath
	 *            the file path to save to, can be null
	 * @return true, if successfully saved
	 */
	private boolean saveDocument(SingleDocumentModel model, Path filePath) {
		if (filePath == null) {
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return false;
			}
			filePath = fc.getSelectedFile().toPath();
			if (Files.exists(filePath)) {
				int confirmation = JOptionPane.showConfirmDialog(
						JNotepadPP.this, 
						MessageFormat.format(
								flp.getString("confirmOverwrite"), 
								filePath.toString()
						)
				);
				if (confirmation != 0) return false;
			}
		}
		documents.saveDocument(model, filePath);
		updateTitle();
		return true;
	}
	
	/**
	 * Saves the current document. If it is untitled, first asks the user where to
	 * save.
	 */
	private final Action saveDocumentAction = new LocalizableAction(flp, "saveDocument", 
			"control S", "icons/save.png") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel currentDocument = documents.getCurrentDocument();
			saveDocument(currentDocument, currentDocument.getFilePath());
		}
	};
	
	/** Saves the current document after asking the user where to save. */
	private final Action saveDocumentAsAction = new LocalizableAction(flp, "saveDocumentAs", 
			"control shift S") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveDocument(documents.getCurrentDocument(), null);
		}
	};

	/** Creates a new blank document and switches to it. */
	private final Action newDocumentAction = new LocalizableAction(flp, "newDocument", "control N") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			documents.createNewDocument();
		}
	};
	
	/** Loads a chosen document from the disk and switches to it. */
	private final Action loadDocumentAction = new LocalizableAction(flp, "loadDocument",
			"control O") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			Path filePath = fc.getSelectedFile().toPath();
			documents.loadDocument(filePath);
		}
	};

}
