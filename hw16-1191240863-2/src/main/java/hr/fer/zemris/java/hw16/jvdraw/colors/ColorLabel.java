package hr.fer.zemris.java.hw16.jvdraw.colors;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * Label that shows current foreground and background colors in rgb form.
 * 
 * @author Bruno Iljazović
 */
public class ColorLabel extends JLabel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The fg color provider. */
	IColorProvider fgColorProvider;

	/** The bg color provider. */
	IColorProvider bgColorProvider;

	/**
	 * Instantiates a new color label.
	 *
	 * @param fgColorProvider the fg color provider
	 * @param bgColorProvider the bg color provider
	 */
	public ColorLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
		ColorChangeListener l = (source, oldColor, newColor) -> {
			updateText();
		};
		fgColorProvider.addColorChangeListener(l);
		bgColorProvider.addColorChangeListener(l);
		updateText();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(50, 20);
	}

	/**
	 * Updates text.
	 */
	private void updateText() {
		Color fgColor = fgColorProvider.getCurrentColor();
		Color bgColor = bgColorProvider.getCurrentColor();
		setText(String.format(
				"Foreground color: (%d, %d, %d), Background color: (%d, %d, %d).", 
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()
		));
	}
}
