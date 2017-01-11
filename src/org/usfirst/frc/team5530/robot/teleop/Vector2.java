package org.usfirst.frc.team5530.robot.teleop;

public class Vector2 {
	public final double x, y;

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
