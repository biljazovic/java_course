package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * EchoNode is generated when a tag with name "=" is encountered.
 * 
 * @author bruno
 */
public class EchoNode extends Node {

	/** elements of echo node */
	private Element[] elements;
	
	/**
	 * Instantiates a new echo node with given elements. and up-casts them to Element class
	 *
	 * @param elements elements of echo node
	 */
	public EchoNode(Object[] elements) {
		this.elements = Arrays.copyOf(elements, elements.length, Element[].class);
	}
	
	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	public Element[] getElements() {
		return Arrays.copyOf(elements, elements.length);
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
