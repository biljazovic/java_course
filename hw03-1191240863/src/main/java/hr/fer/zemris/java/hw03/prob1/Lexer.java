package hr.fer.zemris.java.hw03.prob1;

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
	 * @throws LexerException if this method was called after EOF was already returned or if there 
	 * was invalid text, e.g. number that doesn't fit in type Long.
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("nextToken was called after EOF was returned.");
		}

		while (currentIndex < data.length && isWhitespace(data[currentIndex])) {
			++currentIndex;
		}

		if (currentIndex == data.length) {
			return token = new Token(TokenType.EOF, null);
		}
		
		if (state == LexerState.BASIC) {
			String word = getWord();
			if (word.length() > 0) {
				return token = new Token(TokenType.WORD, word);
			}
			
			Long number = getNumber();
			if (number != null) {
				return token = new Token(TokenType.NUMBER, number);
			}
			
			return new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
		}
		else {
			if (data[currentIndex] == '#') {
				return new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
			}
			
			int indexStart = currentIndex;
			while (currentIndex < data.length && 
					data[currentIndex] != '#' && 
					!isWhitespace(data[currentIndex])) {
				++currentIndex;
			}
			return new Token(TokenType.WORD,
					new String(data, indexStart, currentIndex - indexStart));
		}
			
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
	 * Tries to process a number starting in {@link #currentIndex}. Number should fit into type 
	 * long.
	 *
	 * @return Generated number. Null if the character at {@link #currentIndex} isn't a digit.
	 * 
	 * @throws LexerException If the number was too big (didn't fit into type Long).
	 */
	private Long getNumber() {
	 	int indexStart = currentIndex;
		while (currentIndex < data.length && isDigit(data[currentIndex])) {
			++currentIndex;
		}
		
		if (currentIndex == indexStart) return null;

		try {
			return Long.parseLong(new String(data, indexStart, currentIndex - indexStart));
		} catch (NumberFormatException ex) {
			throw new LexerException("Number was too big for Long. Was " + 
					new String(data, indexStart, currentIndex - indexStart)
			);
		}
	}
		
	
	/**
	 * Tries to process a word. Word is a non-empty sequence of letters. If after escape character 
	 * '\' comes another escape character or a digit they are also considered a part of a word.
	 *	
	 * <p> called only in BASIC state.
	 * 
	 * @return The generated word. Returns empty word if the first character isn't a letter or an
	 * escape character.
	 * 
	 * @throws LexerException If there was invalid character after escape character.
	 */
	private String getWord() {
		StringBuilder word = new StringBuilder();
		while (currentIndex < data.length) {
			if (Character.isLetter(data[currentIndex])) {
				word.append(data[currentIndex++]);
				continue;
			}
			else if (isEscape(data[currentIndex])) {
				if (currentIndex + 1 == data.length) 
					throw new LexerException("Escape character can't be at the end of the string.");
				if (!(isEscape(data[currentIndex + 1]) || isDigit(data[currentIndex + 1]))) 
					throw new LexerException(
							"After escape character came '" + data[currentIndex + 1] + "'.");
				
				++currentIndex;
				word.append(data[currentIndex++]);
			}
			else {
				break;
			}
		}
		return word.toString();
	}
		
	/**
	 * Checks if the given character is escape character.
	 *
	 * @param ch the candidate character
	 * @return {@code true}, iff the given character is escape
	 */
	private boolean isEscape(char ch) {
		return ch == '\\';
	}
	
	/**
	 * Checks if the given character is digit.
	 *
	 * @param ch the candidate character
	 * @return {@code true}, iff the given character is a digit
	 */
	private boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}
	
	/**
	 * Checks if the given character is 'whitespace' character, i.e. if it is ' ', '\n', '\t' or '\r'
	 *
	 * @param ch the candidate character
	 * @return {@code true}, iff the given character is a whitespace.
	 */
	private boolean isWhitespace(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
	}
}
