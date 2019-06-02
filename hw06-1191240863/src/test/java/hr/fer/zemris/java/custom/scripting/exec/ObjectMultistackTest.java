package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.Test;

public class ObjectMultistackTest {
	
	private ObjectMultistack stack;

	@Before
	public void initalizeTest() {
		stack = new ObjectMultistack();
		stack.push("Ana", new ValueWrapper(1));
		stack.push("Ana", new ValueWrapper("2"));
		stack.push("Ana", new ValueWrapper(3.5));
		stack.push("Branka", new ValueWrapper(5));
		stack.push("apples", new ValueWrapper(10));
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue(stack.isEmpty("Anica"));
		assertFalse(stack.isEmpty("Ana"));
	}
		
	@Test
	public void testPeek() {
		ValueWrapper expected = new ValueWrapper(3.5);

		assertEquals(expected, stack.peek("Ana"));
	}
	
	@Test
	public void testPop() {
		ValueWrapper expected = new ValueWrapper("2");
		stack.pop("Ana");

		assertEquals(expected, stack.peek("Ana"));
	}
	
	@Test
	public void testPopALl() {
		stack.pop("Ana");
		stack.pop("Ana");
		stack.pop("Ana");
		
		assertTrue(stack.isEmpty("Ana"));
	}
	
	@Test (expected = EmptyStackException.class)
	public void testPopEmptyStack() {
		stack.pop("Branka");
		stack.pop("Branka");
	}

	@Test (expected = EmptyStackException.class)
	public void testPeekEmptyStack() {
		stack.peek("oranges");
	}
	
	@Test
	public void testPopEverythingThenPush() {
		stack.pop("Ana");
		stack.pop("Ana");
		stack.pop("Ana");
		
		stack.push("Ana", new ValueWrapper(15));
		
		assertEquals(new ValueWrapper(15), stack.peek("Ana"));
	}
}
