package hr.fer.zemris.java.webserver;

/**
 * Web worker that processes client request.
 * 
 * @author Bruno Iljazović
 */
public interface IWebWorker {

	/**
	 * Processes the client request and possibly outputs the response using the
	 * {@link RequestContext} write methods.
	 *
	 * @param context the context
	 * @throws Exception if an error occurred during processing
	 */
	public void processRequest(RequestContext context) throws Exception;
}