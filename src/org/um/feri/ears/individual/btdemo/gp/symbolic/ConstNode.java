package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConstNode extends Node {
    private final double value;

    public ConstNode(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }
}
