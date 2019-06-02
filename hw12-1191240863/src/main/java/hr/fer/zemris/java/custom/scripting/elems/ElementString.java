package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * The string element.
 * @author bruno
 */
public class ElementString extends Element {

	/** value of this element - string of characters */
	private String value;

	/**
	 * Instantiates a new element string.
	 *
	 * @param value the value
	 */
	public ElementString(String value) {
		this.value = Objects.requireNonNull(value);
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

	@Override
	public String toString() {
		return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
			.replace("\t", "\\t").replace("\r", "\\r") + "\"";
	}
}
