package org.um.feri.ears.problems.unconstrained.cec2022;

public class F7 extends CEC2022 {

    // Hybrid Function 2 (N=6): HGBat + Katsuura + Ackley + Rastrigin + Schwefel + SchafferF7
    public F7(int d) {
        super("F07", d, 7);
    }

    @Override
    public double eval(double[] x) {
        return hf10(x, numberOfDimensions, 1, 1) + 2000.0;
    }
}
