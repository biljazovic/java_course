package hr.fer.zemris.java.custom.scripting.elems;

/**
 * The string element.
 * @author bruno
 */
public class ElementString extends Element {

	private String value;

	/**
	 * Instantiates a new element string.
	 *
	 * @param value the value
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return value;
	}

}
