package org.um.feri.ears.problems.unconstrained.cec2017;

public class F1 extends CEC2017 {

    // Shifted and Rotated Bent Cigar Function
    public F1(int d) {
        super("F01", d, 1);
    }

    @Override
    public double eval(double[] x) {
        return bentCigarFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
