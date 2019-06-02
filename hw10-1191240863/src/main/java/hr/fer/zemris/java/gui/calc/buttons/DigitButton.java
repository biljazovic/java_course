package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Digit button. Clicking this button appends the corresponding digit to the
 * current value in the calculator.
 * 
 * @author Bruno Iljazović
 */
public class DigitButton extends JButton {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new digit button.
	 *
	 * @param model the calculator model
	 * @param digit the digit to be appended
	 */
	public DigitButton(CalcModel model, int digit) {
		addActionListener(e -> {
			model.insertDigit(digit);
		});
		setText(Integer.toString(digit));
		Util.customizeButtonLooks(this);
	}
}
