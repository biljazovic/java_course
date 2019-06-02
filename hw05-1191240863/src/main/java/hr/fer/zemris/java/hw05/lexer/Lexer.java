package hr.fer.zemris.java.hw05.lexer;

/**
 * This class breaks the given text in number of tokens. There are two states which are described
 * in {@link LexerState}, and number of possible tokens which are described in {@link TokenType}.
 * 
 * <p>Tokens can be accessed one by one, calling method {@link #nextToken}. Last returned token is 
 * provided by {@link #getToken} method.
 * 
 * <p>User that creates a Lexer instance should change its state when needed by calling {@link 
 * #setState}
 * 
 * @author Bruno Iljazovic
 */
public class Lexer {
	
	/** Given data to tokenize */
	private char[] data;

	/** Last generated token */
	private Token token;

	/** Index of the first unprocessed character in {@link #data} */
	private int currentIndex;
	
	/** The state of the Lexer. */
	private LexerState state;
	
	private final String[] operators = {"LIKE", "<=", ">=", "!=", "=", "<", ">"};

	/**
	 * Instantiates a new lexer with given text as {@link #data}. State is set to BASIC.
	 *
	 * @param text Text to tokenize.
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("text was null.");
		}
		data = text.toCharArray();
		state = LexerState.BASIC;
	}
	
	/**
	 * Sets the state of the lexer.
	 *
	 * @param state New state of the lexer.
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new IllegalArgumentException("state was null.");
		}
		this.state = state;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state of the lexer.
	 */
	public LexerState getState() {
		return state;
	}

	/**
	 * Generates and returns next token. {@link #currentIndex} is set to next unprocessed character.
	 * If the returned token is of type EOF this method shouldn't be called again.
	 *
	 * @return The generated token.
	 * 
	 * @throws LexerException if this method was called after EOF was already returned
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("nextToken was called after EOF was returned.");
		}

		if (state == LexerState.BASIC) {
			while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
				++currentIndex;
			}
		}

		if (currentIndex == data.length) {
			return token = new Token(TokenType.EOF, null);
		}
		
		if (state == LexerState.BASIC) {
			for (String operator : operators) {
				if (peek(operator)) {
					currentIndex += operator.length();
					return token = new Token(TokenType.OPERATOR, operator);
				}
			}
			
			String word = getWord();
			if (word.length() > 0) {
				return token = new Token(TokenType.WORD, word);
			}
			
			return token = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
		}
		else {
			if (data[currentIndex] == '"') {
				return token = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
			}
			
			int indexStart = currentIndex;
			while (currentIndex < data.length && data[currentIndex] != '"') {
				++currentIndex;
			}
			return token = new Token(TokenType.WORD,
					new String(data, indexStart, currentIndex - indexStart));
		}
			
	}
	
	private boolean peek(String string) {
		return currentIndex + string.length() <= data.length &&
				new String(data, currentIndex, string.length()).equals(string);
	}

	/**
	 * Returns the last generated token.
	 *
	 * @return Last generated token, null if {@link #nextToken} still wasn't called.
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Tries to process a word. Word is a non-empty sequence of letters.
	 *	
	 * <p> called only in BASIC state.
	 * 
	 * @return The generated word. Returns empty word if the first character isn't a letter or an
	 * escape character.
	 */
	private String getWord() {
		StringBuilder word = new StringBuilder();

		while (currentIndex < data.length) {
			if (Character.isLetter(data[currentIndex])) {
				word.append(data[currentIndex++]);
			}
			else {
				break;
			}
		}

		return word.toString();
	}
}
