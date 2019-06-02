package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception that is thrown when parser gets invalid input.
 */
public class SmartScriptParserException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 0L;
    

    /**
     * Instantiates a new smart script parser exception.
     */
    public SmartScriptParserException() {
    }
    
    /**
     * Instantiates a new smart script parser exception.
     *
     * @param message the message
     */
    public SmartScriptParserException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new smart script parser exception.
     *
     * @param cause the cause
     */
    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Instantiates a new smart script parser exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public SmartScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
