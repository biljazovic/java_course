package hr.fer.zemris.java.hw16.jvdraw.colors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Shows currently selected color by painting itself in it.
 * 
 * @author Bruno Iljazović
 */
public class JColorArea extends JComponent implements IColorProvider {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The selected color. */
	Color selectedColor;
	
	/** The listeners. */
	List<ColorChangeListener> listeners;
	
	/**
	 * Instantiates a new color area.
	 *
	 * @param defaultColor the initial color.
	 */
	public JColorArea(Color defaultColor) {
		listeners = new ArrayList<>();
		selectedColor = defaultColor;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(
						getParent().getParent(), "Select a color", selectedColor);
				if (newColor != null && !newColor.equals(selectedColor)) {
					Color oldColor = selectedColor;
					selectedColor = newColor;
					for (ColorChangeListener l : listeners) {
						l.newColorSelected(JColorArea.this, oldColor, newColor);
					}
					repaint();
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect(2, 2, getWidth()-2, getHeight()-2);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		if (listeners.contains(l)) return;
		listeners.add(Objects.requireNonNull(l, "Listener was null"));
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
}
