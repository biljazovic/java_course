package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext;

// TODO: Auto-generated Javadoc
/**
 * Demonstrates the {@link SmartScriptEngine}.
 * 
 * @author Bruno Iljazović
 */
public class SmartScriptEngineDemo {

	/**
	 * The main method.
	 *
	 * @param args not used here
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		testOsnovni();
		System.out.println();
		testZbrajanje();
		System.out.println();
		testBrojPoziva();
		System.out.println();
		testFibonacci();
		System.out.println();
		testFibonacciH();
	}

	/**
	 * Test fibonacci.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void testFibonacci() throws IOException {
		new SmartScriptEngine(
			readFromDisk("webroot/scripts/fibonacci.smscr"),
			new RequestContext(System.out, null, null, null)
		).execute();
	}

	/**
	 * Test fibonacci html.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void testFibonacciH() throws IOException {
		new SmartScriptEngine(
			readFromDisk("webroot/scripts/fibonaccih.smscr"),
			new RequestContext(System.out, null, null, null)
		).execute();
	}

	/**
	 * Test broj poziva.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void testBrojPoziva() throws IOException {
		Map<String, String> persistentParameters = new HashMap<String, String>();
		persistentParameters.put("brojPoziva", "3");
		
		RequestContext rc = new RequestContext(System.out, null, persistentParameters, null);

		new SmartScriptEngine(
			readFromDisk("webroot/scripts/brojPoziva.smscr"),
			rc
		).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}
	
	/**
	 * Test zbrajanje.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void testZbrajanje() throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		new SmartScriptEngine(
			readFromDisk("webroot/scripts/zbrajanje.smscr"),
			new RequestContext(System.out, parameters, null, null)
		).execute();
	}

	/**
	 * Test osnovni.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void testOsnovni() throws IOException {
		new SmartScriptEngine(
			readFromDisk("webroot/scripts/osnovni.smscr"),
			new RequestContext(System.out, null, null, null)
		).execute();
	}

	/**
	 * Read from disk.
	 *
	 * @param filepath the filepath
	 * @return the document node
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static DocumentNode readFromDisk(String filepath) throws IOException {
		String docBody = null;
		try {
			docBody = new String( 
					Files.readAllBytes(Paths.get(filepath)), 
					StandardCharsets.UTF_8
			);
		} catch (InvalidPathException | NoSuchFileException ex) {
			System.out.println("Invalid filepath!");
			System.exit(-1);
		}
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} 
		return parser.getDocumentNode();
	}

}
