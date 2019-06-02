package hr.fer.zemris.java.custom.scripting.elems;

/**
 * The function element
 * @author bruno
 */
public class ElementFunction extends Element {

	private String name;
	
	/**
	 * Instantiates a new element function.
	 *
	 * @param name the name
	 */
	public ElementFunction(String name) {
		this.name = name;
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

}
