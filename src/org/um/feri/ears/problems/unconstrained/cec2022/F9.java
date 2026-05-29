package org.um.feri.ears.problems.unconstrained.cec2022;

public class F9 extends CEC2022 {

    // Composition Function 1 (N=5): Rosenbrock + Elliptic + BentCigar + Discus + Elliptic
    public F9(int d) {
        super("F09", d, 9);
    }

    @Override
    public double eval(double[] x) {
        return cf01(x, numberOfDimensions, 1) + 2300.0;
    }
}
