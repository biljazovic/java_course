package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Binary operator button. Clicking the button performs the pending operation
 * with current value and active operand as operands and puts the given operator
 * as the new pending operation.
 * 
 * @author Bruno Iljazović
 */
public class BinaryOperatorButton extends JButton {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new binary operator button.
	 *
	 * @param model the calculator model
	 * @param op the operation to be added as pending
	 * @param opInv the inverse operation
	 * @param name the name of the button
	 * @param inv the Inv checkbox
	 */
	public BinaryOperatorButton(CalcModel model, DoubleBinaryOperator op,
			DoubleBinaryOperator opInv, String name, JCheckBox inv) {
		addActionListener(e -> {
			DoubleBinaryOperator opPending = model.getPendingBinaryOperation();
			if (opPending != null) {
				double result = opPending.applyAsDouble(model.getActiveOperand(), model.getValue());

				if (!Util.checkIfFinite(result, getParent())) return;

				model.setActiveOperand(result);
			} else {
				model.setActiveOperand(model.getValue());
			}

			model.clear();
			
			if (inv != null && inv.isSelected()) {
				model.setPendingBinaryOperation(opInv);
			} else {
				model.setPendingBinaryOperation(op);
			}
		});
		setText(name);
		Util.customizeButtonLooks(this);
	}
	
	/**
	 * Instantiates a new binary operator button.
	 *
	 * @param model the calculator model
	 * @param op the operation to be added as pending
	 * @param name the name of the button
	 */
	public BinaryOperatorButton(CalcModel model, DoubleBinaryOperator op, String name) {
		this(model, op, op, name, null);
	}
}
