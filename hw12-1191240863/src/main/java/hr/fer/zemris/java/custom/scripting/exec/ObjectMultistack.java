package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of objects where each key is mapped to one stack of values. Only
 * possible keys are Strings, and values are instances of {@link ValueWrapper}.
 * Duplicates in the stack are allowed, but null-references are not.
 * <p>
 * Stacks support usual pop and push operations, as well as a method to peek at
 * the top element of the stack, and to check if the stack is empty.
 *
 * @author Bruno Iljazović
 */
public class ObjectMultistack {
	
	/**
	 * Instantiates a new multistack.
	 */
	public ObjectMultistack() {
		map = new HashMap<>();
	}
	
	/**
	 * Pushes the provided value to the stack of the provided key.
	 *
	 * @param name
	 *            the key
	 * @param valueWrapper
	 *            the value to be pushed
	 * @throws NullPointerException
	 *             if the provided value wrapper is null
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (valueWrapper == null) {
			throw new NullPointerException("value wrapper can't be null.");
		}
		map.put(name, new MultiStackEntry(valueWrapper, map.get(name)));
	}
	
	/**
	 * Removes the top element of the stack of the provided key and returns removed
	 * value. If the stack is empty, exception is thrown.
	 *
	 * @param name
	 *            the key
	 * @return removed value
	 * @throws EmptyStackException
	 *             if the stack was empty
	 */
	public ValueWrapper pop(String name) {
		if (map.get(name) == null) {
			throw new EmptyStackException();
		}

		MultiStackEntry popped = map.get(name);
		map.put(name, popped.next);

		return popped.getValueWrapper();
	}
	
	/**
	 * Returns the top element of the stack of the provided key. If the stack is
	 * empty, exception is thrown.
	 *
	 * @param name
	 *            the key
	 * @return top element of the stack
	 * @throws EmptyStackException
	 *             if the stack was empty
	 */
	public ValueWrapper peek(String name) {
		if (map.get(name) == null) {
			throw new EmptyStackException();
		}
		return map.get(name).getValueWrapper();
	}
	
	/**
	 * Checks whether the stack of the provided key is empty or not.
	 *
	 * @param name
	 *            the key
	 * @return true, if, and only if, the stack is empty
	 */
	public boolean isEmpty(String name) {
		return map.get(name) == null;
	}
	
	/** The map that maps Strings to stacks of ValueWrappers */
	private Map<String, MultiStackEntry> map;
	
	/**
	 * Helper class that represents one element in stack. It keeps the value stored
	 * in it and a reference to the next element in stack.
	 */
	private static class MultiStackEntry {
		
		/** The value stored in this element */
		private ValueWrapper valueWrapper;
		
		/** Reference to the next element in the stack. */
		private MultiStackEntry next;

		/**
		 * Instantiates a new stack element
		 *
		 * @param valueWrapper
		 *            the value to be stored in the element
		 * @param next
		 *            reference to the next element in stack from the newly created one
		 */
		public MultiStackEntry(ValueWrapper valueWrapper, MultiStackEntry next) {
			super();
			this.valueWrapper = valueWrapper;
			this.next = next;
		}

		/**
		 * Gets the value stored in this element
		 *
		 * @return the value stored
		 */
		public ValueWrapper getValueWrapper() {
			return valueWrapper;
		}
	}
}
