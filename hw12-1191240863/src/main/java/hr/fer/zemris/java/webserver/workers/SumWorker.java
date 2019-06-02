package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker adds two integers and displays result as HTML table. Integers are
 * passed through parameters a and b. Default values of 1 and 2 are used if any
 * of them are not provided.
 * 
 * @author Bruno Iljazović
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		final int DEFAULT_A = 1;
		final int DEFAULT_B = 2;
		
		int aValue = DEFAULT_A;
		int bValue = DEFAULT_B;
		
		String a = context.getParameter("a");
		
		try {
			aValue = Integer.parseInt(a);
		} catch(NumberFormatException ex) {}
		
		String b = context.getParameter("b");

		try {
			bValue = Integer.parseInt(b);
		} catch(NumberFormatException ex) {}
		
		context.setTemporaryParameter("a", Integer.toString(aValue));
		context.setTemporaryParameter("b", Integer.toString(bValue));
		context.setTemporaryParameter("zbroj", Integer.toString(aValue + bValue));
		
		context.getDispatcher().dispatchRequest("/private/calc.smscr");
	}
}