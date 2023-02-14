package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.problems.DoubleProblem;

/**
 * Real-World Problem:
 * Tension/Compression Spring Design
 *
 * @author Janez Krnc
 * @version 1
 *
 * <p>
 * Goal: Minimize spring weight.
 * Constraints: Shear stress, surge frequency, deflection
 * Variables: Wire diameter (x1), mean coil diameter (x2), number of active coils (x3)
 * </p>
 */
public class RealWorldCompressionSpringDesign extends DoubleProblem {
    public RealWorldCompressionSpringDesign() {
        super(3, 1, 1, 4);
        name = "CompressionSpringDesign";
        maxConstraints = new double[numberOfConstraints];
        minConstraints = new double[numberOfConstraints];
        countConstraints = new double[numberOfConstraints];
        sumConstraints = new double[numberOfConstraints];
        normalizationConstraintsFactor = new double[numberOfConstraints];
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        // diameter of the wire
        lowerLimit.set(0, 0.05);
        upperLimit.set(0, 2.00);
        // diameter of the spring
        lowerLimit.set(1, 0.25);
        upperLimit.set(1, 1.30);
        // number of active coils
        lowerLimit.set(2, 2.00);
        upperLimit.set(2, 15.0);
    }

    @Override
    public double eval(double[] x) {
		return (x[2] + 2) * x[1] * Math.pow(x[0], 2);
    }

    @Override
    public double[] evaluateConstrains(double[] x) {
        double[] g = new double[numberOfConstraints];
        g[0] = 1 - ((Math.pow(x[1], 3) * x[2]) / (71785.0 * Math.pow(x[0], 4)));
        // TODO: Look into it. Velika razhajanja iz constrainta v clanku in definiciji constrainta v http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page5161.htm
        g[1] = (4 * Math.pow(x[1], 2) - x[0] * x[1])
                / (12566.0 * (x[1] * Math.pow(x[0], 3) - Math.pow(x[0], 4)))
                + (1.0 / (5108.0 * Math.pow(x[0], 2)));
        g[2] = 1 - (140.45 * x[0] / (Math.pow(x[1], 2) * x[2]));
        g[3] = (x[0] + x[1]) / 1.5 - 1;
        return g;
    }
}
