package org.um.feri.ears.problems.unconstrained.cec2022;

public class F10 extends CEC2022 {

    // Composition Function 2 (N=3): Schwefel + Rastrigin + HGBat
    public F10(int d) {
        super("F10", d, 10);
    }

    @Override
    public double eval(double[] x) {
        return cf02(x, numberOfDimensions, 1) + 2400.0;
    }
}
