package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;

/**
 * This class represents one node in a tree structure that {@link SmartScriptLexer} generates.
 * User can add children to it, and access the children it has.
 * 
 * @author Bruno Iljazović
 */
public class Node {

	/** The children. */
	private ArrayIndexedCollection children;
	
	/**
	 * Adds the child node and initializes Collection {@link #children} if node had no children
	 * before
	 *
	 * @param child the node to be added as a child
	 */
	public void addChildNode(Node child) {
		if (children == null) {
			children = new ArrayIndexedCollection();
		}
		children.add(child);
	}
	
	/**
	 * Returns the number of children of this node.
	 *
	 * @return the number of children
	 */
	public int numberOfChildren() {
		if (children == null) return 0;
		return children.size();
	}
	
	/**
	 * Gets the child at given position.
	 *
	 * @param index the index of a child to be returned
	 * 
	 * @return the child node at the given index
	 */
	public Node getChild(int index) {
		if (children == null) {
			throw new IndexOutOfBoundsException("node has no children.");
		}
		return (Node) children.get(index); //throws IndexOutOfBoundsException
	}
	
	/**
	 * Checks only the structure of the nodes, not its content. 
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) return false;
		if (!(arg0 instanceof Node)) return false;
		Node other = (Node) arg0;

		if (this.getClass() != arg0.getClass()) return false;

		if (this.numberOfChildren() != other.numberOfChildren()) return false;
		
		if (children == null) return true;
		Node[] arrayThis = Arrays.copyOf(this.children.toArray(), this.numberOfChildren(), Node[].class);
		Node[] arrayOther = Arrays.copyOf(other.children.toArray(), other.numberOfChildren(), Node[].class);
		
		return Arrays.equals(arrayThis, arrayOther);
	}
}
