package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class StudentDatabaseTest {

	private static StudentDatabase database;
	
	private boolean equalRecords(StudentRecord r1, StudentRecord r2) {
		if (!r1.equals(r2)) return false;
		if (!r1.getFirstName().equals(r2.getFirstName())) return false;
		if (!r1.getLastName().equals(r2.getLastName())) return false;
		if (!r1.getFinalGrade().equals(r2.getFinalGrade())) return false;
		return true;
	}
	
	@BeforeClass
	public static void initalize() throws IOException {
		List<String> lines = Files.readAllLines(
				Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		
		database = new StudentDatabase(lines);
	}

	@Test
	public void testForJmbag() {
		String jmbag = "0000000022";
		StudentRecord record = database.forJMBAG(jmbag);
		
		assertTrue(equalRecords(new StudentRecord(jmbag, "Jurina", "Filip", 3), record));
	}
	
	@Test 
	public void testForJmbagNonExistent() {
		String jmbag = "0000000080";
		StudentRecord record = database.forJMBAG(jmbag);
		
		assertNull(record);
	}
	
	@Test
	public void filterAllTrue() {
		IFilter allTrue = record -> true;
		
		List<StudentRecord> list = database.filter(allTrue);
		
		assertEquals(list.size(), 63);
	}
	
	@Test
	public void filterAllFalse() {
		IFilter allFalse = record -> false;
		
		List<StudentRecord> list = database.filter(allFalse);
		
		assertTrue(list.isEmpty());
	}
}
