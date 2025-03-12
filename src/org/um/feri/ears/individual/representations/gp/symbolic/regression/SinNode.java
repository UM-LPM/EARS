package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class SinNode extends OperatorNode {

    public SinNode() {
        super("sin", 1);
    }

    public SinNode(List<Node> children) {
        super("sin", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.sin(children.get(0).evaluate(variables));
    }
}
