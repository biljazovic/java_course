package hr.fer.zemris.java.hw05.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class SimpleHashtabeTest {

	private SimpleHashtable<String, Integer> table;
	
	@Before
	public void initialize() {
		table = new SimpleHashtable<String, Integer>();
		table.put("AA", 1);
		table.put("BB", 2);
		table.put("CC", 3);
	}
	
	
	@Test
	public void testPutOverwrites() {
		table.put("BB", 4);
		
		assertEquals(Integer.valueOf(4), table.get("BB"));
	}
	
	@Test
	public void testSize() {
		assertEquals(3, table.size());
	}
	
	@Test
	public void testRemove() {
		table.remove("AA");
		
		assertNull(table.get("AA"));
		assertEquals(2, table.size());
	}
	
	@Test
	public void testContainsKey() {
		assertTrue(table.containsKey("AA"));
		assertFalse(table.containsKey("DD"));
	}
	
	@Test
	public void testContainsValue() {
		assertTrue(table.containsValue(3));
		table.remove("CC");
		assertFalse(table.containsValue(3));
	}
	
	@Test
	public void testIterator() {
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = table.iterator();
		String string = "";
		while (iterator.hasNext()) {
			string += iterator.next().getKey();
		}
		assertEquals("AABBCC", string);
	}
	
	@Test
	public void testIteratorRemove() {
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = table.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		assertTrue(table.isEmpty());
	}
	
	@Test (expected = ConcurrentModificationException.class)
	public void testIteratorRemoveModificationException() {
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = table.iterator();
		iterator.next();
		iterator.remove();
		table.remove("BB");
		iterator.next();
	}
	
	@Test (expected = IllegalStateException.class)
	public void testIteratorRemoveTwiceAfterNext() {
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = table.iterator();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}
}
