package hr.fer.zemris.java.custom.scripting.elems;

/**
 * The integer element.
 * @author bruno
 */
public class ElementConstantInteger extends Element {
	
	private int value;

	/**
	 * Instantiates a new element constant integer.
	 *
	 * @param value the value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return String.valueOf(value);
	}

}
