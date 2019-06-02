package hr.fer.zemris.java.webserver;

/**
 * Program that starts the server with the configuration file set to
 * $(projectRoot)/config/server.properties
 * 
 * @author Bruno Iljazović
 */
public class HTTPServerDemo {

	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args not used here
	 */
	public static void main(String[] args) {
		SmartHttpServer server = new SmartHttpServer("config/server.properties");
		server.start();
	}

}
