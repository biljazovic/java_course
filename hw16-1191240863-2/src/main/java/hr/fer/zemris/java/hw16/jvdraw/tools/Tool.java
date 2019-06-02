package hr.fer.zemris.java.hw16.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Tool for painting unfinished objects.
 * 
 * @author Bruno Iljazović
 */
public interface Tool {

	/**
	 * Mouse pressed.
	 *
	 * @param e the event
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Mouse released.
	 *
	 * @param e the event
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Mouse clicked.
	 *
	 * @param e the event
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Mouse moved.
	 *
	 * @param e the event
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Mouse dragged.
	 *
	 * @param e the event
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Paints the unfinished object, if there is any.
	 *
	 * @param g2d the graphics object.
	 */
	public void paint(Graphics2D g2d);
}
