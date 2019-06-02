package hr.fer.zemris.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ComplexTest {
	
	private final double treshold = 1e-6;
	
	private Complex a, b;
	
	@Before
	public void initialize() {
		a = new Complex(1, -2);
		b = new Complex(10.1, 2.9);
	}

	@Test
	public void testModule() {
		double actual = b.module();
		double expected = 10.5080921198851;
		assertEquals(expected, actual, treshold);
	}
	
	@Test
	public void testMultiply() {
		Complex expected = new Complex(15.9, -17.3);
		Complex actual = a.multiply(b);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testDivide() {
		Complex expected = new Complex(0.03894222061, -0.20920123166);
		Complex actual = a.divide(b);
		assertTrue(actual.equals(expected, treshold));
	}
	
	
	@Test
	public void testAdd() {
		Complex expected = new Complex(11.1, 0.9);
		Complex actual = a.add(b);
		assertTrue(actual.equals(expected, treshold));
	}
	
	
	@Test
	public void testSub() {
		Complex expected = new Complex(-9.1, -4.9);
		Complex actual = a.sub(b);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testNegate() {
		Complex expected = new Complex(-10.1, -2.9);
		Complex actual = b.negate();
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testPower() {
		Complex expected = new Complex(775.478, 863.098);
		Complex actual = b.power(3);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test (expected = ArithmeticException.class)
	public void testPowerNegative() {
		Complex actual = b.power(-3);
	}
	
	@Test
	public void testRoot() {
		List<Complex> actual = b.root(3);
		List<Complex> expected = Arrays.asList(new Complex(2.1808155462, 0.2038475808),
				new Complex(-1.2669449566397, 1.7867178735), new Complex(-0.9138705895, -1.9905654544));
		for (int i = 0; i < 3; i++) {
			assertTrue(actual.get(i).equals(expected.get(i), treshold));
		}
	}
	
	@Test
	public void testParseNormal() {
		Complex expected = Complex.parse("1-i2");
		Complex actual = a;
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test 
	public void testParseLoneI() {
		Complex expected = Complex.parse("1-i");
		Complex actual = new Complex(1, -1);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test 
	public void testParseWithoutReal() {
		Complex expected = Complex.parse("-i3.5");
		Complex actual = new Complex(0, -3.5);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test 
	public void testParseWithoutImaginary() {
		Complex expected = Complex.parse("-3e-3");
		Complex actual = new Complex(-0.003, 0);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test 
	public void testParseWithSpaces() {
		Complex expected = Complex.parse("10.1 + i2.9");
		Complex actual = b;
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testParseEmptyString() {
		Complex expected = Complex.parse("");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testParseIllegalSpaces() {
		Complex expected = Complex.parse("2 3 + i2");
	}

	@Test (expected = IllegalArgumentException.class)
	public void testParseDoubleImaginary() {
		Complex expected = Complex.parse("3 + ii2");
	}

	@Test (expected = IllegalArgumentException.class)
	public void testImaginayUnitAfterNumber() {
		Complex expected = Complex.parse("3 + 2i");
	}
}
