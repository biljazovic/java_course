package hr.fer.zemris.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class Vector2DTest {
	
	Vector2D first, second;
	
	@Before
	public void initialize() {
		first = new Vector2D(2, 3);
		second = new Vector2D(-1.5, 3.14);
	}

	@Test
	public void testGetX() {
		Assert.assertEquals(-1.5, second.getX(), Vector2D.THRESHOLD);
	}
	
	@Test
	public void testGetY() {
		Assert.assertEquals(3.14, second.getY(), Vector2D.THRESHOLD);
	}
	
	@Test
	public void testTranslate() {
		second.translate(first);
		
		Vector2D expected = new Vector2D(0.5, 6.14);
		
		Assert.assertEquals(expected, second);
	}
	
	@Test
	public void testRotate() {
		second.rotate(60);
		
		Vector2D expected = new Vector2D(-3.4693197679, 0.2709618943);
		
		Assert.assertEquals(expected, second);
	}

	@Test
	public void testScale() {
		first.scale(1.5);
		
		Vector2D expected = new Vector2D(3, 4.5);
		
		Assert.assertEquals(expected, first);
	}
	
	@Test
	public void testTranslated() {
		Vector2D actual = second.translated(first);
		Vector2D expected = new Vector2D(0.5, 6.14);
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRotated() {
		Vector2D actual = second.rotated(-500);
		Vector2D expected = new Vector2D(3.1674197591, -1.4411981369);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testScaled() {
		Vector2D actual = first.scaled(-0.5);
		Vector2D expected = new Vector2D(-1, -1.5);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCopy() {
		Vector2D secondCopy = second.copy();
		
		//check that copy isn't just returning given reference
		Assert.assertFalse(secondCopy == second);

		Assert.assertEquals(second, secondCopy);
	}
	
	@Test
	public void testDirectionFromAngle() {
		Vector2D expected = new Vector2D(0.7071067812, 0.7071067812);
		
		Assert.assertEquals(expected, Vector2D.directionFromAngle(45));
	}
}
