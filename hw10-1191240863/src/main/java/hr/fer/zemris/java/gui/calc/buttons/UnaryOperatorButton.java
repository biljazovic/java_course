package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Unary Operator button. Clicking this button performs the given unary operation
 * on the current value in the calculator. Result is displayed, and the active
 * operand or the pending operation are left unchanged.
 * 
 * @author Bruno Iljazović
 */
public class UnaryOperatorButton extends JButton {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unary operator button.
	 *
	 * @param model the calculator model
	 * @param op the operation to be performed when clicking the button
	 * @param opInv the inverse operation
	 * @param name the name of the button
	 * @param inv the Inv checkbox
	 */
	public UnaryOperatorButton(CalcModel model, DoubleUnaryOperator op, DoubleUnaryOperator opInv,
			String name, JCheckBox inv) {
		addActionListener(e -> {
			double result;
			
			if (inv.isSelected()) {
				result = opInv.applyAsDouble(model.getValue());
			} else {
				result = op.applyAsDouble(model.getValue());
			}

			if (!Util.checkIfFinite(result, getParent())) return;
			
			model.setValue(result);
		});
		setText(name);
		Util.customizeButtonLooks(this);
	}
}
