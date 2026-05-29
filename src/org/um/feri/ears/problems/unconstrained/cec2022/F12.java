package org.um.feri.ears.problems.unconstrained.cec2022;

public class F12 extends CEC2022 {

    // Composition Function 4 (N=6): HGBat + Rastrigin + Schwefel + BentCigar + Elliptic + EScaffer6
    public F12(int d) {
        super("F12", d, 12);
    }

    @Override
    public double eval(double[] x) {
        return cf07(x, numberOfDimensions, 1) + 2700.0;
    }
}
