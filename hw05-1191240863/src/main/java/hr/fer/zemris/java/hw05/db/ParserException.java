package hr.fer.zemris.java.hw05.db;

/**
 * Exception that is thrown when parser gets invalid input.
 */
public class ParserException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    
    
    /**
     * Instantiates a new parser exception.
     */
    public ParserException() {
    }
    
    /**
     * Instantiates a new parser exception with given message.
     *
     * @param message the message
     */
    public ParserException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new parser exception. with the given cuase.
     *
     * @param cause the cause
     */
    public ParserException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Instantiates a new parser exception with the given message and cause.
     *
     * @param message the message
     * @param cause the cause
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
