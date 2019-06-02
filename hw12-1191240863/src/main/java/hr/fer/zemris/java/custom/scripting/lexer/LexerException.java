package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception which is thrown when an error in tokenizing text occurs. 
 * 
 * @author Bruno Iljazović
 */
public class LexerException extends RuntimeException {

	/** constant serialVersionUID */
    private static final long serialVersionUID = 0L;
    
    /**
     * Instantiates new Lexer exception
     */
    public LexerException() {
    }
    
    /**
     * Instantiates new Lexer exception
     * 
     * @param message an exception message
     */
    public LexerException(String message) {
        super(message);
    }
    
    /**
     * Instantiates new Lexer exception
     * 
     * @param cause an exception cause
     */
    public LexerException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Instantiates new Lexer exception
     * 
     * @param message an exception message
     * @param cause an exception cause
     */
    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }
}
