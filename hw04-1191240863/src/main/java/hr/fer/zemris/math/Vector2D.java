package hr.fer.zemris.math;

/**
 * This class represents radius vector. Provides methods to rotate, translate and scale the vector.
 * Vectors are in two dimensions.
 */
public class Vector2D {
	
	/*
	 * x and y coordinates of the vector
	 */
	private double x;
	private double y;
	
	/**
	 * If the two numbers are different by no more than THRESHOLD then they are considered equal.
	 */
	public static final double THRESHOLD = 1e-6;
	
	/**
	 * Instantiates new Vector2D from given x and y coordinates.
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the x coordinate.
	 *
	 * @return the x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y coordinate.
	 *
	 * @return the y coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translates this by the given offset.
	 *
	 * @param offset the offset
	 */
	public void translate(Vector2D offset) {
		x += offset.getX();
		y += offset.getY();
	}
	
	/**
	 * Returns the translated vector by the given offset, without modifying this one.
	 *
	 * @param offset the offset
	 * @return the translated vector
	 */
	public Vector2D translated(Vector2D offset) {
		Vector2D result = copy();
		result.translate(offset);
		return result;
	}
	
	/**
	 * Rotates this vector by the given angle in degrees in the anti-clockwise direction.
	 *
	 * @param angle the angle in degrees
	 */
	public void rotate(double angle) {
		//convert to radians
		angle = angle / 360 * 2 * Math.PI;
		
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		
		double newX = x * cosAngle - y * sinAngle;
		double newY = x * sinAngle + y * cosAngle;
		
		x = newX;
		y = newY;
	}
	
	/**
	 * Returns the rotated vector by the given angle in degrees, without modifying this one.
	 * Vector is rotated in anti-clockwise direction.
	 *
	 * @param angle the angle in degrees
	 * @return the rotated vector
	 */
	public Vector2D rotated(double angle) {
		Vector2D result = copy();
		result.rotate(angle);
		return result;
	}

	/**
	 * Scales this vector by the given factor, which can be negative. 
	 *
	 * @param scaler the scaler
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	/**
	 * Returns the scaled vector, without modifying this one.
	 *
	 * @param scaler the scaler
	 * @return the scaled vector
	 */
	public Vector2D scaled(double scaler) {
		Vector2D result = copy();
		result.scale(scaler);
		return result;
	}
	
	/**
	 * Returns new Vector2D with same coordinates as this one.
	 *
	 * @return the copied vector
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	/**
	 * Returns the unit-sized vector that makes the given angle with the x axis.
	 * Angle is given in degrees.
	 *
	 * @param angle the wanted angle with the x axis, in degrees
	 * @return unit sized vector that makes the given angle with the x axis.
	 */
	public static Vector2D directionFromAngle(double angle) {
		Vector2D result = new Vector2D(1, 0);
		result.rotate(angle);
		return result;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) return false;
		
		if (!(arg0 instanceof Vector2D)) return false;
		Vector2D other = (Vector2D) arg0;
		
		if (Math.abs(other.x - this.x) > THRESHOLD) return false;
		if (Math.abs(other.y - this.y) > THRESHOLD) return false;
		
		return true;
	}
}
