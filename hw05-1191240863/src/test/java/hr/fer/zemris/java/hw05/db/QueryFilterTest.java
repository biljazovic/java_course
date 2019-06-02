package hr.fer.zemris.java.hw05.db;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryFilterTest {

	@Test
	public void testPass() {
		List<ConditionalExpression> conditions = new QueryParser("firstName LIKE \"A*\"").getQuery();
		QueryFilter filter = new QueryFilter(conditions);
		
		assertTrue(filter.accepts(new StudentRecord(
				"000",
				"Bosnic",
				"Ana",
				1
		)));
	}
	
	@Test
	public void testFail() {
		List<ConditionalExpression> conditions = new QueryParser("firstName LIKE \"A*\"").getQuery();
		QueryFilter filter = new QueryFilter(conditions);
		
		assertFalse(filter.accepts(new StudentRecord(
				"000",
				"Bosnic",
				"Bna",
				1
		)));
	}
	
	@Test
	public void testNullRecord() {
		List<ConditionalExpression> conditions = new QueryParser("firstName LIKE \"A*\"").getQuery();
		QueryFilter filter = new QueryFilter(conditions);
		
		assertFalse(filter.accepts(null));
	}

	@Test
	public void testTwoConditions() {
		List<ConditionalExpression> conditions = new QueryParser("firstName LIKE \"A*\""
				+ "lastName < \"B\"").getQuery();
		QueryFilter filter = new QueryFilter(conditions);
		
		assertTrue(filter.accepts(new StudentRecord(
				"000",
				"Anic",
				"Ana",
				1
		)));
	}
}
