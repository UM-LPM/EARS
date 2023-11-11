package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.util.Util;

import java.util.*;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class VarNode extends Node {
    public static List<String> variables = Arrays.asList("x", "y", "z");

    public VarNode() {
        super(0, variables.get(Util.rnd.nextInt(variables.size())));
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
