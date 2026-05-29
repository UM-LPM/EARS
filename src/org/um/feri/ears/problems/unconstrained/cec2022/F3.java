package org.um.feri.ears.problems.unconstrained.cec2022;

public class F3 extends CEC2022 {

    // Shifted and Rotated Schaffer's F7 Function
    public F3(int d) {
        super("F03", d, 3);
    }

    @Override
    public double eval(double[] x) {
        return schafferF7Func(x, numberOfDimensions, 1, 1, 0) + 600.0;
    }
}
