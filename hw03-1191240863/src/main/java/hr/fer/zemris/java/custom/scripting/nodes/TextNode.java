package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * TextNode is generated when text outside of tags is encountered.
 * 
 * @author bruno
 */
public class TextNode extends Node {
	
	private String text;
	
	public TextNode() {
	}

	/**
	 * Instantiates a new text node from given text.
	 *
	 * @param text
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
}
