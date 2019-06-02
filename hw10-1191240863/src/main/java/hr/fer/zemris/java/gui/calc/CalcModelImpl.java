package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * Represents current state of the calculator.
 * 
 * @author Bruno Iljazović
 */
public class CalcModelImpl implements CalcModel {
	
	/** Character used for decimal separator. */
	private final char DECIMAL_POINT = '.';
	
	/** The current value. */
	private String currentValue;
	
	/** The active operand. */
	private double activeOperand;
	
	/** The pending operation. */
	private DoubleBinaryOperator pendingOperation;
	
	/** The value listeners. */
	List<CalcValueListener> listeners = new ArrayList<>();
	
	/** true, if the decimal point is present in the current value */
	private boolean decimalPoint;
	
	/** true, if the active operand is set */
	private boolean activeOperandSet;
	
	/**
	 * Notifies value listeners that the value has changed.
	 */
	private void notifyListeners() {
		for (CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if (listeners.contains(l)) return;
		listeners.add(Objects.requireNonNull(l, "Listener was null."));
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		return currentValue == null ? 0.0 : Double.parseDouble(currentValue);
	}

	@Override
	public void setValue(double value) {
		if (!Double.isFinite(value)) return;
		currentValue = Double.toString(value);
		decimalPoint = currentValue.indexOf(DECIMAL_POINT) > -1;
		notifyListeners();
	}

	@Override
	public void clear() {
		currentValue = null;
		decimalPoint = false;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		currentValue = null;
		decimalPoint = false;
		activeOperandSet = false;
		pendingOperation = null;
		notifyListeners();
	}

	@Override
	public void swapSign() {
		if (currentValue == null) return;
		if (currentValue.charAt(0) == '-') {
			currentValue = currentValue.substring(1);
		} else {
			currentValue = "-" + currentValue;
		}
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() {
		if (decimalPoint) return;
		if (currentValue == null) currentValue = "0";
		currentValue += DECIMAL_POINT;
		decimalPoint = true;
		notifyListeners();
	}
	
	@Override
	public String toString() {
		return currentValue == null ? "0" : currentValue;
	}

	@Override
	public void insertDigit(int digit) {
		if (currentValue == null) {
			currentValue = "0";
		}

		double newValueDouble = Double.parseDouble(currentValue + digit);
		double currentValueDouble = Double.parseDouble(currentValue);

		if (!Double.isFinite(newValueDouble)) return;

		if (currentValueDouble == 0 && !decimalPoint) {
			currentValue = Integer.toString(digit);
		} else {
			currentValue += digit;
		}
		
		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperandSet;
	}

	@Override
	public double getActiveOperand() {
		if (!activeOperandSet) throw new IllegalStateException("Active operand is not set.");
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		activeOperandSet = true;
	}

	@Override
	public void clearActiveOperand() {
		activeOperandSet = false;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingOperation = op;
	}

}
