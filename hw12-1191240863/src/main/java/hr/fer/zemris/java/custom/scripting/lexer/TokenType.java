package hr.fer.zemris.java.custom.scripting.lexer;


/**
 * Enum of token types, used by {@link SmartScriptLexer} class.
 * 
 * @author Bruno Iljazović
 */
public enum TokenType {

	/** Represents start of a tag, token with this type is returned when "{$" is encountered. */
	TAG_START,
	
	/** Represents end of a tag, token with this type is returned when "$}" is encountered. */
	TAG_END,
	
	/**A string, no limitations */
	TEXT,
	
	/** A String that starts with a letter and consists of letters, digits and/or character '_'.*/
	VARIABLE,
	
	/** Single character */
	SYMBOL,
	
	/** Integer that fits type int. */
	INT_NUMBER,
	
	/** Decimal number that fits type double. */
	DOUBLE_NUMBER,
	
	/** Represents end of input. Is returned when there is no more unprocessed data. */
	EOF
}
