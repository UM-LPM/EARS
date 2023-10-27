package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.Map;

public class VarNode extends Node {
    private final String name;

    public VarNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("No value for variable: " + name);
        }
        return variables.get(name);
    }
}
