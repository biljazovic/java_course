package hr.fer.zemris.java.hw05.lexer;

/**
 * Enum of token types, used by {@link Lexer} class.
 * 
 * @author Bruno Iljazović
 */
public enum TokenType {
	
	/** Represents end of input. Is returned when there is no more unprocessed data. */
	EOF,
	
	/** Sequence of letters in BASIC state, sequence of characters in STRING_LITERAL state */
	WORD,

	/** Any of the following: "<", ">", "<=", ">=", "=", "!=", "LIKE" */
	OPERATOR,
	
	/** any character that isn't a part of a word or an operator */
	SYMBOL
}
