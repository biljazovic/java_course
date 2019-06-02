package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * The function element
 * @author bruno
 */
public class ElementFunction extends Element {

	/** name of this function */
	private String name;
	
	/**
	 * Instantiates a new element function.
	 *
	 * @param name the name
	 */
	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return "@" + name;
	}

}
