package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * This program demonstrates the {@link RequestContext} class by creating three
 * files with HTTP response written in them.
 * 
 * @author Bruno Iljazović
 */
public class DemoRequestContext {

	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args
	 *            not used here
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		demo1("primjer1.txt", "ISO-8859-2");
		demo1("primjer2.txt", "UTF-8");
		demo2("primjer3.txt", "UTF-8");
	}

	/**
	 * First demonstration method that writes some special characters to the HTTP
	 * content.
	 *
	 * @param filePath
	 *            name of the file to write the response to
	 * @param encoding
	 *            the encoding to use for the HTTP content
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void demo1(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(),
			new HashMap<String, String>(), new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}

	/**
	 * Second demonstration method which adds some cookies to the HTTP header.
	 *
	 * @param filePath
	 *            name of the file to write the response to
	 * @param encoding
	 *            the encoding to use for the HTTP content
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void demo2(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(),
			new HashMap<String, String>(), new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1", "/"));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/"));
		rc.write("Čevapčići i Šiščevapčići.");

		os.close();
	}
}