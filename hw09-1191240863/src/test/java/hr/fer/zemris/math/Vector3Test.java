package hr.fer.zemris.math;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;

import org.junit.Test;

public class Vector3Test {
	
	Vector3 v, w;
	
	final double treshold = 1E-6;
	
	@Before
	public void initialize() {
		v = new Vector3(1, 2, 3);
		w = new Vector3(-1.5, 2.4, 5.1);
	}

	@Test
	public void testNorm() {
		double actual = v.norm();
		double expected = 3.74165738677;
		assertEquals(expected, actual, treshold);
	}
	
	@Test
	public void testNormalized() {
		Vector3 vNormalized = v.normalized();
		assertEquals(vNormalized.norm(), 1, treshold);
	}
	
	@Test
	public void testSub() {
		Vector3 actual = v.sub(w);
		Vector3 expected = new Vector3(1 + 1.5, 2 - 2.4, 3 - 5.1);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testAdd() {
		Vector3 actual = v.add(w);
		Vector3 expected = new Vector3(1 - 1.5, 2 + 2.4, 3 + 5.1);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testDot() {
		double actual = v.dot(w);
		double expected = 18.59999999999;
		assertEquals(expected, actual, treshold);
	}
	
	@Test
	public void testCross() {
		Vector3 actual = v.cross(w);
		Vector3 expected = new Vector3(3, -9.6, 5.4);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testScale() {
		Vector3 actual = v.scale(-2);
		Vector3 expected = new Vector3(-2, -4, -6);
		assertTrue(actual.equals(expected, treshold));
	}
	
	@Test
	public void testCosAngle() {
		double actual = v.cosAngle(w);
		double expected = 0.8522789688037332;
		assertEquals(expected, actual, treshold);
		
	}
	
	@Test
	public void testToArray() {
		double[] expected = new double[] {-1.5, 2.4, 5.1};
		double[] actual = w.toArray();
		assertTrue(Arrays.equals(expected, actual));
	}
}
