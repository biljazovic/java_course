package hr.fer.zemris.java.custom.collections;

/**
 * This class represents the exception that is usually called when the stack is empty 
 * and its top element is accessed.
 * 
 * @author Bruno Iljazović
 */
public class EmptyStackException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /*
     * Series of standard exception constructors
     */

    public EmptyStackException() {
    }
    
    public EmptyStackException(String message) {
        super(message);
    }
    
    public EmptyStackException(Throwable cause) {
        super(cause);
    }
    
    public EmptyStackException(String message, Throwable cause) {
        super(message, cause);
    }
}
