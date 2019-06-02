package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class FieldValueGettersTest {
	
	private static StudentRecord record;

	@BeforeClass
	public static void initalize() throws IOException {
		record = new StudentRecord("0000000003", "Brezović", "Jusufadis", 1);
	}
	
	@Test
	public void testFirstName() {
		assertEquals("Jusufadis", FieldValueGetters.FIRST_NAME.get(record));
	}

	@Test
	public void testLastName() {
		assertEquals("Brezović", FieldValueGetters.LAST_NAME.get(record));
	}

	@Test
	public void testJmbag() {
		assertEquals("0000000003", FieldValueGetters.JMBAG.get(record));
	}
}
