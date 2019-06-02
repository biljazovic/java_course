package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * This class parses given text and makes tree-like structure with root in {@link #documentNode}.
 * It makes use of {@link SmartScriptLexer} to tokenize given text. 
 * 
 * <p> There are three possible nodes besides documentNode: EchoNode, ForLoopNode and TextNode.
 * ForLoopNode is generated when a tag named FOR is encountered. Then every Node till matching END 
 * tag is added as a child of that ForLoopNode.
 * 
 * <p>EchoNode is generated when a tag named = is encountered. 
 * 
 * <p>TextNode is generated from text outside of tags.
 * 
 * Example text and tree this parser produces:
 * <pre>======================================================
 * This is text. 
 * {$ FOR i 1 2 $}
 *   Inside for loop!
 *   {$ = 1 "1.2" 1.2 * $}{$END$}
 * Outside!
 * ======================================================
 * DocumentNode
 *   |- TextNode (...)
 *   |- ForLoopNode (i, 1, 2)
 *         |- TextNode (...)
 *         |- EchoNode (1, "1.2", 1.2, *)
 *   |- TextNode (...) 
 * ====================================================== </pre>
 * 
 * Throws {@link SmartScriptParserException} if given text can't be parsed.
 * 
 * @see SmartScriptLexer
 * 
 * @author Bruno Iljazović
 */
public class SmartScriptParser {

	/** stakc of nodes */
	private Stack<Object> stack;
	
	/** lexer used for tokenizing the input string */
	private SmartScriptLexer lexer;
	
	/** The text that is parsed */
	private String documentBody;
	
	/** Root of the structure of nodes */
	private DocumentNode documentNode;
	
	/**
	 * Instantiates a new smart script parser text given as String argument.
	 *
	 * @param documentBody the text to be parsed.
	 */
	public SmartScriptParser(String documentBody) {
		this.documentBody = documentBody;
		parse();
	}
	
	/**
	 * Gets the document node which contains information about whole tree structure.
	 *
	 * @return the document node
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}
	
	/**
	 * Wrapper for {@link SmartScriptLexer#nextToken} method
	 * @return next token
	 * @throws SmartScriptParserException if a {@link LexerException} occurred
	 */
	private Token getToken() {
		try {
			return lexer.nextToken();
		} catch(LexerException ex) {
			throw new SmartScriptParserException(ex);
		}
	}
		
	/**
	 * Parses the text stored in {@link #documentBody}. Parser manages states of the lexer.
	 * 
	 * 
	 * @throws SmartScriptParserException if the text can't be parsed.
	 */
	private void parse() {
		stack = new Stack<>();

		documentNode = new DocumentNode();
		stack.push(documentNode);

		lexer = new SmartScriptLexer(documentBody);
		lexer.setState(LexerState.TEXT);
		
		Token token;
		
		//array of Element-s for ForLoopNode and EchoNode
		List<Object> elements = new ArrayList<>();

		String tagName = "";

		do {
			token = getToken();
					
			if (token.getType() == TokenType.TEXT) {
				if (lexer.getState() == LexerState.STRING) {
					elements.add(new ElementString((String) token.getValue()));
				} else { //TEXT state
					addNode(new TextNode((String) token.getValue()), false);
				}
			} else if (token.getType() == TokenType.TAG_START) {
				lexer.setState(LexerState.TAG);
				tagName = getTagName();

				elements.clear();
			} else if (token.getType() == TokenType.TAG_END) {
				if (tagName.equals("FOR")) {
					try {
						addNode(new ForLoopNode(elements.toArray()), true);
					} catch (IllegalArgumentException ex) {
						throw new SmartScriptParserException(ex);
					}
				} else if (tagName.equals("=")) {
					addNode(new EchoNode(elements.toArray()), false);
				}

				lexer.setState(LexerState.TEXT);
			} else if (token.getType() == TokenType.SYMBOL) {
				if (isOperator((char) token.getValue())) {
					elements.add(new ElementOperator(token.getValue().toString()));
				} else if (token.getValue() == Character.valueOf('"')) {
					if (lexer.getState() == LexerState.STRING) {
						lexer.setState(LexerState.TAG);
					} else {
						lexer.setState(LexerState.STRING);
					}
				} else if (token.getValue() == Character.valueOf('@')) {
					elements.add(new ElementFunction(getFunctionName()));
				} else {
					throw new SmartScriptParserException("Invalid operator inside the tag. Was "
							+ token.getValue().toString() + ".");
				}
			} else if (token.getType() == TokenType.VARIABLE) {
				elements.add(new ElementVariable((String) token.getValue()));
			} else if (token.getType() == TokenType.DOUBLE_NUMBER) {
				elements.add(new ElementConstantDouble(((Double) token.getValue()).doubleValue()));
			} else if (token.getType() == TokenType.INT_NUMBER) {
				elements.add(new ElementConstantInteger(((Integer) token.getValue()).intValue()));
			}
		} while (token.getType() != TokenType.EOF);
		
		if (lexer.getState() == LexerState.TAG) {
			throw new SmartScriptParserException("A tag wasn't closeed with $}. Invalid document.");
		} else if (lexer.getState() == LexerState.STRING) {
			throw new SmartScriptParserException(
					"A string inside tag wasn't closed. Invalid document.");
		}
		if (stack.size() != 1) {
			throw new SmartScriptParserException("A FOR tag wasn't matched with END tag.");
		}
	}

	/**
	 * Returns function name if it's the next token of the lexer.
	 * 
	 * @return function name
	 * @throws SmartScriptParserException if the next token isn't valid function name
	 */
	private String getFunctionName() {
		Token token = getToken();
		if (token.getType() == TokenType.VARIABLE) {
			return (String) token.getValue();
		}
		throw new SmartScriptParserException(
				"After @ symbol must come valid function name. Instead it was "
						+ token.getType().toString() + " \"" + token.getValue().toString() + "\".");
	}
	

	/**
	 * Returns tag name if it's the next token of the lexer.
	 * 
	 * @return tag name
	 * @throws SmartScriptParserException
	 *             if the next token isn't valid tag name or if the end tag is in
	 *             invalid position
	 */
	private String getTagName() {
		Token token = getToken();

		if (token.getType() == TokenType.SYMBOL && token.getValue() == Character.valueOf('=')
				|| token.getType() == TokenType.VARIABLE) {
			String tagNameTemp = token.getValue().toString().toUpperCase();
			if (tagNameTemp.equals("FOR") || tagNameTemp.equals("=")) {
				return tagNameTemp;
			} 
			if (tagNameTemp.equals("END")) {
				stack.pop();
				if (stack.isEmpty()) {
					throw new SmartScriptParserException(
							"There are too many END tags, or they are on invalid positions.");
				}
				return tagNameTemp;
			} 
			throw new SmartScriptParserException("Unknown tag name. Was " + tagNameTemp + ".");
		}
		
		throw new SmartScriptParserException("First element in a tag must be name of the tag.");
	}		
	
	/**
	 * Adds the given node as a child to the node currently on top of the stack.
	 * Pushes new node on the stack if needed.
	 *
	 * @param newNode node to be added
	 * @param pushToStack true if new node should be pushed on the stack
	 */
	private void addNode(Node newNode, boolean pushToStack) {
		((Node) stack.peek()).addChildNode(newNode);

		if (pushToStack) {
			stack.push(newNode);
		}
	}
	
	/**
	 * Checks if the given character is valid operator, i.e. if it is '+', '-', '/',
	 * '^' or '*'.
	 *
	 * @param ch candidate character
	 * @return true iff the given character is an operator.
	 */
	private boolean isOperator(char ch) {
		return "+-*/^".indexOf(ch) != -1;
	}
}
