package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Provides a stack of the TurtleStates whose push and pop methods can be useful in drawing certain
 * fractals (by PushCommand and PopCommand).
 * 
 * @author Bruno Iljazović
 */
public class Context { 
	
	/**
	 * Stack of the TurtleStates.
	 */
	private ObjectStack stack;
	
	/**
	 * Instantiates new Context with empty stack.
	 */
	public Context() {
		stack = new ObjectStack();
	}
	
	/**
	 * Returns the TurtleState on the top of the stack.
	 * @return the TurtleState on the top of the stack.
	 */
	public TurtleState getCurrentState() {
		return (TurtleState) stack.peek();
	}
	
	/**
	 * Pushes new TurtleState on the stack.
	 * @param state state to be pushed.
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	/**
	 * Removes the TurtleState on the top of the stack.
	 */
	public void popState() {
		stack.pop();
	}
}
