package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * EchoNode is generated when a tag with name "=" is encountered.
 * 
 * @author bruno
 */
public class EchoNode extends Node {

	private Element[] elements;
	
	public EchoNode() {
	}
	
	/**
	 * Instantiates a new echo node with given elements. and up-casts them to Element class
	 *
	 * @param elements
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
}
