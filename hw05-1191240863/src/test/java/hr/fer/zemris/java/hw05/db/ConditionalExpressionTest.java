package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConditionalExpressionTest {
	
	private static ConditionalExpression condition;

	@BeforeClass
	public static void initalize() {
		condition = new ConditionalExpression(
				FieldValueGetters.FIRST_NAME,
				"J*",
				ComparisonOperators.LIKE
		);
	}
	
	@Test
	public void testSatisfiedTrue() {
		assertTrue(condition.satisfied(new StudentRecord(
				"00",
				"Brezović",
				"Jusufadis",
				1
		)));
	}
		
	@Test
	public void testSatisfiedFalse() {
		assertFalse(condition.satisfied(new StudentRecord(
				"00",
				"Brezović",
				"usufadis",
				1
		)));
	}
	
	@Test
	public void testGettersTrue() {
		assertTrue(condition.getComparisonOperator().satisfied(
				condition.getFieldGetter().get(new StudentRecord(
						"000000000",
						"Bosnic",
						"Janko",
						2
				)), 
				condition.getStringLiteral()
		));
	}
	
	@Test
	public void testGettersFalse() {
		assertFalse(condition.getComparisonOperator().satisfied(
				condition.getFieldGetter().get(new StudentRecord(
						"000000000",
						"Bosnic",
						"Ilija",
						2
				)), 
				condition.getStringLiteral()
		));
	}
}
