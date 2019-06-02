package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception which is thrown when an error in tokenizing text occurs. 
 * 
 * @author Bruno Iljazović
 */
public class LexerException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    
    /*
     * Series of standard exception constructors
     */

    public LexerException() {
    }
    
    public LexerException(String message) {
        super(message);
    }
    
    public LexerException(Throwable cause) {
        super(cause);
    }
    
    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }
}
