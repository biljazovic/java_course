package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * The variable element.
 * @author bruno
 */
public class ElementVariable extends Element{

	/** name of this variable */
	private String name;
	
	/**
	 * Instantiates a new element variable.
	 *
	 * @param name the name
	 */
	public ElementVariable(String name) {
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
		return name;
	}
}
