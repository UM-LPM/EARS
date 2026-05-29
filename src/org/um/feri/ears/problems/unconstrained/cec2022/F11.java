package org.um.feri.ears.problems.unconstrained.cec2022;

public class F11 extends CEC2022 {

    // Composition Function 3 (N=5): EScaffer6 + Schwefel + Griewank + Rosenbrock + Rastrigin
    public F11(int d) {
        super("F11", d, 11);
    }

    @Override
    public double eval(double[] x) {
        return cf06(x, numberOfDimensions, 1) + 2600.0;
    }
}
