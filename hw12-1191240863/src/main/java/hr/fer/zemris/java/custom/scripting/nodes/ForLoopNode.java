package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * This node is generated when a FOR tag is encountered. It must have 3 or 4 elements and first
 * element must be ElementVariable instance. 
 * 
 * @author bruno
 */
public class ForLoopNode extends Node {
	

	/** The variable. */
	private ElementVariable variable;
	
	/** The start expression. */
	private Element startExpression;
	
	/** The end expression. */
	private Element endExpression;
	
	/** The step expression. */
	private Element stepExpression;

	/**
	 * Creates an instance of this class and sets the expressions from the given array.
	 * If the given array doesn't have 3 or 4 elements or the first element isn't an instance of 
	 * ElementVariable throws an exception.
	 * 
	 * @param elements array with expressions for this For
	 * 
	 * @throws IllegalArgumentException if the given array doesn't have 3 or 4 elements or the first
	 * element isn't an instance of ElementVariable.
	 */
	public ForLoopNode(Object[] elements) {
		if (elements == null) {
			throw new NullPointerException("elements was null.");
		}
		if (elements.length != 3 && elements.length != 4) {
			throw new IllegalArgumentException("FOR tag must have 3 or 4 elements.");
		}
	
		if (!(elements[0] instanceof ElementVariable)) {
			throw new IllegalArgumentException("First element of FOR tag should be variable. Was " + ((Element) elements[0]).asText() + ".");
		}
		variable = (ElementVariable) elements[0];
		
		for (int i = 1; i < elements.length; i++) {
			if (!(elements[i] instanceof ElementConstantInteger
				|| elements[i] instanceof ElementString 
				|| elements[i] instanceof ElementVariable
			)) {
				throw new IllegalArgumentException("Start, end and step elements should be of "
					+ "number, string or variable. Was " + elements[i].getClass().getName());
			}
		}
		
		startExpression = (Element) elements[1];
		endExpression = (Element) elements[2];
		if (elements.length == 4) {
			stepExpression = (Element) elements[3];
		}
	}

	/**
	 * Gets the variable.
	 *
	 * @return the variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Gets the start expression.
	 *
	 * @return the start expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Gets the end expression.
	 *
	 * @return the end expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Gets the step expression.
	 *
	 * @return the step expression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
}
