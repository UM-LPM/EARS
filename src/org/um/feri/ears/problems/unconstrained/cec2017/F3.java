package org.um.feri.ears.problems.unconstrained.cec2017;

public class F3 extends CEC2017 {

    // Shifted and Rotated Rosenbrock’s Function
    public F3(int d) {
        super("F03", d, 3);
    }

    @Override
    public double eval(double[] x) {
        return rosenbrockFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
