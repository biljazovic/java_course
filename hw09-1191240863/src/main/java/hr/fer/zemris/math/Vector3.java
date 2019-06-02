package hr.fer.zemris.math;

/**
 * Represents an unmodifiable three-dimensional vector.
 * 
 * @author Bruno Iljazović
 */
public final class Vector3 {
	

	/** The x component. */
	private final double x;

	/** The y component. */
	private final double y;
	
	/** The z component. */
	private final double z;

	/**
	 * Instantiates a new vector
	 *
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the x component.
	 *
	 * @return the x component.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y component.
	 *
	 * @return the y component.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the z component.
	 *
	 * @return the z component
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Returns the length of this vector.
	 *
	 * @return the length of this vector
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Returns new vector that is colinear and of the same orientation as this one,
	 * but of length 1.
	 *
	 * @return the normalized vector ( of length 1 )
	 */
	public Vector3 normalized() {
		double norm = norm();
		return new Vector3(
				x / norm, 
				y / norm, 
				z / norm
		);
	}
	
	/**
	 * Adds the given vector to this one and returns the result.
	 *
	 * @param other
	 *            the other vector
	 * @return the result of addition
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(
				x + other.x,
				y + other.y,
				z + other.z
		);
	}
	
	/**
	 * Subtracts the given vector from this one and returns the result.
	 *
	 * @param other
	 *            the other vector
	 * @return the result of subtraction
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(
				x - other.x,
				y - other.y,
				z - other.z
		);
	}
	
	/**
	 * Returns the result of scalar product between this and the given vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return the scalar product
	 */
	public double dot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;

	}
	
	/**
	 * Returns the vector product between this and the given vector.
	 *
	 * @param other
	 *            the other vector
	 * @return the vector product
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(
				y * other.z - z * other.y,
				z * other.x - x * other.z,
				x * other.y - y * other.x
		);
	}
	
	/**
	 * Returns the scaled vector by the given factor
	 *
	 * @param s the factor of scaling
	 * @return the result of scaling
	 */
	public Vector3 scale(double s) {
		return new Vector3(
				x * s,
				y * s,
				z * s
		);
	}
	
	/**
	 * Returns the cosine of the angle between this and the given vectors.
	 *
	 * @param other the other vector
	 * @return the cosine of the angle between vectors
	 */
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (norm() * other.norm());
	}
	
	/**
	 * Returns the 3-sized array of the vector's components.
	 *
	 * @return the 3-sized array of the vector's components
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	/**
	 * Returns true iff the given vector's components are within the distance of
	 * threshold from this vector's components
	 *
	 * @param other
	 *            the other vector
	 * @param treshold
	 *            the threshold
	 * @return true, if this vector is equal to the given one
	 */
	public boolean equals(Vector3 other, double treshold) {
		if (other == null) return false;
		if (Math.abs(x - other.x) > treshold) return false; 
		if (Math.abs(y - other.y) > treshold) return false; 
		if (Math.abs(z - other.z) > treshold) return false; 
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}
}

