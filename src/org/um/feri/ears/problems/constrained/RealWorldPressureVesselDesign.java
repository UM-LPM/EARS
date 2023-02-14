package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.problems.DoubleProblem;

/**
 * Real-World Problem:
 * Pressure Vessel Design
 *
 * @author Janez Krnc
 * @version 1
 *
 * <p>
 * Goal: Minimize total cost (material, forming, welding) of the cylindrical pressure vessel
 * Variables: Thickness of the shell (x1), thickness of the head (x2), inner radius (x3)
 * </p>
 */
public class RealWorldPressureVesselDesign extends DoubleProblem {

    public RealWorldPressureVesselDesign() {
        super("PressureVesselDesign", 4, 1, 1, 4);
        maxConstraints = new double[numberOfConstraints];
        minConstraints = new double[numberOfConstraints];
        countConstraints = new double[numberOfConstraints];
        sumConstraints = new double[numberOfConstraints];
        normalizationConstraintsFactor = new double[numberOfConstraints];

        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));

        // thickness of the vessel
        lowerLimit.set(0, 0.0);
        upperLimit.set(0, 99.0);
        // thickness of the head
        lowerLimit.set(1, 0.0);
        upperLimit.set(1, 99.0);
        // internal radius
        lowerLimit.set(2, 10.0);
        upperLimit.set(2, 200.0);
        // length of the cylindrical section of the vessel
        lowerLimit.set(3, 10.0);
        upperLimit.set(3, 200.0);
    }

    @Override
    public double eval(double[] x) {
        return (0.6224 * x[0] * x[2] * x[3])
                + (1.7781 * x[1] * Math.pow(x[2], 2))
                + (3.1661 * Math.pow(x[0], 2) * x[3])
                + (19.84 * Math.pow(x[0], 2) * x[2]);
    }

    @Override
    public double[] evaluateConstrains(double[] x) {
        double[] g = new double[numberOfConstraints];
        g[0] = (-1.0 * x[0]) + (0.0193 * x[2]);
        g[1] = (-1.0 * x[1]) + (0.00954 * x[2]);
        g[2] = ((-1.0 * Math.PI) * Math.pow(x[2], 2) * x[3])
                - ((4.0 / 3.0) * Math.PI * Math.pow(x[2], 3))
                + 1296000;
        g[3] = x[3] - 240;
        return g;
    }
}
