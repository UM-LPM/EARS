package org.um.feri.ears.algorithms.so.cro;

import org.um.feri.ears.problems.DoubleSolution;

/**
 * Represents a Coordinate in Coral Reef Grid
 * 
 * @author inacio-medeiros
 *
 */
public class Coordinate implements Comparable<Coordinate> {
	private int x, y;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            Coordinate's x-position
	 * @param y
	 *            Coordinate's y-position
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Retrieves Coordinate's x-position
	 * 
	 * @return Coordinate's x-position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieves Coordinate's y-position
	 * 
	 * @return Coordinate's y-position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets Coordinate's x-position to a new value
	 * 
	 * @param x
	 *            new value for Coordinate's x-position
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets Coordinate's y-position to a new value
	 * 
	 * @param x
	 *            new value for Coordinate's y-position
	 */
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int compareTo(Coordinate arg0) {
		int diffX = Math.abs(arg0.x - this.x);
		int diffY = Math.abs(arg0.y - this.y);
		double result = Math.sqrt((diffX * diffX) + (diffY * diffY));

		return Integer.parseInt(Double.toString(result));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;

		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
