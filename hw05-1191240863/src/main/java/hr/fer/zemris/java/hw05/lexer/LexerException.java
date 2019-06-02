package hr.fer.zemris.java.hw05.lexer;

/**
 * Exception which is thrown when an error in tokenizing text occurs. 
 * 
 * @author Bruno Iljazovic
 */
public class LexerException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 0L;
    
    /**
     * Instantiates a new lexer exception.
     */
    public LexerException() {
    }
    
    /**
     * Instantiates a new lexer exception with the given message.
     *
     * @param message the message
     */
    public LexerException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new lexer exception with the given cause.
     *
     * @param cause the cause
     */
    public LexerException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Instantiates a new lexer exception with the given message and cause.
     *
     * @param message the message
     * @param cause the cause
     */
    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }
}
