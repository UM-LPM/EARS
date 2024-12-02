package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class TanhNode extends OperatorNode {

    public TanhNode() {
        super("tanh", 1);
    }

    public TanhNode(List<Node> children) {
        super("tanh", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.tanh(children.get(0).evaluate(variables));
    }
}
