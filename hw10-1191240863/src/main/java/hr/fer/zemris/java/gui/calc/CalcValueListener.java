package hr.fer.zemris.java.gui.calc;

/**
 * Calculator value listener. It is notified when the calculator value changes.
 * 
 * @author Bruno Iljazović
 */
public interface CalcValueListener {

	/**
	 * This method is called when the current value in {@link CalcModel} changes.
	 *
	 * @param model
	 *            the model
	 */
	void valueChanged(CalcModel model);
}