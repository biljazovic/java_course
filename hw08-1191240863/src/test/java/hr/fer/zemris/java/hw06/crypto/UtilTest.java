package hr.fer.zemris.java.hw06.crypto;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class UtilTest {
	
	@Test
	public void byteToHexTest() {
		byte[] actual = Util.hextobyte("01aE22");
		byte[] expected = new byte[] {1, -82, 34};

		assertTrue(Arrays.equals(actual, expected));
	}
	
	@Test
	public void hexToByteTest() {
		String actual = Util.bytetohex(new byte[] {0, -50, 100, 126});
		String expected = "00ce647e";

		assertEquals(expected, actual);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void oddSizedString() {
		byte[] ret = Util.hextobyte("00ce5");
	}

	@Test (expected = IllegalArgumentException.class)
	public void invalidCharacterString() {
		byte[] ret = Util.hextobyte("g0");
	}
	
	@Test
	public void zeroLengthString() {
		byte[] actual = Util.hextobyte("");
		assertEquals(0, actual.length);
	}

}
