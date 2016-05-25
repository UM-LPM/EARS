//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.spea2;

public class DistanceNode {

	/**
	 * Indicates the position of a <code>Solution</code> in a
	 * <code>SolutionSet</code>.
	 */
	private int reference_;

	/**
	 * Indicates the distance to the <code>Solution</code> represented by
	 * <code>reference_</code>.
	 */
	private double distance_;

	/**
	 * Constructor.
	 * 
	 * @param distance The distance to a <code>Solution</code>.
	 * @param reference The position of the <code>Solution</code>.
	 */
	public DistanceNode(double distance, int reference) {
		distance_ = distance;
		reference_ = reference;
	}

	/**
	 * Sets the distance to a <code>Solution</code>
	 * 
	 * @param distance
	 *            The distance
	 */
	public void setDistance(double distance) {
		distance_ = distance;
	}

	/**
	 * Sets the reference to a <code>Solution</code>
	 * 
	 * @param reference The reference
	 */
	public void setReferece(int reference) {
		reference_ = reference;
	}

	/**
	 * Gets the distance
	 * 
	 * @return the distance
	 */
	public double getDistance() {
		return distance_;
	}

	/**
	 * Gets the reference
	 * 
	 * @return the reference
	 */
	public int getReference() {
		return reference_;
	}
}
