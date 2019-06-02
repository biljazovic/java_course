package hr.fer.zemris.java.hw16.jvdraw.drawing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;

/**
 * Main component which shows all objects from the {@link DrawingModel}.
 * 
 * @author Bruno Iljazović
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The drawing model. */
	DrawingModel drawingModel;
	
	/** The parent component. */
	JVDraw parent;
	
	/**
	 * Instantiates a new drawing canvas.
	 *
	 * @param parent the parent component
	 * @param drawingModel the drawing model
	 */
	public JDrawingCanvas(JVDraw parent, DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		this.parent = parent;
		drawingModel.addDrawingModelListener(this);
		addMouseListener(forwardMouseListener);
		addMouseMotionListener(forwardMouseMotionListener);
		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D)g);
		for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
			drawingModel.getObject(i).accept(painter);
		}
		parent.getCurrentState().paint((Graphics2D)g);
	}
	
	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	/** The mouse listener used for creating objects. */
	private MouseListener forwardMouseListener = new MouseAdapter() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			parent.getCurrentState().mouseReleased(e);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			parent.getCurrentState().mousePressed(e);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			parent.getCurrentState().mouseClicked(e);
		}
	};
	
	/** The mouse motion listener used for creating objects. */
	private MouseMotionListener forwardMouseMotionListener = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			parent.getCurrentState().mouseMoved(e);
			repaint();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			parent.getCurrentState().mouseDragged(e);
		}
	};

}
