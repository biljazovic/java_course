package hr.fer.zemris.java.custom.collections;

/**
 * This class represents stack collection. Standard stack operations: push, pop,
 * peek, checking if the stack is empty and clearing the stack are supported.
 * 
 * <p>Newly created stack contains no items.
 * 
 * <p>Items can be any references. Duplicates are allowed, but {@code null} 
 * references are not.
 * 
 * <p>This class uses {@link ArrayIndexedCollection} for storage of elements.
 * 
 * @author Bruno Iljazović
 */
public class ObjectStack {
	
	private ArrayIndexedCollection indexedCollection;
	
	/**
	 * Default constructor, creates empty stack.
	 */
	public ObjectStack() {
		indexedCollection = new ArrayIndexedCollection();
	}
	
	/**
	 * Checks if the stack is empty.
	 * 
	 * @return {@code true} if the stack is empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return indexedCollection.isEmpty();
	}
	
	/**
	 * Returns number of elements in the stack.
	 * 
	 * @return number of elements in the stack
	 */
	public int size() {
		return indexedCollection.size();
	}
	
	/**
	 * Adds new element to the stack. Element should be non-null reference.
	 * 
	 * @param element that should be put on stack
	 * 
	 * @throws NullPointerException if the element is null reference
	 */
	public void push(Object value) {
		indexedCollection.add(value);
	}
		
	/**
	 * Returns the top element of the stack and removes it.
	 * 
	 * @return element removed
	 * 
	 * @throws EmptyStackException if the stack is empty before removal
	 */
	public Object pop() {
		if (indexedCollection.isEmpty()) {
			throw new EmptyStackException("Stack was empty.");
		}
		int endIndex = indexedCollection.size() - 1;

		Object result = indexedCollection.get(endIndex);
		indexedCollection.remove(endIndex);
		
		return result;
	}
	
	/**
	 * Returns top element of the stack without modifying the stack.
	 * 
	 * @return top element of the stack
	 */
	public Object peek() {
		if (indexedCollection.isEmpty()) {
			throw new EmptyStackException("Stack was empty.");
		}
		return indexedCollection.get(indexedCollection.size() - 1);
	}
	
	/**
	 * Clears the stack.
	 */
	public void clear() {
		indexedCollection.clear();
	}
}
