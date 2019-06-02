package hr.fer.zemris.java.hw05.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple database emulator. Program reads the database.txt file which contains data for students.
 * Attributes are: jmbag, lastName, firstName, finalGrade.
 * <p>User can then input queries which 
 * are parsed by {@link QueryParser}. Program outputs text table of students which satisfy the
 * query. 
 * <p>If the query is direct query ({@link QueryParser#isDirectQuery}) database uses index
 * by jmbags. No other indexes are available.
 * <p>User should input "exit" to end the program
 * 
 * @author Bruno Iljazović
 */
public class StudentDB {

	/**
	 * The main method.
	 *
	 * @param args not used
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		String filename = "database.txt";
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(
					Paths.get("./" + filename), 
					StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.err.println("There was a problem reading the file " + filename + ".");
			System.exit(-1);
		}
		
		StudentDatabase database = null;

		try {
			database = new StudentDatabase(lines);
		} catch(IllegalArgumentException ex) {
			System.err.println(ex.getMessage());
			System.exit(-1);
		}

		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));	

		while (true) {
			System.out.print("> ");

			String line = reader.readLine();
			
			line = line.trim();
			String[] array = line.split("\\s+", 2);
			
			if (array.length == 1 && array[0].equals("exit")) {
				System.out.println("Goodbye!");
				break;
			} else if (array[0].equals("query")) {
				if (array.length == 1) {
					System.out.println("No query given.");
					continue;
				}

				try {
					processQuery(database, array[1]);
				} catch(ParserException ex) {
					System.out.println(ex.getMessage());
				}
			} else {
				System.out.println("Unknown command '" + line + "'.");
			}
			
		}
		
	}
	
	/**
	 * Processes query inputed by user. Prints the table of students records that satisfy the 
	 * query, or throws an exception if the query is invalid.
	 *
	 * @param database the database
	 * @param query the query
	 * 
	 * @throws ParserException if the query string is invalid
	 */
	private static void processQuery(StudentDatabase database, String query) {
		QueryParser parser = new QueryParser(query);

		List<StudentRecord> records;

		if (parser.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");

			StudentRecord record = database.forJMBAG(parser.getQueriedJMBAG());

			records = new ArrayList<StudentRecord>();
			if (record != null) records.add(record);
		} else {
			records = database.filter(new QueryFilter(parser.getQuery()));
		}
		
		printStudentRecordList(records);
	}
	
	/**
	 * Returns the string formed from given number of given character
	 *
	 * @param ch the character to be repeated
	 * @param count the count
	 * @return the string of character repeated count times
	 */
	private static String repeatChar(char ch, int count) {
		char[] result = new char[count];
		Arrays.fill(result, ch);
		return new String(result);
	}
	
	/**
	 * Prints the text table of a given list of student records. Example:
	 * 
<pre>+============+===========+==========+===+
| 0000000007 | Čima      | Sanjin   | 4 |
| 0000000010 | Dokleja   | Luka     | 3 |
| 0000000022 | Jurina    | Filip    | 3 |
| 0000000040 | Mišura    | Zrinka   | 5 |
| 0000000043 | Perica    | Krešimir | 4 |
| 0000000049 | Saratlija | Branimir | 2 |
| 0000000050 | Sikirica  | Alen     | 3 |
| 0000000054 | Šamija    | Pavle    | 3 |
+============+===========+==========+===+ 
Records selected: 8<pre>
	 *
	 * @param records the student records
	 */
	private static void printStudentRecordList(List<StudentRecord> records) {
		int maxLastNameWidth = 0;
		int maxFirstNameWidth = 0;

		for (StudentRecord record : records) {
			maxLastNameWidth = Math.max(maxLastNameWidth, record.getLastName().length());
			maxFirstNameWidth = Math.max(maxFirstNameWidth, record.getFirstName().length());
		}

		if (!records.isEmpty()) {
			String spacer = "+" + repeatChar('=', 12) + "+" + repeatChar('=', maxLastNameWidth + 2)
							+ "+" + repeatChar('=', maxFirstNameWidth + 2) + "+===+";

			System.out.println(spacer);
			for (StudentRecord record : records) {
				System.out.printf("| %s | ", record.getJmbag());
				System.out.printf("%" + -maxLastNameWidth + "s | ", record.getLastName());
				System.out.printf("%" + -maxFirstNameWidth + "s | ", record.getFirstName());
				System.out.printf("%d |%n", record.getFinalGrade());
			}
			System.out.println(spacer);

		}
		
		System.out.printf("Records selected: %d%n", records.size());
	}
}
