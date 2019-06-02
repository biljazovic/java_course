package hr.fer.zemris.java.custom.scripting.elems;

/**
 * The operator element.
 * @author bruno
 */
public class ElementOperator extends Element {

	private String symbol;
	
	/**
	 * Instantiates a new element operator.
	 *
	 * @param symbol the symbol
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}

}
