package org.usfirst.frc.team5530.robot.teleop;

public class Vector2 {
	public final double x, y, magnitude, direction;

	/**
	 * Constructs a vector and Initializes it to {0, 0}, Note: {@link Vector2} is immutable
	 */
	public Vector2() {
		this(0, 0);
	}

	/**
	 * Constructs a vector, Note: {@link Vector2} is immutable
	 *
	 * @param x
	 *            The x component of the vector
	 * @param y
	 *            The y component of the vector
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
		this.magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		this.direction = Math.atan(y/x);
	}
	
	/**
	 * Constructs a vector, Note: {@link Vector2} is immutable
	 *
	 * @param magnitude
	 *            The magnitude of the vector
	 * @param direction
	 *            The direction of the vector (0 is towards the opposing alliance)
	 * @param md
	 * 			  Unused variable to separate this from Vector2(x,y).... is there a better way to do this?
	 */
	public Vector2(double magnitude, double direction, boolean md) {
		this.magnitude = magnitude;
		this.direction = direction;
		this.x = magnitude * Math.cos(direction);
		this.y = magnitude * Math.sin(direction);
	}
	
	
	/**
	 * Adds this vector to another
	 *
	 * @param other
	 *            The vector to add
	 * @return The resulting vector
	 */
	public Vector2 add(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}
	
	/**
	 * Subtracts another vector from this one
	 *
	 * @param other
	 *            The vector being subtracted
	 * @return The resulting vector
	 */
	public Vector2 subtract(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	/**
	 * Multiplies this vector by a scaler
	 *
	 * @param scalar
	 *            The scalar by which to multiply this vector
	 * @return the resulting vector
	 */
	public Vector2 multiply(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}

	/**
	 * Creates a unit vector with the same direction as this vector
	 *
	 * @return the unit vector
	 */
	public Vector2 normalize() {
		double mag = magnitude();
		return new Vector2(x / mag, y / mag);
	}

	/**
	 * Gets the magnitude/length of this vector
	 *
	 * @return the magnitude
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}
}