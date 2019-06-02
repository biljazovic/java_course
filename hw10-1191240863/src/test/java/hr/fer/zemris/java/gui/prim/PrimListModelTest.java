package hr.fer.zemris.java.gui.prim;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PrimListModelTest {
	
	PrimListModel model;
	
	@Before
	public void init() {
		model = new PrimListModel();
	}

	@Test
	public void testNoPrimes() {
		assertEquals(1, model.getSize());
		assertEquals(Integer.valueOf(1), model.getElementAt(0));
	}
	
	@Test
	public void testFewPrimes() {
		model.next();
		model.next();
		model.next();
		assertEquals(4, model.getSize());
		assertEquals(Integer.valueOf(2), model.getElementAt(1));
		assertEquals(Integer.valueOf(3), model.getElementAt(2));
		assertEquals(Integer.valueOf(5), model.getElementAt(3));
	}
	
	@Test
	public void test1000prime() {
		for (int i = 0; i < 1000; i++) {
			model.next();
		}
		assertEquals(Integer.valueOf(7919), model.getElementAt(1000));
	}

}
