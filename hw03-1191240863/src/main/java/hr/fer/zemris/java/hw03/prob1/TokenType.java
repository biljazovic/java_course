package hr.fer.zemris.java.hw03.prob1;

/**
 * Enum of token types, used by {@link Lexer} class.
 * 
 * @author Bruno Iljazović
 */
public enum TokenType {
	
	/** Represents end of input. Is returned when there is no more unprocessed data. */
	EOF,
	
	/** Sequence of letters or digits in BASIC state, sequence of non white-spaces in EXTENDED 
	 * state */
	WORD,
	
	/** The number that fits into the type Long. Always positive. */
	NUMBER,
	
	/** Any symbol character that isn't a part of a word or a number and isn't a white-space */
	SYMBOL
}
