package hr.fer.zemris.java.hw01;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testovi za program Factorial.
 * 
 * @author Bruno Iljazović
 */
public class FactorialTest {

	@Test(expected = IllegalArgumentException.class)
	public void tooBigNumber() {
		Factorial.computeFactorial(25);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void negativeNumber() {
		Factorial.computeFactorial(-1);
	}

	@Test
	public void positiveNumberWithinBounds() {
		int number = 10;
		long expected = 3628800;
		
		long actual = Factorial.computeFactorial(number);
		
		Assert.assertEquals(expected, actual);
	}

}
