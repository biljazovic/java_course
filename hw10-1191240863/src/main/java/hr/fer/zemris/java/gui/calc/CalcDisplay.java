package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

/**
 * Represents calculator display. Displays current value in the calculator.
 * 
 * @author Bruno Iljazović
 */
public class CalcDisplay extends JLabel implements CalcValueListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The font size. */
	private final int FONT_SIZE = 30;
	
	/** The background color. */
	private final Color BACKGROUND_COLOR = new Color(255, 216, 24);

	/**
	 * Instantiates a new display.
	 */
	public CalcDisplay() {
		setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
		setHorizontalAlignment(SwingConstants.RIGHT);
		setVerticalAlignment(SwingConstants.CENTER);
		setText("0");
		setOpaque(true);
		setBackground(BACKGROUND_COLOR);
		setBorder(new CompoundBorder(
				BorderFactory.createLineBorder(Color.BLUE, 1),
				BorderFactory.createEmptyBorder(0, 0, 0, 10)
		));
		
	}
	
	@Override
	public void valueChanged(CalcModel model) {
		setText(model.toString());
	}

}
