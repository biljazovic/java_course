package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception that is thrown when parser gets invalid input.
 */
public class SmartScriptParserException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    
    /*
     * Series of standard exception constructors
     */

    public SmartScriptParserException() {
    }
    
    public SmartScriptParserException(String message) {
        super(message);
    }
    
    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }
    
    public SmartScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
