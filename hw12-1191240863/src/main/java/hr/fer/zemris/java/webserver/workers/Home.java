package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker displays links to scripts and other workers, HTML form for adding
 * two integers, and HTML form for changing the background color.
 * 
 * @author Bruno Iljazović
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		String bgcolor = context.getPersistentParameter("bgcolor");
		
		if (bgcolor == null) {
			bgcolor = "7F7F7F";
		}
		
		context.setTemporaryParameter("background", bgcolor);
		
		context.getDispatcher().dispatchRequest("/private/home.smscr");
	}
}