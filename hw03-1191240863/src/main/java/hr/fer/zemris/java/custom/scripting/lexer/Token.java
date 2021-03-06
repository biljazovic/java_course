package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * This class represents one token that is generated by {@link Lexer}.
 * 
 * <p>Has two read-only properties: type of token and its value.
 * 
 * @see TokenType
 * 
 " @author Bruno Iljazović
 */
public class Token {
	
	TokenType type;
	
	Object value;
	
	/**
	 * Instantiates a new token with given type and value.
	 *
	 * @param type type of token
	 * @param value value of token
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

}
