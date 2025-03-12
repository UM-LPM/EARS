package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class TanNode extends OperatorNode {
    public TanNode() {
        super("tan", 1);
    }

    public TanNode(List<Node> children) {
        super("tan", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.tan(children.get(0).evaluate(variables));
    }
}
