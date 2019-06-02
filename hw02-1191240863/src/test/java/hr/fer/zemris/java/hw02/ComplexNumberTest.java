package hr.fer.zemris.java.hw02;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import hr.fer.zemris.java.hw02.ComplexNumber;

public class ComplexNumberTest {

	private static ComplexNumber c1, c2;
	private double threshold = ComplexNumber.THRESHOLD;
	
	@BeforeClass
	public static void testInitialize() {
		c1 = new ComplexNumber(2, 3);
		c2 = new ComplexNumber(3.5, 1);
	}

	@Test
	public void testGetReal() {
		double expected = 2;
		double actual = c1.getReal();
		assertEquals(expected, actual, threshold);
				
	}

	@Test
	public void testGetImaginary() {
		double expected = 3;
		double actual = c1.getImaginary();
		assertEquals(expected, actual, threshold);
	}
	
	
	@Test
	public void testGetMagnitude() {
		double expected = 3.6055512755;
		double actual = c1.getMagnitude();
		assertEquals(expected, actual, threshold);
	}

	@Test
	public void testGetAngle() {
		double expected = 0.9827937232;
		double actual = c1.getAngle();
		assertEquals(expected, actual, threshold);
	}
	
	@Test
	public void testFromReal() {
		ComplexNumber expected = new ComplexNumber(-3.51, 0);
		ComplexNumber actual = ComplexNumber.fromReal(-3.51);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFromImaginary() {
		ComplexNumber expected = new ComplexNumber(0, 100.1);
		ComplexNumber actual = ComplexNumber.fromImaginary(100.1);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFromMagnitudeAndAngle() {
		ComplexNumber expected = c1;
		ComplexNumber actual = ComplexNumber.fromMagnitudeAndAngle(3.6055512755, 0.9827937232);
		assertEquals(expected, actual);
	}	
	
	@Test
	public void testParseNormal() {
		ComplexNumber expected = c1;
		ComplexNumber actual = ComplexNumber.parse("2+3i");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testParseOnlyImaginary() {
		ComplexNumber expected = new ComplexNumber(0, 1);
		ComplexNumber actual = ComplexNumber.parse("i");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAdd() {
		ComplexNumber expected = new ComplexNumber(5.5, 4);
		ComplexNumber actual = c1.add(c2);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSub() {
		ComplexNumber expected = new ComplexNumber(-1.5, 2);
		ComplexNumber actual = c1.sub(c2);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMul() {
		ComplexNumber expected = new ComplexNumber(4, 12.5);
		ComplexNumber actual = c1.mul(c2);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDiv() {
		ComplexNumber expected = new ComplexNumber(0.7547169811, 0.64150943396);
		ComplexNumber actual = c1.div(c2);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPower() {
		ComplexNumber expected = new ComplexNumber(122, -597);
		ComplexNumber actual = c1.power(5);
		assertEquals(expected, actual);
	}	
	
	@Test
	public void testRoot() {
		ComplexNumber[] expecteds = {
				new ComplexNumber(1.53166788, 0.142496542),
				new ComplexNumber(-0.88923956, 1.25521502),
				new ComplexNumber(-0.642428313, -1.397711561)};
		ComplexNumber[] actuals = c2.root(3);
		assertArrayEquals(expecteds, actuals);
	}
	
	@Test
	public void testToString() {
		String expected = "3.5+i";
		String actual = c2.toString();
		assertEquals(expected, actual);
	}
}
