package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueWrapperTest {
	
	private static final double THRESHOLD = 1e-6;

	@Test
	public void testAddBothIntegers() {
		ValueWrapper v1 = new ValueWrapper(150);
		ValueWrapper v2 = new ValueWrapper(42);
		
		v1.add(v2.getValue());
		
		assertEquals(v1.getValue(), Integer.valueOf(192));
	}

	@Test
	public void testAddOneDouble() {
		ValueWrapper v1 = new ValueWrapper(150.0);
		ValueWrapper v2 = new ValueWrapper(42);
		
		v1.add(v2.getValue());
		
		assertEquals(((Double)v1.getValue()).doubleValue(), 192, THRESHOLD);
	}
	
	@Test
	public void testAddBothNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.add(v2.getValue());
		
		assertEquals(v1.getValue(), Integer.valueOf(0));
	}

	@Test
	public void testAddStringDoubleInteger() {
		ValueWrapper v1 = new ValueWrapper("1.2");
		ValueWrapper v2 = new ValueWrapper(3);
		
		v1.add(v2.getValue());
		
		assertEquals(((Double)v1.getValue()).doubleValue(), 4.2, THRESHOLD);
	}

	@Test
	public void testAddIntegerStringInteger() {
		ValueWrapper v1 = new ValueWrapper(15);
		ValueWrapper v2 = new ValueWrapper("20");
		
		v1.add(v2.getValue());
		
		assertEquals(v1.getValue(), Integer.valueOf(35));
	}

	@Test
	public void testAddStringIntegerStringDouble() {
		ValueWrapper v1 = new ValueWrapper("15");
		ValueWrapper v2 = new ValueWrapper("20.5");
		
		v1.add(v2.getValue());
		
		assertEquals(((Double)v1.getValue()).doubleValue(), 35.5, THRESHOLD);
	}
	
	@Test
	public void testAddStringDoubleScientificNotation() {
		ValueWrapper v1 = new ValueWrapper("2.5e2");
		ValueWrapper v2 = new ValueWrapper("20");
		
		v1.add(v2.getValue());
		
		assertEquals(((Double)v1.getValue()).doubleValue(), 270, THRESHOLD);
	}
	
	@Test (expected = RuntimeException.class)
	public void testAddInvalidString() {
		ValueWrapper v1 = new ValueWrapper("15a");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));
		
		v1.add(v2.getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testAddInvalidString2() {
		ValueWrapper v1 = new ValueWrapper("15.2");
		ValueWrapper v2 = new ValueWrapper("null");
		
		v1.add(v2.getValue());
	}
	
	@Test
	public void testArguemntUnchanged() {
		ValueWrapper v1 = new ValueWrapper("2");
		ValueWrapper v2 = new ValueWrapper(null);
		
		assertNull(v2.getValue());
	}

	@Test (expected = ArithmeticException.class)
	public void testDivisonbyZeroIntegers() {
		ValueWrapper v1 = new ValueWrapper("5");
		ValueWrapper v2 = new ValueWrapper("0");
		
		v1.divide(v2.getValue());
	}

	@Test
	public void testDivisionbyZeroDoubles() {
		ValueWrapper v1 = new ValueWrapper("5.5");
		ValueWrapper v2 = new ValueWrapper("0");
		
		v1.divide(v2.getValue());
		
		assertEquals(Double.POSITIVE_INFINITY, v1.getValue());
	}
	
	@Test
	public void testDivisionbyZeroDoublesNegative() {
		ValueWrapper v1 = new ValueWrapper("-5.5");
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.divide(v2.getValue());
		
		assertEquals(Double.NEGATIVE_INFINITY, v1.getValue());
	}
	
	@Test
	public void testSubtract() {
		ValueWrapper v1 = new ValueWrapper("15.5");
		ValueWrapper v2 = new ValueWrapper("20.8");
		
		v1.subtract(v2.getValue());
		
		assertEquals(((Double)v1.getValue()).doubleValue(), -5.3, THRESHOLD);
	}
	
	@Test
	public void testMultiply() {
		ValueWrapper v1 = new ValueWrapper("2");
		ValueWrapper v2 = new ValueWrapper("10");
		
		v1.multiply(v2.getValue());
		
		assertEquals(v1.getValue(), Integer.valueOf(20));
	}
	
	@Test
	public void testDivideDouble() {
		ValueWrapper v1 = new ValueWrapper(1.0);
		ValueWrapper v2 = new ValueWrapper(2);
		
		v1.divide(v2.getValue());
		
		assertEquals(((Double)v1.getValue()).doubleValue(), 0.5, THRESHOLD);
	}
	
	@Test
	public void testDivideInteger() {
		ValueWrapper v1 = new ValueWrapper(1);
		ValueWrapper v2 = new ValueWrapper(2);
		
		v1.divide(v2.getValue());
		
		assertEquals(v1.getValue(), Integer.valueOf(0));
	}
	
	@Test
	public void testNumCompareEquals() {
		ValueWrapper v1 = new ValueWrapper(1);
		ValueWrapper v2 = new ValueWrapper("1.0");
		
		assertEquals(v1.numCompare(v2.getValue()), 0);
	}
	
	@Test 
	public void testNumCompareLess() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper("2.0");
		
		assertTrue(v1.numCompare(v2.getValue()) < 0);
	}
		
	@Test 
	public void testNumCompareGreater() {
		ValueWrapper v1 = new ValueWrapper(50.2);
		ValueWrapper v2 = new ValueWrapper("2e1");
		
		assertTrue(v1.numCompare(v2.getValue()) > 0);
	}
}
