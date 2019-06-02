package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComparisonOperatorsTest {

	@Test
	public void testLikeWithWildcard() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AAbbcaBC", "AA*BC"));
	}

	@Test
	public void testLikeWithoutWildcard() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AABC", "AABC"));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testLikeTwoWildCards() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AAabbacBC", "AA*BC*"));
	}

	@Test
	public void testLikeEmptyWildCard() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AABB", "AA*BB"));
	}

	@Test
	public void testLess() {
		assertTrue(ComparisonOperators.LESS.satisfied("Ana", "Anaa"));
	}

	@Test
	public void testLessEqual() {
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Ana", "Anaa"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Anaa", "Anaa"));
	}

	@Test
	public void testGreater() {
		assertTrue(ComparisonOperators.GREATER.satisfied("Anaa", "Ana"));
	}

	@Test
	public void testGreaterEqual() {
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Anaa", "Ana"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Anaa", "Anaa"));
	}

	@Test
	public void testEquals() {
		assertTrue(ComparisonOperators.EQUALS.satisfied("Anaa", "Anaa"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("Anaa", "Ana"));
	}

	@Test
	public void testNotEquals() {
		assertFalse(ComparisonOperators.NOT_EQUALS.satisfied("Anaa", "Anaa"));
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("Anaa", "Ana"));
	}
}
