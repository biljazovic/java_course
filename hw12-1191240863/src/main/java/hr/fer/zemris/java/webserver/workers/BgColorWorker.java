package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker gets the background color from the parameters and displays a
 * message depending on whether the given color is valid or not. Also displays
 * link to /index2.html page.
 * 
 * @author Bruno Iljazović
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception{

		String bgcolor = context.getParameter("bgcolor");
		
		if (bgcolor != null && bgcolor.matches("[0-9A-Fa-f]{6}")) {
			context.setPersistentParameter("bgcolor", bgcolor);
			context.setTemporaryParameter("message", "Pozadinska boja promijenjena.");
		} else {
			context.setTemporaryParameter("message", "Pozadinska boja <b>nije</b> promijenjena.");
		}
		
		context.getDispatcher().dispatchRequest("/private/setbgcolor.smscr");
	}
}