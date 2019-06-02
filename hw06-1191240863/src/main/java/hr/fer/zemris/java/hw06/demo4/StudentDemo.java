package hr.fer.zemris.java.hw06.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Program that reads the studenti.txt file and gets the list of student records
 * from that file. It then uses the {@link Stream} methods to print some result,
 * described in methods of this class.
 * 
 * @author Bruno Iljazović
 */
public class StudentDemo {

	/**
	 * The main method that is called when the program is run. 
	 *
	 * @param args the command line arguments, not used in this program
	 */
	public static void main(String[] args) {
		String filename = "studenti.txt";

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("./" + filename), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.err.println("There was a problem reading the file " + filename + ".");
			System.exit(-1);
		}

		List<StudentRecord> records = convert(lines);
		
		// FIRST QUERY
		System.out.println("Broj studenata koji imaju više od 25 bodova: " + 
				vratiBodovaViseOd25(records));
		System.out.println();

		//SECOND QUERY
		System.out.println("Broj studenata koji su dobili ocjenu 5: " + 
				vratiBrojOdlikasa(records));
		System.out.println();
		
		//THIRD QUERY
		System.out.println("Studenti koji su dobili ocjenu 5: ");
		for (StudentRecord record : vratiListuOdlikasa(records)) {
			printStudent(record);
		}
		System.out.println();

		//FOURTH QUERY
		System.out.println("Studenti koji su dobili ocjenu 5 (Sortirano po ukupnim bodovima): ");
		for (StudentRecord record : vratiSortiranuListuOdlikasa(records)) {
			printStudent(record);
		}
		System.out.println();
		
		//FIFTH QUERY
		System.out.println("Jmbag-ovi studenata koji nisu položili kolegij (od manjeg prema većem):");
		for (String jmbag : vratiPopisNepolozenih(records)) {
			System.out.println(jmbag);
		}
		System.out.println();

		//SIXTH QUERY
		System.out.println("Studenti razvrstani po ocjenama:");
		for (Map.Entry<Integer, List<StudentRecord>> entry : 
			razvrstajStudentePoOcjenama(records).entrySet()) {
			System.out.println("Ocjena " + entry.getKey() + ":");
			for (StudentRecord record : entry.getValue()) {
				printStudent(record);
			}
		}
		System.out.println();
		
		//SEVENTH QUERY
		System.out.println("Broj studenata po ocjenama:");
		for (Map.Entry<Integer, Integer> entry : vratiBrojStudenataPoOcjenama(records).entrySet()) {
			System.out.println("Ocjena " + entry.getKey() + ": " + entry.getValue());
		}
		System.out.println();
		
		//EIGTH QUERY
		System.out.println("Studenti razvrstani po prolasku/padu:");
		Map<Boolean, List<StudentRecord>> map = razvrstajProlazPad(records);
		System.out.println("PROLAZ:");
		for (StudentRecord record : map.get(true)) {
			printStudent(record);
		}
		System.out.println();
		System.out.println("PAD:");
		for (StudentRecord record : map.get(false)) {
			printStudent(record);
		}
		System.out.println();

	}
	
	/**
	 * Prints the student record.
	 *
	 * @param record the student record
	 */
	private static void printStudent(StudentRecord record) {
		System.out.printf("%s %s %s %f %f %f %d%n", record.getJmbag(), record.getLastName(),
				record.getFirstName(), record.getMidtermPoints(), record.getFinalPoints(),
				record.getLabPoints(), record.getFinalGrade());
	}

	/**
	 * Returns the number of students that have more than 25 points (all points
	 * combined).
	 *
	 * @param records
	 *            list of student records
	 * @return number of students that have more than 25 points
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getTotalPoints() > 25.0)
				.count();
	}
	
	/**
	 * Returns the number of students that got the perfect final grade.
	 *
	 * @param records
	 *            list of student records
	 * @return the number of students that got the perfect final grade.
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getFinalGrade() == 5)
				.count();
	}
	
	/**
	 * Returns the list of students that got the perfect final grade.
	 *
	 * @param records
	 *            list of student records
	 * @return the list of students that got the perfect final grade.
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getFinalGrade() == 5)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the list of students that got the perfect final grade, sorted by the
	 * total points (first in the list is the student with the most points).
	 *
	 * @param records
	 *            list of student records
	 * @return the sorted list of students that got the perfect final grade
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getFinalGrade() == 5)
				.sorted((r1, r2) -> Double.compare(
						r2.getTotalPoints(),
						r1.getTotalPoints()))
				.collect(Collectors.toList());
	}

	/**
	 * Returns the list of students that passed, sorted by jmbags.
	 *
	 * @param records
	 *            list of student records
	 * @return the sorted list of students that passed
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
				.filter(record -> record.getFinalGrade() < 2)
				.map(StudentRecord::getJmbag)
				//.sorted((jmbag1, jmbag2) -> jmbag1.compareTo(jmbag2))
				.sorted()
				.collect(Collectors.toList());
	}

	/**
	 * Returns the map where the key is the final grade and the value if the list of
	 * students that got that grade.
	 *
	 * @param records
	 *            list of student records
	 * @return the map of grade -> list of students with the grade
	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.groupingBy(
						StudentRecord::getFinalGrade,
						Collectors.toList()));
	}

	/**
	 * Returns the map where the key is the final grade and the value is the number
	 * of students that got that grade.
	 *
	 * @param records
	 *            list of student records
	 * @return the map of grade -> number of students with the grade.
	 */
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.toMap(
						StudentRecord::getFinalGrade,
						record -> Integer.valueOf(1),
						(i, j) -> i + j));
	}

	/**
	 * Returns the map where the true is mapped to the list of students that passed,
	 * and false is mapped to the list of other students.
	 *
	 * @param records
	 *            list of student records
	 * @return the map : true->list of students that passed, false->that didn't pass
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.partitioningBy(record -> record.getFinalGrade() > 1));
	}

	/**
	 * Converts the list lines from the text file into the list of
	 * {@link StudentRecord}, or throws the exception if any of the lines in the
	 * file is invalid student record.
	 *
	 * @param lines
	 *            the list of lines of the file
	 * @return the list of student records
	 * @throws IllegalArgumentException
	 *             if the provided list of lines is invalid list of student records
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> records = new ArrayList<>();

		for (String line : lines) {
			line = line.trim();
			if (line.equals(""))
				continue;

			String[] array = line.split("\\s+");

			if (!array[0].matches("\\d{10}")) {
				throw new IllegalArgumentException("Invalid jmbag. Was " + array[0] + ".");
			}

			if (array.length < 7) {
				throw new IllegalArgumentException("Each line must contain at least 7 elements.");
			}

			StringBuilder lastName = new StringBuilder();
			for (int i = 1; i < array.length - 5; ++i) {
				if (lastName.length() != 0) {
					lastName.append(" ");
				}
				lastName.append(array[i]);
			}

			try {
				records.add(new StudentRecord(array[0], lastName.toString(),
						array[array.length - 5], Double.parseDouble(array[array.length - 4]),
						Double.parseDouble(array[array.length - 3]),
						Double.parseDouble(array[array.length - 2]),
						Integer.parseInt(array[array.length - 1])));
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(
						"Last four elements must be valid numbers." + "Line was " + line + " .");
			}
		}

		return records;
	}

}
