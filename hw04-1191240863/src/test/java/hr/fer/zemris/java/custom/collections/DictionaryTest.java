package hr.fer.zemris.java.custom.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DictionaryTest {
	
	Dictionary dictionary;

	@Before
	public void initialize() {
		dictionary = new Dictionary();
		dictionary.put("Test1", "Test1");
		dictionary.put("Test2", null);
	}
	
	@Test
	public void testIsEmpty() {
		Assert.assertFalse(dictionary.isEmpty());
	}
	
	@Test
	public void testSize() {
		dictionary.put("Test1", "Test1Modified");
		dictionary.put("Test3", null);
		dictionary.put("Test3", "Test3");
		
		Assert.assertEquals(3, dictionary.size());
	}
	
	@Test
	public void testClear() {
		dictionary.clear();
		
		Assert.assertNull(dictionary.get("Test1"));
		Assert.assertEquals(0, dictionary.size());
	}
	
	@Test
	public void testGetNull() {
		Assert.assertNull(dictionary.get("Test2"));
	}
	
	@Test
	public void testGetNonExistent() {
		Assert.assertNull(dictionary.get("Test3"));
	}
	
	@Test 
	public void testGetExistent() {
		Assert.assertEquals("Test1", dictionary.get("Test1"));
	}
	
	@Test
	public void testPutOverride() {
		dictionary.put("Test1", "Test2");
		dictionary.put("Test2", "Test3");
		
		Assert.assertEquals("Test2", dictionary.get("Test1"));
		Assert.assertEquals("Test3", dictionary.get("Test2"));
	}
}
