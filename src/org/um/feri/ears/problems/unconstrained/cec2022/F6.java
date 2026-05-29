package org.um.feri.ears.problems.unconstrained.cec2022;

public class F6 extends CEC2022 {

    // Hybrid Function 1 (N=3): BentCigar + HGBat + Rastrigin
    public F6(int d) {
        super("F06", d, 6);
    }

    @Override
    public double eval(double[] x) {
        return hf02(x, numberOfDimensions, 1, 1) + 1800.0;
    }
}
