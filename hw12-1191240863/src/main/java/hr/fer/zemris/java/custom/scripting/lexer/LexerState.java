package hr.fer.zemris.java.custom.scripting.lexer;


/**
 * Enum of lexer states that are used in {@link SmartScriptLexer} class.
 * 
 * @see TokenType
 * 
 * @author Bruno Iljazovic
 */
public enum LexerState {
	
	/**
	 * In this state everything is turned into one token of TEXT type with exception of TAG_START
	 * which is made if "{$" is encountered. Also EOF can be returned.
	 * <p>Escapable characters are '{' and '\'. Any other character that comes after '\' raises a
	 * LexerException
	 */
	TEXT,
	
	/**
	 * In this state white-spaces are ignored. Possible returned tokens are TAG_END, VARIABLE, 
	 * INT_NUMBER, DOUBLE_NUMBER and SYMBOL. Numbers can be negative.
	 */
	TAG,
	
	/**
	 * In this state everything is turned into one token of TEXT type with exception of 
	 * symbol '"' which is returned as SYMBOL token. 
	 * <p>Escapable characters are '{', '\', 'n', 'r', 't'. Any other character that comes after '\'
	 * raises a LexerException
	 */
	STRING,
}
