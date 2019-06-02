package hr.fer.zemris.java.gui.calc;

import java.awt.Container;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.calc.buttons.BinaryOperatorButton;
import hr.fer.zemris.java.gui.calc.buttons.DigitButton;
import hr.fer.zemris.java.gui.calc.buttons.EqualsButton;
import hr.fer.zemris.java.gui.calc.buttons.SingleActionButton;
import hr.fer.zemris.java.gui.calc.buttons.UnaryOperatorButton;
import hr.fer.zemris.java.gui.calc.buttons.Util;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * Main program that starts the calculator gui. User can enter the numbers by
 * clicking on the digits buttons and perform operations by clicking operation
 * buttons. Input by keyboard is not supported. Checking the Inv checkbox
 * 
 * @author Bruno Iljazović
 */
public class Calculator extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new calculator.
	 */
	public Calculator() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Calculator");
		setLocation(100, 100);
		setSize(700, 500);
		initGUI();
	}
	
	/**
	 * Initializes the gui
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(10));
		
		CalcModel model = new CalcModelImpl();
		CalcDisplay display = new CalcDisplay();

		model.addCalcValueListener(display);

		JCheckBox inv = new JCheckBox("Inv");
		inv.setBackground(Util.LIGHT_BLUE);
		inv.setFont(Util.SANS_SERIF_BOLD);
		inv.setHorizontalAlignment(SwingConstants.CENTER);
		
		addDigitButtons(cp, model);
		addBinaryOperators(cp, model, inv);
		addUnaryOperators(cp, model, inv);
		addSingleActionButtons(cp, model);
		cp.add(new EqualsButton(model), "1,6");
		cp.add(inv, "5,7");
		cp.add(display, "1,1");
	}

	/**
	 * This method is called when the program is run.
	 *
	 * @param args the command line arguments, not used here
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Calculator calculator = new Calculator();
				calculator.setVisible(true);
			}
		});
	}

	/**
	 * Adds the single action buttons to the given container.
	 *
	 * @param cp the container
	 * @param model the calculator model
	 */
	private static void addSingleActionButtons(Container cp, CalcModel model) {
		cp.add(new SingleActionButton(model, CalcModel::clear, "clr"), "1,7");
		cp.add(new SingleActionButton(model, CalcModel::insertDecimalPoint, "."), "5,5");
		cp.add(new SingleActionButton(model, CalcModel::swapSign, "+/-"), "5,4");
		cp.add(new SingleActionButton(model, CalcModel::clearAll, "res"), "2,7");

		Stack<Double> stack = new Stack<>();

		//stack operations
		cp.add(new SingleActionButton(model, md -> stack.push(md.getValue()), "push"), "3,7");
		cp.add(new SingleActionButton(model, md -> {
			if (stack.empty()) {
				JOptionPane.showMessageDialog(
						cp, 
						"Stack is empty!", 
						"Empty stack", 
						JOptionPane.ERROR_MESSAGE
				);
			} else {
				md.setValue(stack.pop());
			}
		}, "pop"), "4,7");
	}
	
	/**
	 * Adds the digit buttons to the given container
	 *
	 * @param cp the container
	 * @param model the calculator model
	 */
	private static void addDigitButtons(Container cp, CalcModel model) {
		cp.add(new DigitButton(model, 1), "4,3");
		cp.add(new DigitButton(model, 2), "4,4");
		cp.add(new DigitButton(model, 3), "4,5");
		cp.add(new DigitButton(model, 4), "3,3");
		cp.add(new DigitButton(model, 5), "3,4");
		cp.add(new DigitButton(model, 6), "3,5");
		cp.add(new DigitButton(model, 7), "2,3");
		cp.add(new DigitButton(model, 8), "2,4");
		cp.add(new DigitButton(model, 9), "2,5");
		cp.add(new DigitButton(model, 0), "5,3");
	}
	
	/**
	 * Adds the binary operators to the given container.
	 *
	 * @param cp the container
	 * @param model the calculator model
	 * @param inv the Inv checkbox
	 */
	private static void addBinaryOperators(Container cp, CalcModel model, JCheckBox inv) {
		cp.add(new BinaryOperatorButton(model, (e, f) -> e + f, "+"), "5,6");
		cp.add(new BinaryOperatorButton(model, (e, f) -> e - f, "-"), "4,6");
		cp.add(new BinaryOperatorButton(model, (e, f) -> e / f, "/"), "2,6");
		cp.add(new BinaryOperatorButton(model, (e, f) -> e * f, "*"), "3,6");
		cp.add(new BinaryOperatorButton(model, (e,f)->Math.pow(e,f), (e,f)->Math.pow(e,1/f), "x^n", inv), 
				"5,1");
	}
	
	/**
	 * Adds the unary operators to the given container.
	 *
	 * @param cp the container
	 * @param model the calculator model
	 * @param inv the Inv checkbox
	 */
	private static void addUnaryOperators(Container cp, CalcModel model, JCheckBox inv) {
		cp.add(new UnaryOperatorButton(model, Math::sin, Math::asin, "sin", inv), "2,2");		
		cp.add(new UnaryOperatorButton(model, Math::cos, Math::acos, "cos", inv), "3,2");		
		cp.add(new UnaryOperatorButton(model, Math::tan, Math::atan, "tan", inv), "4,2");		
		cp.add(new UnaryOperatorButton(model, e->1/Math.tan(e), e->Math.atan(1/e), "ctg", inv), "5,2");		
		cp.add(new UnaryOperatorButton(model, e->1/e, e->1/e, "1/x", inv), "2,1");		
		cp.add(new UnaryOperatorButton(model, Math::log10, e->Math.pow(10, e), "log", inv), "3,1");		
		cp.add(new UnaryOperatorButton(model, Math::log, Math::exp, "ln", inv), "4,1");		
	}
}
