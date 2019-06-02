package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple file search program. Program should get as a command line argument
 * folder with documents to provide search for. Then, three commands and exit
 * command are supported. Query ... command searches the documents and provides
 * 10 closest ones to the list of words given through parameters. They are
 * ranked by similarity with the query. Results lists the results again. Type
 * index prints the document with given index on the screen.
 * 
 * @author Bruno Iljazović
 */
public class Konzola {
	
	/** File with stop words. */
	private static final String STOP_WORDS_FILE = "stopWords.txt";
	
	/** The word regex. */
	private static Pattern word = Pattern.compile("(\\p{IsAlphabetic})+");
	
	/** Used for comparing double numbers. */
	private static double EPS = 1e-5;
	
	/** The prompt string. */
	private static String promptString = "Enter command > ";
	
	/** The document count. */
	private static int documentCount;
	
	/** The word count. */
	private static int wordCount;

	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Required number of arguments is 1.");
			return;
		}
		
		Path dir = Paths.get(args[0]);
		
		if (!Files.isDirectory(dir)) {
			System.out.println("Argument must be path to a valid directory.");
			return;
		}
		
		Map<String, IntPair> vocabulary;
		try {
			vocabulary = generateVocabulary(dir);
		} catch (IOException e) {
			System.out.println("Error while reading the documents.");
			return;
		}
		
		System.out.println("Veličina rječnika je " + wordCount + ".");
		
		List<Document> documents = null;
		try {
			documents = generateDocuments(dir, vocabulary);
		} catch (IOException e) {
			System.out.println("Error while reading the documents.");
			return;
		}
		
		try {
			startShell(vocabulary, documents);
		} catch(IOException ex) { }
	}
	
	/**
	 * Starts the main shell.
	 *
	 * @param vocabulary the vocabulary
	 * @param documents the documents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void startShell(Map<String, IntPair> vocabulary, List<Document> documents) 
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		List<Result> results = null;

		while (true) {
			System.out.print(promptString);
			String line = reader.readLine();
			String[] lineArr = line.trim().split("\\s+");
			if (lineArr.length == 0) {
				System.out.println("No command given.");
				continue;
			}
			switch(lineArr[0]) {
			case "exit":
				return;
			case "query":
				List<String> words = new ArrayList<>();
				for (int i = 1; i < lineArr.length; i++) {
					String word = lineArr[i].toLowerCase();
					if (vocabulary.get(word) == null) continue;
					words.add(word);
				}
				System.out.print("Query is: ["); 
				for (int i = 0; i < words.size(); i++) {
					if (i > 0) System.out.print(", ");
					System.out.print(words.get(i));
				}
				System.out.println("]");
				Vector tfidf = generateVector(words, vocabulary);
				results = generateResults(tfidf, documents);
				printResults(results);
				break;
			case "results":
				printResults(results);
				break;
			case "type":
				if (lineArr.length < 2) {
					System.out.println("Not eoungh arguments.");
					break;
				}
				printResult(results, lineArr[1]);
				break;
			default:
				System.out.println("Unknown command.");
			}

		}
	}
	
	/**
	 * Prints the document with the given index.
	 *
	 * @param results the results
	 * @param index the index
	 */
	private static void printResult(List<Result> results, String index) {
		int indexInt;
		try {
			indexInt = Integer.parseInt(index);
		} catch(NumberFormatException ex) {
			System.out.println("Invalid argument.");
			return;
		}
		if (results == null) {
			System.out.println("No query given.");
			return;
		}
		if (indexInt < 0 || indexInt >= results.size()) {
			System.out.println("Index out of bounds. There are " + results.size() + " results.");
			return;
		}
		Path path = results.get(indexInt).getDocumentPath();
		System.out.println("Dokument: " + path.normalize().toAbsolutePath().toString());
		System.out.println("------------------------------------------------------------------");
		try {
			Files.readAllLines(path).forEach(System.out::println);
		} catch (IOException e) {
			System.out.println("Error while reading the document.");
		}
	}

	/**
	 * Prints the results list.
	 *
	 * @param results the results
	 */
	private static void printResults(List<Result> results) {
		if (results == null) {
			System.out.println("No query given.");
			return;
		}
		if (results.isEmpty()) {
			System.out.println("No document matches given query.");
			return;
		}
		for (int i = 0; i < results.size(); i++) {
			System.out.printf("[%2d](%.4f) %s%n", i, results.get(i).value,
					results.get(i).documentPath.normalize().toAbsolutePath().toString());
		}
	}

	/**
	 * Generates results of the query.
	 *
	 * @param tfidf the tfidf vector
	 * @param documents the documents
	 * @return the list of results.
	 */
	private static List<Result> generateResults(Vector tfidf, List<Document> documents) {
		List<Result> results = new ArrayList<>();
		for (Document document : documents) {
			double product = Vector.cos(document.getTfidf(), tfidf);
			results.add(new Result(product, document.getFilePath()));
		}
		Collections.sort(results);
		if (results.size() > 10) results = results.subList(0, 10);
		int ind = 0;
		while (ind < results.size() && results.get(ind).value > EPS) {
			ind++;
		}
		return results.subList(0, ind);
	}

	/**
	 * Generates tfidf vector from the list of words.
	 *
	 * @param wordList the word list
	 * @param vocabulary the vocabulary
	 * @return the generated vector
	 */
	private static Vector generateVector(List<String> wordList, Map<String, IntPair> vocabulary) {
		Vector tfidf = new Vector(wordCount);
		int[] frequency = new int[wordCount];
		for (String word : wordList) {
			IntPair ip = vocabulary.get(word);
			if (ip == null) continue;
			tfidf.set(ip.x, Math.log((double)documentCount / ip.y));
			frequency[ip.x]++;
		}
		for (int i = 0; i < wordCount; i++) {
			if (frequency[i] == 0) continue;
			tfidf.set(i, frequency[i] * tfidf.get(i));
		}
		return tfidf;
	}

	/**
	 * Generates documents from the given directory.
	 *
	 * @param dir the directory with all documents.
	 * @param vocabulary the vocabulary
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static List<Document> generateDocuments(Path dir, Map<String, IntPair> vocabulary) throws IOException {
		List<Document> documents = new ArrayList<>();
		
		goThroughFiles(dir, (wordList, path) -> {
			documents.add(new Document(generateVector(wordList, vocabulary), path));
		});
		
		return documents;
	}
	
	/**
	 * Reads stop words from the given file.
	 *
	 * @param path the path to the stop words file.
	 * @return the set of stop words.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Set<String> readStopWords(String path) throws IOException {
		Set<String> result = new HashSet<>();
		for (String line : Files.readAllLines(Paths.get(path))) {
			result.add(line.trim());
		}
		return result;
	}
	
	/**
	 * Generates vocabulary from the given directory
	 *
	 * @param dir the directory with all documents.
	 * @return the vocabulary
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Map<String, IntPair> generateVocabulary(Path dir) throws IOException {
		Map<String, IntPair> vocabulary = new HashMap<>();
		Set<String> stopWords = readStopWords(STOP_WORDS_FILE);
		goThroughFiles(dir, (wordList, file) -> {
			Set<String> wordSet = new HashSet<>();
			for (String word : wordList) {
				if (wordSet.contains(word) || stopWords.contains(word)) continue;
				wordSet.add(word);
				IntPair ip = vocabulary.get(word);
				if (ip == null) {
					vocabulary.put(word, new IntPair(wordCount++, 1));
				} else {
					vocabulary.put(word, new IntPair(ip.x, ip.y+1));
				}
			}
			documentCount++;
		});
		return vocabulary;
	}

	/**
	 * Goes through the files and executes the given action on all of them.
	 *
	 * @param dir the directory with all documents
	 * @param action the action to be executed on the documents.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void goThroughFiles(Path dir, BiConsumer<List<String>, Path> action) throws IOException {
		try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
			for (Path file : files) {
				List<String> words = new ArrayList<>();
				Files.lines(file).forEach(line -> {
					Matcher matcher = word.matcher(line);
					while (matcher.find()) {
						words.add(matcher.group(0).toLowerCase());
					}
				});
				action.accept(words, file);
			}
		}
	}
	
	/**
	 * Represents query result.
	 */
	public static class Result implements Comparable<Result> {

		/** The similarity value. */
		double value;
		
		/** The document path. */
		Path documentPath;

		/**
		 * Instantiates a new result.
		 *
		 * @param value the value
		 * @param documentPath the document path
		 */
		public Result(double value, Path documentPath) {
			this.value = value;
			this.documentPath = documentPath;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public double getValue() {
			return value;
		}

		/**
		 * Gets the document path.
		 *
		 * @return the document path
		 */
		public Path getDocumentPath() {
			return documentPath;
		}

		@Override
		public int compareTo(Result o) {
			return Double.compare(o.value, value);
		}
	}
	
	/**
	 * Represents an integer pair.
	 */
	public static class IntPair {
		
		/** The first integer. */
		private int x;
		
		/** The second integer. */
		private int y;

		/**
		 * Instantiates a new integer pair.
		 *
		 * @param x the x
		 * @param y the y
		 */
		public IntPair(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		/**
		 * Gets the x.
		 *
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * Gets the y.
		 *
		 * @return the y
		 */
		public int getY() {
			return y;
		}
	}
}
