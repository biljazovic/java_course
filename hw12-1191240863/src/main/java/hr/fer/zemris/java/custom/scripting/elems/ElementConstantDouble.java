package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Constant double number element.
 * @author bruno
 */
public class ElementConstantDouble extends Element {
	
	/** value of this element */
	private double value;

	/**
	 * Instantiates a new element constant double.
	 *
	 * @param value the value
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
	@Override
	public String toString() {
		return asText();
	}

}
