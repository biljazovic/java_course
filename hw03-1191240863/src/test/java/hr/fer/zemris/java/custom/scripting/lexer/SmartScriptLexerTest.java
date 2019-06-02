package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SmartScriptLexerTest {

	@Test
	public void testNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertNotNull("Token was expected but null was returned.", lexer.nextToken());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testNullInput() {
		new SmartScriptLexer(null);
	}

	@Test
	public void testEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertEquals("Empty input must generate only EOF token.", TokenType.EOF, lexer.nextToken().getType());
	}
	@Test
	public void testGetReturnsLastNext() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		Token token = lexer.nextToken();
		assertEquals("getToken returned different token than nextToken.", token, lexer.getToken());
		assertEquals("getToken returned different token than nextToken.", token, lexer.getToken());
	}

	@Test(expected=LexerException.class)
	public void testRadAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		lexer.nextToken();
		lexer.nextToken();
	}
	
	@Test
	public void testText() {
		SmartScriptLexer lexer = new SmartScriptLexer("This is a lexer. \n");
		
		Token correctData[] = {
			new Token(TokenType.TEXT, "This is a lexer. \n"),
			new Token(TokenType.EOF, null)
		};
				
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testTags() {
		SmartScriptLexer lexer = new SmartScriptLexer("This is text. {$ END $} {$ $}");
		
		Token correctData[] = {
			new Token(TokenType.TEXT, "This is text. "),
			new Token(TokenType.TAG_START, null),
			new Token(TokenType.VARIABLE, "END"),
			new Token(TokenType.TAG_END, null),
			new Token(TokenType.TEXT, " "),
			new Token(TokenType.TAG_START, null),
			new Token(TokenType.TAG_END, null),
		};
				
		checkTokenStream(lexer, correctData);
	}
	
	
	@Test
	public void testTagContent() {
		SmartScriptLexer lexer = new SmartScriptLexer("This is text. {$ \"String 1.2323 \" 1.232 124 @func$}");
		
		Token correctData[] = {
			new Token(TokenType.TEXT, "This is text. "),
			new Token(TokenType.TAG_START, null),
			new Token(TokenType.SYMBOL, Character.valueOf('"')),
			new Token(TokenType.TEXT, "String 1.2323 "),
			new Token(TokenType.SYMBOL, Character.valueOf('"')),
			new Token(TokenType.DOUBLE_NUMBER, Double.valueOf(1.232)),
			new Token(TokenType.INT_NUMBER, Integer.valueOf(124)),
			new Token(TokenType.SYMBOL, Character.valueOf('@')),
			new Token(TokenType.VARIABLE, "func"),
			new Token(TokenType.TAG_END, null),
		};
				
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testEscapedCharacters() {
		SmartScriptLexer lexer = new SmartScriptLexer("This is brace \\{ and backslash \\\\ {$ \"This is newline \\n and backslash "
				+ "\\\\ and double quote \\\"\"$}");
		
		Token correctData[] = {
			new Token(TokenType.TEXT, "This is brace { and backslash \\ "),
			new Token(TokenType.TAG_START, null),
			new Token(TokenType.SYMBOL, Character.valueOf('"')),
			new Token(TokenType.TEXT, "This is newline \n and backslash \\ and double quote \""),
			new Token(TokenType.SYMBOL, Character.valueOf('"')),
			new Token(TokenType.TAG_END, null),
		};
				
		checkTokenStream(lexer, correctData);
	}
	
	@Test(expected = LexerException.class)
	public void testInvalidEscape() {
		SmartScriptLexer lexer = new SmartScriptLexer("This is wrong \\a");

		for (Token token = lexer.nextToken(); token.getType() != TokenType.EOF; token = lexer.nextToken()) {
			changeState(token, lexer);
		}
	}

	@Test(expected = LexerException.class)
	public void testInvalidNumber() {
		SmartScriptLexer lexer = new SmartScriptLexer("This is also wrong: {$ 12.12.12 $}");

		for (Token token = lexer.nextToken(); token.getType() != TokenType.EOF; token = lexer.nextToken()) {
			changeState(token, lexer);
		}
	}
	
	private void changeState(Token token, SmartScriptLexer lexer) {
		if (token.getType() == TokenType.TAG_START) {
			lexer.setState(LexerState.TAG);
		}
		else if (token.getType() == TokenType.TAG_END) {
			lexer.setState(LexerState.TEXT);
		}
		else if (token.getType() == TokenType.SYMBOL && 
				token.getValue() == Character.valueOf('"')) {
			if (lexer.getState() == LexerState.STRING) {
				lexer.setState(LexerState.TAG);
			}
			else {
				lexer.setState(LexerState.STRING);
			}
		}
	}
	

	private void checkTokenStream(SmartScriptLexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			changeState(actual, lexer);	
			String msg = "Checking token "+counter + ":";
			assertEquals(msg, expected.getType(), actual.getType());
			assertEquals(msg, expected.getValue(), actual.getValue());
			counter++;
		}
	}
}
