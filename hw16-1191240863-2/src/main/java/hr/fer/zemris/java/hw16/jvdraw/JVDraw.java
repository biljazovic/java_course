/*
 * 
 */
package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.colors.ColorLabel;
import hr.fer.zemris.java.hw16.jvdraw.colors.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.drawing.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;
import hr.fer.zemris.java.hw16.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.Tool;

// TODO: Auto-generated Javadoc
/**
 * A program that lets user draw some objects on the screen. Currently user can
 * draw lines, empty circles and filled circles. User can change foreground and
 * background colors, delete current objects, change their order, export the
 * current image in a chosen format, save the current objects in the .jvd
 * format, and opening .jvd files.
 * 
 * @author Bruno Iljazović
 */
public class JVDraw extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The current state. */
	private Tool currentState;
	
	/** True, if the current objects are unsaved to .jvd file */
	private boolean unsaved = false;
	
	/** The line tool. */
	private Tool lineTool;
	
	/** The circle tool. */
	private Tool circleTool;
	
	/** The circle tool. */
	private Tool fCircleTool;
	
	/** The drawing model. */
	private DrawingModel drawingModel;
	
	/** The file chooser. */
	private JFileChooser fc = new JFileChooser();
	
	/** The opened JVD file, or null if there aren't any. */
	private Path openedJVD;
	
	/**
	 * Gets the current state.
	 *
	 * @return the current state
	 */
	public Tool getCurrentState() {
		return currentState;
	}

	/**
	 * Instantiates a new JVDraw program.
	 */
	public JVDraw() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(100, 100);
		setTitle("JVDraw");
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
	 * Initializes the gui.
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
		drawingModel = new DrawingModelImpl();
		drawingModel.addDrawingModelListener(new DrawingModelListener() {
			
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				unsaved = true;
			}
			
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				unsaved = true;
			}
			
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				unsaved = true;
			}
		});
		createToolBar();
		getContentPane().add(new JDrawingCanvas(this, drawingModel), BorderLayout.CENTER);
		createList();
		createMenuBar();
	}
	
	/**
	 * Parses the JVD file into a list of geometrical objects.
	 * Format:<br>
	 * 
	 * LINE x0 y0 x1 y1 red green blue<br>
	 * CIRCLE centerx centery radius red green blue<br>
	 * FCIRCLE centerx centery radius red green blue red green blue<br>
	 * 
	 * @param filePath the file path
	 * @return the list of objects.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	List<GeometricalObject> parseJVDFile(Path filePath) throws IOException {
		List<GeometricalObject> objects = new ArrayList<>();
		for (String line : Files.readAllLines(filePath)) {
			line = line.trim();
			if (line.isEmpty()) continue;
			String[] lineArr = line.split("[ ]+");
			lineArr[0] = lineArr[0].toUpperCase();
			switch(lineArr[0]) {
			case "LINE":
				if (lineArr.length != 8) throw new IllegalArgumentException();
				objects.add(Line.fromJVDEntry(lineArr));
				break;
			case "CIRCLE":
				if (lineArr.length != 7) throw new IllegalArgumentException();
				objects.add(Circle.fromJVDEntry(lineArr));
				break;
			case "FCIRCLE":
				if (lineArr.length != 10) throw new IllegalArgumentException();
				objects.add(FilledCircle.fromJVDEntry(lineArr));
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
		return objects;
	}
	
	/**
	 * Creates the menu bar.
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAsAction));
		fileMenu.add(new JMenuItem(exportDocumentAction));
		fileMenu.add(new JMenuItem(exitAction));
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	
	/**
	 * Exits the application, but asks the user to save if there are unsaved changes.
	 */
	private void exitApplication() {
		if (unsaved) {
			int save = JOptionPane.showConfirmDialog(
					this, 
					"You have unsaved changes, would you like to save?",
					"Save",
					JOptionPane.YES_NO_CANCEL_OPTION
			);
			switch (save) {
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.OK_OPTION:
				if (saveAsJVD(openedJVD) == null) return;
				break;
			case JOptionPane.NO_OPTION:
				break;
			}
		}
		dispose();
	}
	
	/**
	 * Exit action.
	 */
	private final AbstractAction exitAction = new AbstractAction("Exit") {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exitApplication();
		}
	};
	
	/** save document action. */
	private final AbstractAction saveDocumentAction = new AbstractAction("Save") {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			openedJVD = saveAsJVD(openedJVD);
		}

	};
	
	/** save document as action. */
	private final AbstractAction saveDocumentAsAction = new AbstractAction("Save As") {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			openedJVD = saveAsJVD(null);
		}

	};

	/** open document action. */
	private final AbstractAction openDocumentAction = new AbstractAction("Open") {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fc.setFileFilter(new FileNameExtensionFilter("JVD FILE", "jvd"));
			if (fc.showOpenDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				Path filePath = fc.getSelectedFile().toPath();
				if (!Files.isRegularFile(filePath)) {
					JOptionPane.showMessageDialog(
							JVDraw.this,
							"Not a valid file.",
							"Error",
							JOptionPane.ERROR_MESSAGE
					);
					return;
				}
				List<GeometricalObject> objects = null;
				try {
					objects = parseJVDFile(filePath);
				} catch(IOException | IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(
							JVDraw.this,
							"Not a valid jvd file.",
							"Error",
							JOptionPane.ERROR_MESSAGE
					);
					return;
				}
				for (int i = drawingModel.getSize()-1; i >= 0; i--) {
					drawingModel.remove(drawingModel.getObject(i));
				}
				for (GeometricalObject o : objects) {
					drawingModel.add(o);
				}
				openedJVD = filePath;
				unsaved = false;
			}
		}
	};
	
	/** export document action. */
	private final AbstractAction exportDocumentAction = new AbstractAction("Export") {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (drawingModel.getSize() == 0) {
				JOptionPane.showMessageDialog(
						JVDraw.this,
						"Nothing to export.",
						"Error",
						JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			fc.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "gif"));
			if (fc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				Path filePath = fc.getSelectedFile().toPath();
				if (!filePath.toString().endsWith(".png") && !filePath.toString().endsWith(".jpg") 
						&& !filePath.toString().endsWith(".gif")) {
					JOptionPane.showMessageDialog(
							JVDraw.this,
							"Invalid file format.",
							"Error",
							JOptionPane.ERROR_MESSAGE
					);
					return;
				}
				String extension = filePath.toString().substring(filePath.toString().length() - 3);
				GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
				for (int i = 0; i < drawingModel.getSize(); i++) {
					drawingModel.getObject(i).accept(bbcalc);
				}
				Rectangle box = bbcalc.getBoundingBox();
				BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = image.createGraphics();
				g.translate(-box.x, -box.y);
				GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
				for (int i = 0; i < drawingModel.getSize(); i++) {
					drawingModel.getObject(i).accept(painter);
				}
				g.dispose();
				try {
					if (!ImageIO.write(image, extension, filePath.toFile())) throw new IOException();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(
							JVDraw.this,
							"Failed to export.",
							"Error",
							JOptionPane.ERROR_MESSAGE
					);
					return;
				}
				JOptionPane.showMessageDialog(
						JVDraw.this,
						"Exported to " + filePath.toString(),
						"Success",
						JOptionPane.INFORMATION_MESSAGE
				);
			}
		}
	};

	/**
	 * Saves current objects as .jvd file. See {@link JVDraw#parseJVDFile}.
	 *
	 * @param filePath
	 *            the file path, or null if user should choose the file path.
	 * @return the path of created file.
	 */
	private Path saveAsJVD(Path filePath) {
		if (filePath == null) {
			fc.setFileFilter(new FileNameExtensionFilter("JVD FILE", "jvd"));
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				filePath = fc.getSelectedFile().toPath();
				if (!filePath.toString().endsWith(".jvd")) {
					filePath = filePath.resolveSibling(filePath.getFileName() + ".jvd");
				}
				if (Files.exists(filePath)) {
					int confirmation = JOptionPane.showConfirmDialog(
							this, 
							"Do you want to overwrite this file?"
					);
					if (confirmation != 0) return null;
				}
			} else {
				return null;
			}
		}
		List<String> lines = new ArrayList<>();
		for (int i = 0; i < drawingModel.getSize(); i++) {
			lines.add(drawingModel.getObject(i).toJVDEntry());
		}
		try {
			Files.write(filePath, lines);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					this,
					"Failed to save to file",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
			return null;
		}
		unsaved = false;
		return filePath;
	}
	
	/** The delete object action. */
	private final AbstractAction delAction = new AbstractAction() {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JList<GeometricalObject> list = (JList<GeometricalObject>)e.getSource();
			drawingModel.remove(list.getSelectedValue());
		}
	};

	/** Shifts the object by one in the list. */
	private final AbstractAction minusAction = new AbstractAction() {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JList<GeometricalObject> list = (JList<GeometricalObject>)e.getSource();
			int index = list.getSelectedIndex();
			if (index + 1 <= list.getLastVisibleIndex()) {
				drawingModel.changeOrder(list.getSelectedValue(), 1);
				list.setSelectedIndex(index + 1);
			}
		}
	};

	/** Shifts the object by one in the list, backwards. */
	private final AbstractAction plusAction = new AbstractAction() {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JList<GeometricalObject> list = (JList<GeometricalObject>)e.getSource();
			int index = list.getSelectedIndex();
			if (index - 1 >= 0) {
				drawingModel.changeOrder(list.getSelectedValue(), -1);
				list.setSelectedIndex(index - 1);
			}
		}
	};

	/**
	 * Creates the list of objects.
	 */
	private void createList() {
		JList<GeometricalObject> list = new JList<>(new DrawingObjectListModel(drawingModel));
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(300, 0));
		InputMap im = list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = list.getActionMap();
		im.put(KeyStroke.getKeyStroke("DELETE"), "del");
		am.put("del", delAction);
		im.put(KeyStroke.getKeyStroke("typed +"), "plus");
		am.put("plus", plusAction);
		im.put(KeyStroke.getKeyStroke("typed -"), "minus");
		am.put("minus", minusAction);

		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
					if (r != null && r.contains(e.getPoint())) {
						GeometricalObjectEditor editor = ((GeometricalObject) list
								.getSelectedValue()).createGeometricalObjectEditor();
						if (JOptionPane.showConfirmDialog(
								JVDraw.this, 
								editor, 
								"Edit an Object", 
								JOptionPane.OK_CANCEL_OPTION
						) == JOptionPane.OK_OPTION) {
							try {
								editor.checkEditing();
								editor.acceptEditing();
							} catch(NumberFormatException ex) {
								JOptionPane.showMessageDialog(
										JVDraw.this,
										"Illegal coordinate values"
								);
							}
						}
					}
				}
			}
		});
		getContentPane().add(listScroller, BorderLayout.LINE_END);
	}

	/**
	 * Creates the tool bar.
	 */
	private void createToolBar() {
		JToolBar toolBar = new JToolBar("Toolbar");
		
		JColorArea fgColorArea = new JColorArea(Color.red);
		JColorArea bgColorArea = new JColorArea(Color.blue);
		
		toolBar.add(fgColorArea);
		toolBar.add(bgColorArea);
		
		lineTool = new LineTool(drawingModel, fgColorArea, bgColorArea);
		circleTool = new CircleTool(drawingModel, fgColorArea, bgColorArea);
		fCircleTool = new FilledCircleTool(drawingModel, fgColorArea, bgColorArea);
		currentState = lineTool;
		
		ButtonGroup bGroup = new ButtonGroup();

		JToggleButton lineButton = new JToggleButton("Line");
		lineButton.addActionListener(e -> currentState = lineTool);
		JToggleButton circleButton = new JToggleButton("Circle");
		circleButton.addActionListener(e -> currentState = circleTool);
		JToggleButton fCircleButton = new JToggleButton("Filled Circle");
		fCircleButton.addActionListener(e -> currentState = fCircleTool);
		
		lineButton.setSelected(true);
		
		bGroup.add(lineButton);
		bGroup.add(circleButton);
		bGroup.add(fCircleButton);
		
		toolBar.add(lineButton); 
		toolBar.add(circleButton); 
		toolBar.add(fCircleButton);
		
		getContentPane().add(new ColorLabel(fgColorArea, bgColorArea), BorderLayout.PAGE_END);
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args not used here.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			(new JVDraw()).setVisible(true);
		});
	}

}
