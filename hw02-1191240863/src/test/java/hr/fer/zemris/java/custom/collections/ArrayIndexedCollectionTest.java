package hr.fer.zemris.java.custom.collections;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

public class ArrayIndexedCollectionTest {
	private static ArrayIndexedCollection col;

	@Before
	public void testInitialize() {
		col = new ArrayIndexedCollection(2);
		col.add("String");
		col.add("Object");
	}
	
	@Test
	public void testAdd() {
		col.add("New Object");

		assertTrue(col.contains("New Object"));
		
		assertTrue(col.size() == 3);
	}
	
	@Test
	public void testGetInBounds() {
		String str = (String)col.get(1);
		
		assertEquals(str, "Object");
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetOutBounds() {
		Object str = col.get(3);
	}

	@Test
	public void testClear() {
		col.clear();
		
		assertTrue(col.size() == 0);
	}
	
	@Test 
	public void testInsertInside() {
		col.insert("New Object", 1);
		
		Object[] expecteds = {"String", "New Object", "Object"};
		Object[] actuals = col.toArray();
		
		assertArrayEquals(expecteds, actuals);
	}
	
	@Test 
	public void testInsertEnd() {
		col.insert("New Object", 2);
		
		Object[] expecteds = {"String", "Object", "New Object"};
		Object[] actuals = col.toArray();
		
		assertArrayEquals(expecteds, actuals);
	}
	
	@Test
	public void testIndexOfFound() {
		assertEquals(0, col.indexOf("String"));
	}

	@Test
	public void testIndexOfNotFound() {
		assertEquals(-1, col.indexOf("Bruno"));
	}
		
	@Test 
	public void testRemove() {
		col.remove(0);
		
		Object[] expecteds = {"Object"};
		Object[] actuals = col.toArray();
		
		assertArrayEquals(expecteds, actuals);
	}
}
