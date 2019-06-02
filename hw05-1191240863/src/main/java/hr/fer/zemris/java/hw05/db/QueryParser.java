package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.lexer.Lexer;
import hr.fer.zemris.java.hw05.lexer.LexerState;
import hr.fer.zemris.java.hw05.lexer.Token;
import hr.fer.zemris.java.hw05.lexer.TokenType;

/**
 * Parses the string into a list of conditional expressions which are separated in the string 
 * with operator AND. Valid conditional expression is of the form
 * 'attribute operator stringLiteral'. Possible attributes and operators can be seen in 
 * {@link QueryParser#getFieldGetter} and {@link QueryParser#getComparisonOperator}.
 *
 * @author Bruno Iljazović
 */
public class QueryParser {
	
	/** The conditions. */
	List<ConditionalExpression> conditions;
	
	/**
	 * Instantiates a new query parser with the given string.
	 *
	 * @param queryString the string which describes one query
	 * 
	 * @throws ParserException if the queryString isn't valid
	 */
	public QueryParser(String queryString) {
		conditions = new ArrayList<ConditionalExpression>();
		parse(queryString);
		if (conditions.isEmpty()) {
			throw new ParserException("No queries given.");
		}
	}
	
	/**
	 * Expects token of the given type. If the given object value isn't null, also checks whether
	 * the token's value equals the given one.
	 * <p>If the types and/or values aren't matching, determined by equals method exception is
	 * thrown
	 *
	 * @param token the token in question
	 * @param type the expected type
	 * @param value if this is not null, expected value
	 * 
	 * @throws ParserException if the types and/or values don't match
	 */
	private void expectToken(Token token, TokenType type, Object value) {
		if (token.getType() != type) {
			throw new ParserException(
					"Invalid queryString. Expected token of the type " + type.toString() 
					+ ", but got " + token.getType().toString());
		}
		if (value != null && !token.getValue().equals(value)) {
			throw new ParserException(
					"Invalid queryString. Expected " + value.toString() + ", but got " 
					+ token.getValue().toString() + ".");
		}
	}
	
	/**
	 * Gets the field getter based on the given string. Three possible strings:
	 * "firstName", "lastName", "jmbag". In other cases, exception is thrown.
	 *
	 * @param word the string describing the field getter
	 * @return the field getter
	 * @throws ParserException if the given string doesn't describe a field Getter
	 */
	IFieldValueGetter getFieldGetter(String word) {
		switch(word) {
		case "firstName":
			return FieldValueGetters.FIRST_NAME;
		case "lastName":
			return FieldValueGetters.LAST_NAME;
		case "jmbag":
			return FieldValueGetters.JMBAG;
		default:
			throw new ParserException("Invalid attribute name. Was " + word + ".");
		}
	}
	
	/**
	 * Gets the comparison operator based on the given string. Seven possible strings:
	 * "LESS", "GREATER", "GREATER_OR_EQUALS", "LESS_OR_EQUALS", "EQUALS", "NOT_EQUALS", "LIKE".
	 * In other cases, exception is thrown.
	 *
	 * @param word the string describing the comparison operator
	 * @return the comparison operator
	 * @throws ParserException if the given string doesn't describe a comparison operator
	 */
	IComparisonOperator getComparisonOperator(String word) {
		switch(word) {
		case "<":
			return ComparisonOperators.LESS;
		case ">":
			return ComparisonOperators.GREATER;
		case ">=":
			return ComparisonOperators.GREATER_OR_EQUALS;
		case "<=":
			return ComparisonOperators.LESS_OR_EQUALS;
		case "=":
			return ComparisonOperators.EQUALS;
		case "!=":
			return ComparisonOperators.NOT_EQUALS;
		case "LIKE":
			return ComparisonOperators.LIKE;
		default:
			//this shouldn't ever happen
			throw new ParserException("Invalid operator. Was " + word + ".");
		}
	}
			
	
	/**
	 * Parses the given string, and if it is valid, produces the list of conditional expressions.
	 *
	 * @param queryString the string describing a query
	 * 
	 * @throws ParserException if the given query string isn't valid
	 */
	private void parse(String queryString) {
		Lexer lexer = new Lexer(queryString); //won't ever throw LexerException (or rather, it shouldn't)
		
		boolean andOnEnd = false;
		
		while (true) {
			Token token = lexer.nextToken();
			if (token.getType() == TokenType.EOF) {
				break;
			} 
			expectToken(lexer.getToken(), TokenType.WORD, null);

			String word = (String) lexer.getToken().getValue();

			if (word.toUpperCase().contentEquals("AND")) {
				andOnEnd = true;
				continue;
			}

			IFieldValueGetter fieldGetter = getFieldGetter(word);
			
			expectToken(lexer.nextToken(), TokenType.OPERATOR, null);

			IComparisonOperator operator = getComparisonOperator((String)lexer.getToken().getValue());
			
			expectToken(lexer.nextToken(), TokenType.SYMBOL, Character.valueOf('"'));
			lexer.setState(LexerState.STRING_LITERAL);
			expectToken(lexer.nextToken(), TokenType.WORD, null);

			String stringLiteral = (String) lexer.getToken().getValue();

			if (operator == ComparisonOperators.LIKE && stringLiteral.split("\\*", -1).length > 2) {
				throw new ParserException(
						"At most one * character is allowed in the pattern for operator LIKE");
			}
			
			expectToken(lexer.nextToken(), TokenType.SYMBOL, Character.valueOf('"'));
			lexer.setState(LexerState.BASIC);
			
			conditions.add(new ConditionalExpression(fieldGetter, stringLiteral, operator));
			
			andOnEnd = false;
		}
		
		if (andOnEnd) {
			throw new ParserException("AND operator cannot be at the end of queryString.");
		}
	}
		

	/**
	 * Checks if this query is direct query, i. e. is of the type 'jmbag = "..."'
	 *
	 * @return true, if this query is direct query
	 */
	public boolean isDirectQuery() {
		return conditions.size() == 1 &&
				conditions.get(0).getFieldGetter() == FieldValueGetters.JMBAG &&
				conditions.get(0).getComparisonOperator() == ComparisonOperators.EQUALS;
	}
	
	/**
	 * If this query is a direct query, determined by {@link QueryParser#isDirectQuery()} returns
	 * the queried jmbag. Otherwise, throws an exception
	 *
	 * @return the queried JMBAG
	 * 
	 * @throws IllegalArgumentException iff {@link QueryParser#isDirectQuery()} return false
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Query isn't a direct one.");
		}
		return conditions.get(0).getStringLiteral();
	}
	
	/**
	 * Gets the list of conditional expressions.
	 *
	 * @return the list of conditional expressions.
	 */
	public List<ConditionalExpression> getQuery() {
		return new ArrayList<ConditionalExpression>(conditions);
	}
	
	
}
