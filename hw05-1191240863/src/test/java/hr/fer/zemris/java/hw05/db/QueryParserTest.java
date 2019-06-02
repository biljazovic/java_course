package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class QueryParserTest {
	
	@Test 
	public void testLIKEQuery() {
		QueryParser parser = new QueryParser("firstName LIKE \"A*\"");

		assertFalse(parser.isDirectQuery());

		List<ConditionalExpression> conditions = parser.getQuery();
		
		assertEquals(1, conditions.size());
		
		ConditionalExpression expression = conditions.get(0);
		
		assertEquals(expression.getFieldGetter(), FieldValueGetters.FIRST_NAME);
		assertEquals(expression.getComparisonOperator(), ComparisonOperators.LIKE);
		assertEquals(expression.getStringLiteral(), "A*");
	}

	@Test 
	public void testTwoConditions() {
		QueryParser parser = new QueryParser("firstName > \"B\" AND jmbag != \"000\"");

		assertFalse(parser.isDirectQuery());

		List<ConditionalExpression> conditions = parser.getQuery();
		
		assertEquals(2, conditions.size());
		
		ConditionalExpression expression = conditions.get(0);
		
		assertEquals(expression.getFieldGetter(), FieldValueGetters.FIRST_NAME);
		assertEquals(expression.getComparisonOperator(), ComparisonOperators.GREATER);
		assertEquals(expression.getStringLiteral(), "B");

		expression = conditions.get(1);
		
		assertEquals(expression.getFieldGetter(), FieldValueGetters.JMBAG);
		assertEquals(expression.getComparisonOperator(), ComparisonOperators.NOT_EQUALS);
		assertEquals(expression.getStringLiteral(), "000");
	}
	
	@Test
	public void testDirectQuery() {
		QueryParser parser = new QueryParser("jmbag=\"000\"");

		assertTrue(parser.isDirectQuery());
		
		assertEquals("000", parser.getQueriedJMBAG());
	}
	
	@Test
	public void testAlmostDirectQuery() {
		QueryParser parser = new QueryParser("jmbag != \"000\"");
		
		assertFalse(parser.isDirectQuery());
	}
	
	@Test (expected = ParserException.class) 
	public void testInvalidQuery() {
		QueryParser parser = new QueryParser("");
	}

	@Test (expected = ParserException.class) 
	public void testInvalidQueryJMBG() {
		QueryParser parser = new QueryParser("jmbg = \"000\"");
	}

	@Test (expected = ParserException.class) 
	public void testInvalidQueryANDOnEND() {
		QueryParser parser = new QueryParser("firstName LIKE \"*\" AND");
	}

	@Test (expected = ParserException.class) 
	public void testInvalidQueryNoStringLiteral() {
		QueryParser parser = new QueryParser("firstName LIKE");
	}
}
