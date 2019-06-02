package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * Equals Button. Clicking this button ensures that the pending operation is
 * evaluated. Result of the possible operation is displayed.
 * 
 * @author Bruno Iljazović
 */
public class EqualsButton extends JButton {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new equals button.
	 *
	 * @param model the calculator model
	 */
	public EqualsButton(CalcModel model) {
		addActionListener(e -> {
			DoubleBinaryOperator opPending = model.getPendingBinaryOperation();
			if (opPending != null) {
				double result = opPending.applyAsDouble(model.getActiveOperand(), model.getValue());

				if (!Util.checkIfFinite(result, getParent())) return;

				model.clearActiveOperand();
				model.setValue(result);

				model.setPendingBinaryOperation(null);
			} 
		});
		setText("=");
		Util.customizeButtonLooks(this);
	}
}
