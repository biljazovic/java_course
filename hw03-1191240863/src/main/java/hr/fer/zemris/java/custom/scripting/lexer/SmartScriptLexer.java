package hr.fer.zemris.java.custom.scripting.lexer;

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
 * @author Bruno Iljazović
 */
public class SmartScriptLexer {
	
	/** Given data to tokenize */
	private char[] data;
	
	/** Last generated token */
	private Token token;
	
	/** Index of the first unprocessed character in {@link #data} */
	private int currentIndex;
	
	/** The state of the lexer 
	 * @see LexerState
	 * */
	private LexerState state;
	
	/**
	 * Instantiates a new lexer with given text as {@link #data}. State is set to TEXT.
	 *
	 * @param text Text to tokenize.
	 */
	public SmartScriptLexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("text was null.");
		}
		data = text.toCharArray();
		state = LexerState.TEXT;
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
	 * Returns current state of the lexer.
	 *
	 * @return the state of the lexer.
	 */
	public LexerState getState() {
		return state;
	}

	/*
	 * Strings that describe which sequences of characters should be escaped and which should make
	 * the lexer stop interpreting whatever it's interpreting.
	 */

	private final String textEscapable = "\\{";
	private final String textTurnInto = "\\{";
	private final String textStop = "{$";

	private final String stringEscapable = "\\\"nrt";
	private final String stringTurnInto = "\\\"\n\r\t";
	private final String stringStop = "\"";
	
	/**
	 * Generates and returns next token. {@link #currentIndex} is set to next unprocessed character.
	 * If the returned token is of type EOF this method shouldn't be called again.
	 *
	 * @return The generated token.
	 * 
	 * @throws LexerException if this method was called after EOF was already returned or if there 
	 * was invalid text (e.g. sequence of digits that has more than one decimal point).
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("nextToken was called after EOF was returned.");
		}
		
		if (currentIndex == data.length) {
			return token = new Token(TokenType.EOF, null);
		}
			
		if (state == LexerState.TEXT) {
			String text = getText(textEscapable, textTurnInto, textStop);

			if (text.length() == 0) { //found textStop right away
				currentIndex += 2;
				return token = new Token(TokenType.TAG_START, null);
			}

			return token = new Token(TokenType.TEXT, text);
		}
		else if (state == LexerState.STRING) {
			String text = getText(stringEscapable, stringTurnInto, stringStop);

			if (text.length() == 0) { //found stringStop right away
				return token = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
			}

			return token = new Token(TokenType.TEXT, text);
		}
		else { //TAG state
			//skip white-spaces
			while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
				++currentIndex;
			}
			
			//after skipping white-spaces we may reach EOF
			if (currentIndex == data.length) return nextToken();

			//try to parse tag end
			if (data[currentIndex] == '$' 
					&& currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
				currentIndex += 2;
				return token = new Token(TokenType.TAG_END, null);
			}
					
			//try to parse a variable name
			String word = getWord();
			if (word.length() > 0) {
				return token = new Token(TokenType.VARIABLE, word);
			}

			//try to parse minus sign
			Integer numberSign = 1;
			if (data[currentIndex] == '-') {
				if (currentIndex + 1 < data.length && isDigit(data[currentIndex + 1])) {
					numberSign = -1;
					++currentIndex;
				}
			}

			//try to parse a number(double or integer)
			String number = getNumber();
			if (number.length() > 0) {
				if (number.indexOf('.') == -1) { //integer
					try {
						return token = new Token(TokenType.INT_NUMBER,
								Integer.parseInt(number) * numberSign);
					} catch (NumberFormatException ex) {
						throw new LexerException("Invalid integer inside tag. Was " + number + ".");
					}
				}

				//non-integer number
				try {
					return token = new Token(TokenType.DOUBLE_NUMBER,
							Double.parseDouble(number) * numberSign);
				} catch (NumberFormatException ex) {
					throw new LexerException("Invalid decimal number inside tag. Was " + number + ".");
				}
			}
			
			//if nothing else worked, return symbol
			return token = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
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
	 * Tries to process a number starting in {@link #currentIndex}. Number can be decimal or 
	 * integer.
	 * 
	 * <p> Only decimal point ('.') is considered valid.
	 * 
	 * <p>This method only returns sequence of characters, it doesn't convert it into Integer or
	 * Double
	 * 
	 * <p> called only in TAG state
	 *
	 * @return Sequence of digits and at most one decimal point.
	 * 
	 * @throws LexerException If more than one decimal point is encountered.
	 */
	private String getNumber() {
		boolean decimalPointFound = false;
		int startIndex = currentIndex;
		
		while (currentIndex < data.length) {
			if (data[currentIndex] == '.') {
				if (decimalPointFound) {
					throw new LexerException("Two decimal points encountered in a number");
				}
				decimalPointFound = true;
				++currentIndex;
			}
			else if (isDigit(data[currentIndex])) {
				++currentIndex;
			}
			else break;
		}
		
		return new String(data, startIndex, currentIndex - startIndex);
	}
		
	/**
	 * Tries to process a word. Word is a non-empty sequence of letters, digits and character '_'
	 * that start with a letter. 
	 *	
	 * <p> called only in TAG state
	 * 
	 * @return The generated word. Returns empty word if the first character isn't a letter or an
	 * escape character.
	 * 
	 */
	private String getWord() {
		int startIndex = currentIndex;
		if (Character.isLetter(data[currentIndex])) {
			++currentIndex;
			while (currentIndex < data.length && isVariableCharacter(data[currentIndex])) {
				++currentIndex;
			}
		}
		return new String(data, startIndex, currentIndex - startIndex);
	}
	
	/**
	 * Tries to process a text. This method is called in TEXT and STRING states. Method is given
	 * sequence of escapable characters and what should these characters be converted and stopping 
	 * string depending on which state lexer is in.
	 *  
	 * @param escapable sequence of characters which can be escaped
	 * @param turnInto first character in turnInto is a result of escaping the first character in 
	 * String escapable and so on
	 * @param stopString sequence of character which stop parsing of the text and ends this method.
	 *  
	 * @return processed text, empty string if a stopString is found right away.
	 * 
	 * @throws LexerException if after '\' comes end of string or character that isn't in escapable
	 */ 
	private String getText(String escapable, String turnInto, String stopString) {
		StringBuilder text = new StringBuilder();
		while (currentIndex < data.length) {

			//stopString check
			int j = 0;
			for (int i = currentIndex; i < data.length && j < stopString.length(); ++i, ++j) {
				if (data[i] != stopString.charAt(j)) break;
			}
			if (j == stopString.length()) break;

			//escapable characters check
			if (data[currentIndex] == '\\') {
				if (currentIndex + 1 == data.length) 
					throw new LexerException("Lone escape character can't be at the end.");
				if (escapable.indexOf(data[currentIndex + 1]) == -1) 
					throw new LexerException(
							"After escape character came '" + data[currentIndex + 1] + "'.");
				
				++currentIndex;
				text.append(turnInto.charAt(escapable.indexOf(data[currentIndex++])));
			}

			//normal character
			else {
				text.append(data[currentIndex++]);
			}
		}
		return text.toString();
	}
	
	/**
	 * Checks if a given character is valid variable character, i.e. if it is a letter, digit, or 
	 * a character '_'.
	 *
	 * @param ch the candidate character.
	 * 
	 * @return {@code true}, iff the given character is a variable character.
	 */
	private boolean isVariableCharacter(char ch) {
		return Character.isLetter(ch) || isDigit(ch) || ch == '_';
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
	
}
