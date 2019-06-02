package hr.fer.zemris.java.custom.scripting.elems;

/**
 * The variable element.
 * @author bruno
 */
public class ElementVariable extends Element{

	private String name;
	
	/**
	 * Instantiates a new element variable.
	 *
	 * @param name the name
	 */
	public ElementVariable(String name) {
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
