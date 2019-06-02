package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

/**
 * Represents current calculator state. 
 * 
 * @author Bruno Iljazović
 */
public interface CalcModel {

	/**
	 * Adds the value listener.
	 *
	 * @param l the listener to add
	 */
	void addCalcValueListener(CalcValueListener l);
	
	/**
	 * Removes the value listener.
	 *
	 * @param l the listener to remove
	 */
	void removeCalcValueListener(CalcValueListener l);
	
	/**
	 * Returns string representation of value in calculator.
	 *
	 * @return the string representation of current value
	 */
	String toString();
	
	/**
	 * Gets the value in calculator.
	 *
	 * @return the current value
	 */
	double getValue();
	
	/**
	 * Sets the value in calculator.
	 *
	 * @param value the new value
	 */
	void setValue(double value);
	
	/**
	 * Clears only the current value, keeping active operand and
	 * pending operation unchanged.
	 */
	void clear();
	
	/**
	 * Clears current value, active operand and the pending operation.
	 */
	void clearAll();
	
	/**
	 * Swaps sign of the current value in calculator.
	 */
	void swapSign();
	
	/**
	 * Inserts decimal point, if it not already exists.
	 */
	void insertDecimalPoint();
	
	/**
	 * Appends given digit to the current value in calculator.
	 * If appending this digit would make the value too big (for double)
	 * value remains unchanged.
	 *
	 * @param digit the new digit to be appended.
	 */
	void insertDigit(int digit);
	
	/**
	 * Checks if the active operand is set.
	 *
	 * @return true, iff the active operand is set
	 */
	boolean isActiveOperandSet();
	
	/**
	 * Gets the active operand.
	 *
	 * @return the active operand
	 */
	double getActiveOperand();
	
	/**
	 * Sets the active operand.
	 *
	 * @param activeOperand the new active operand
	 */
	void setActiveOperand(double activeOperand);
	
	/**
	 * Clear active operand.
	 */
	void clearActiveOperand();
	
	/**
	 * Gets the pending binary operation.
	 *
	 * @return the pending binary operation
	 */
	DoubleBinaryOperator getPendingBinaryOperation();
	
	/**
	 * Sets the pending binary operation.
	 *
	 * @param op the new pending binary operation
	 */
	void setPendingBinaryOperation(DoubleBinaryOperator op);
}