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
    private int reference;

    /**
     * Indicates the distance to the <code>Solution</code> represented by
     * <code>reference_</code>.
     */
    private double distance;

    /**
     * Constructor.
     *
     * @param distance  The distance to a <code>Solution</code>.
     * @param reference The position of the <code>Solution</code>.
     */
    public DistanceNode(double distance, int reference) {
        this.distance = distance;
        this.reference = reference;
    }

    /**
     * Sets the distance to a <code>Solution</code>
     *
     * @param distance The distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Sets the reference to a <code>Solution</code>
     *
     * @param reference The reference
     */
    public void setReference(int reference) {
        this.reference = reference;
    }

    /**
     * Gets the distance
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Gets the reference
     *
     * @return the reference
     */
    public int getReference() {
        return reference;
    }
}
