package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.*;

public class ConstNode extends Node {

    public static final double MIN_VALUE = -10;
    public static final double MAX_VALUE = 10;

    private final double value;

    public ConstNode() {
        this.value = RNG.nextDouble() * (MAX_VALUE - MIN_VALUE) + MIN_VALUE;
        this.name = String.format("%.4f", value);
    }

    public ConstNode(double value) {
        this.value = value;
        this.name = String.format("%.4f", value);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

}
