package hr.fer.zemris.java.webserver;

/**
 * The request path dispatcher.
 * 
 * @author Bruno Iljazović
 */
public interface IDispatcher {

	/**
	 * Processes the given requested path.
	 * 
	 * @param urlPath the requested path
	 * @throws Exception if an error occurred during processing
	 */
	void dispatchRequest(String urlPath) throws Exception;
}