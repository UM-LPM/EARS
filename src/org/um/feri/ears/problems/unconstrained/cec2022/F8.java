package org.um.feri.ears.problems.unconstrained.cec2022;

public class F8 extends CEC2022 {

    // Hybrid Function 3 (N=5): Katsuura + HappyCat + GrieRosen + Schwefel + Ackley
    public F8(int d) {
        super("F08", d, 8);
    }

    @Override
    public double eval(double[] x) {
        return hf06(x, numberOfDimensions, 1, 1) + 2200.0;
    }
}
