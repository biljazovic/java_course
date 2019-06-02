package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Provides utility functions for the buttons.
 *
 * @author Bruno Iljazović
 */
public class Util {
	
	/** Color of the button background */
	public static final Color LIGHT_BLUE = new Color(120, 168, 216);
	
	/** Font of the button name */
	public static final Font SANS_SERIF_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	
	/**
	 * Customizes button looks.
	 *
	 * @param button the button to be customized.
	 */
	public static void customizeButtonLooks(JButton button) {
		button.setBackground(LIGHT_BLUE);
		button.setFont(SANS_SERIF_BOLD);
		button.setMargin(new Insets(0, 0, 0, 0));
	}
	
	/**
	 * Checks if the given number is finite, i.e. if {@link Double#isFinite} returns 
	 * true, and if it isn't, creates a dialog with the appropriate message.
	 *
	 * @param d the number to check
	 * @param parent container, used for creating the dialog
	 * @return true, iff the given number is finite
	 */
	public static boolean checkIfFinite(double d, Container parent) {
		if (!Double.isFinite(d)) {
			JOptionPane.showMessageDialog(
					parent,
					"Result of operation is " + d + "!", 
					"Computation Error", 
					JOptionPane.ERROR_MESSAGE
			);
			return false;
		}
		return true;
	}
}
