package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.*;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class VarNode extends Node {
    public static List<String> variables = Arrays.asList("x", "y", "z");

    public VarNode() {
        super(0, variables.get(RNG.nextInt(variables.size())));
    }

    public VarNode(String name) {
        super(0, name);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("No value for variable: " + name);
        }
        return variables.get(name);
    }

}
