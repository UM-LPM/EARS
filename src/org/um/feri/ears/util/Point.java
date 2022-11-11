package org.um.feri.ears.util;

import java.util.Arrays;

import org.um.feri.ears.problems.moo.MOSolutionBase;

public class Point<Type extends Number> {

    private double[] point;

    /**
     * Constructor
     *
     * @param dimensions Dimensions of the point
     */
    public Point(int dimensions) {
        point = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            point[i] = 0.0;
        }
    }

    /**
     * Copy constructor
     *
     * @param point
     */
    public Point(Point point) {

        this.point = new double[point.getNumberOfDimensions()];

        for (int i = 0; i < point.getNumberOfDimensions(); i++) {
            this.point[i] = point.getDimensionValue(i);
        }
    }

    /**
     * Constructor from a solution
     *
     * @param solution
     */
    public Point(MOSolutionBase<Type> solution) {

        int dimensions = solution.numberOfObjectives();
        point = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            point[i] = solution.getObjective(i);
        }
    }

    /**
     * Constructor from an array of double values
     *
     * @param point
     */
    public Point(double[] point) {
        this.point = new double[point.length];
        System.arraycopy(point, 0, this.point, 0, point.length);
    }

    public int getNumberOfDimensions() {
        return point.length;
    }

    public double[] getValues() {
        return point;
    }


    public double getDimensionValue(int index) {
        return point[index];
    }

    public void setDimensionValue(int index, double value) {
        point[index] = value;
    }

    public boolean equals(Point p) {
        if (this == p)
            return true;
		return Arrays.equals(point, p.point);
	}
}
