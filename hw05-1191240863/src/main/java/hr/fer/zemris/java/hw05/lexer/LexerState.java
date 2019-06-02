package hr.fer.zemris.java.hw05.lexer;


/**
 * Enum of lexer states that are used in {@link Lexer} class.
 */
public enum LexerState {
	
	/** 
	 * In this state WORD can contain only letters, digits and backslashes. White-spaces are 
	 * ignored. Any character that isn't a part of a WORD or a NUMBER is SYMBOL. Number must fit
	 * into type long. 
	 * <p> Minus sign before a number doesn't make number negative, but is interpreted as a symbol.
	 */
	BASIC,
	
	/** 
	 * In this state any sequence of characters that doesn't contain '"' is a WORD. 
	 * Symbol '"' is returned as SYMBOL.
	 */
	STRING_LITERAL,
}
