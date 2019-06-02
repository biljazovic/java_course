package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.collections.EmptyStackException;

/**
 * This program takes exactly one argument from the command line. If more or less 
 * arguments are provided, program ends. 
 * 
 * <p> Argument should be postfix expression that consists of integers, spaces and 
 * operands '/', '*', '-', '+', '%'. '/' and '%' produce integers results, so 
 * "9 / 4" would evaluate to 2.
 * 
 * <p> If the expression is invalid, program ends. For valid expressions program outputs
 * evaluation of the expression.
 * 
 * <p> Division by zero ends the program and writes appropriate message to the user.
 * 
 * @author Bruno Iljazović
 */
public class StackDemo {

	/**
	 * Main method which is called when the program is run.
	 * 
	 * @param args Command line arguments. There should be only one here.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Number of arguments should be 1!");
			return;
		}

		String[] expression = args[0].split("\\s+");
		ObjectStack stack = new ObjectStack();
		final String operators = "+-%/*";
		
		for (String element : expression) {
			if (element.length() == 1 && operators.contains(element)) {
				int firstOperand, secondOperand;
				try {
					secondOperand = (Integer)stack.pop();
					firstOperand = (Integer)stack.pop();
				} catch(EmptyStackException ex) {
					System.err.println("Expression is invalid.");
					return;
				}
				
				try {
					switch (element) {
					case "+":
						firstOperand += secondOperand;
						break;
					case "*":
						firstOperand *= secondOperand;
						break;
					case "/":
						firstOperand /= secondOperand;
						break;
					case "%":
						firstOperand %= secondOperand;
						break;
					case "-":
						firstOperand -= secondOperand;
						break;
					}
				} catch (ArithmeticException ex) {
					System.err.println("Divison by zero is not possible.");
					return;
				}
				
				stack.push(Integer.valueOf(firstOperand));
			}
			else {
				try {
					stack.push(Integer.valueOf(Integer.parseInt(element)));
				} catch (NumberFormatException ex) {
					System.err.println("Operands should be integers. '" + element + "' is not.");
					return;
				}
			}
		}
		
		if (stack.size() != 1) {
			System.err.println("Expression is invalid.");
			return;
		}
				
		System.out.println("Expression evaluates to " + stack.peek() + ".");
	}
}
