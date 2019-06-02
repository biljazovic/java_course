package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * TextNode is generated when text outside of tags is encountered.
 * 
 * @author bruno
 */
public class TextNode extends Node {
	
	/** The text. */
	private String text;
	
	/**
	 * Instantiates a new text node.
	 */
	public TextNode() {
	}

	/**
	 * Instantiates a new text node from given text.
	 *
	 * @param text text of this node
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
