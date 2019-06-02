package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * Represents one state of the Turtle. Provides access to its position, direction, color and 
 * unit length.
 * @author Bruno Iljazović
 */
public class TurtleState {
	
	/** current position of the turtle. */
	public Vector2D position;
	
	/** current direction of the turtle, represented by a unit sized vector */
	public Vector2D direction;
	
	/** current color of the trace that Turtle leaves */
	public Color color;
	
	/** Length of a unit movement of the turtle. */
	public double unitLength;
	
	/**
	 * Instantiates the new TurtleState from given parameters.
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double unitLength) {
		this.position = position.copy();
		this.direction = direction.copy();
		this.color = color;
		this.unitLength = unitLength;
	}

	/**
	 * Returns new TurtleState which has all values equal to the current one.
	 * @return Copy of this TurtleState
	 */
	public TurtleState copy() {
		return new TurtleState(position, direction, color, unitLength);
	}
}
