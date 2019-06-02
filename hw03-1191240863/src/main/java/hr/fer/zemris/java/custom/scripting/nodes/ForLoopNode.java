package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * This node is generated when a FOR tag is encountered. It must have 3 or 4 elements and first
 * element must be ElementVariable instance. 
 * 
 * @author bruno
 */
public class ForLoopNode extends Node {
	
	private ElementVariable variable;
	
	private Element startExpression;
	
	private Element endExpression;
	
	private Element stepExpression;
	
	public ForLoopNode () {
	}

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
		
		startExpression = (Element) elements[1];
		endExpression = (Element) elements[2];
		if (elements.length == 4) {
			stepExpression = (Element) elements[3];
		}
	}

	/* getters for private properties */

	public ElementVariable getVariable() {
		return variable;
	}

	public Element getStartExpression() {
		return startExpression;
	}

	public Element getEndExpression() {
		return endExpression;
	}

	public Element getStepExpression() {
		return stepExpression;
	}
}
