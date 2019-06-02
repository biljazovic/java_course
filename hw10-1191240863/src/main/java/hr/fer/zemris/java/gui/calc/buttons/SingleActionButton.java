package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.Consumer;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Single action button. Clicking this button performs the given action on the
 * given {@link CalcModel}
 * 
 * @author Bruno Iljazović
 */
public class SingleActionButton extends JButton {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new single action button.
	 *
	 * @param model the calculator model
	 * @param singleAction the action to be performed when clicking the button
	 * @param name the name of the button
	 */
	public SingleActionButton(CalcModel model, Consumer<CalcModel> singleAction, String name) {
		addActionListener(e -> {
			singleAction.accept(model);
		});
		setText(name);
		Util.customizeButtonLooks(this);
	}
}
