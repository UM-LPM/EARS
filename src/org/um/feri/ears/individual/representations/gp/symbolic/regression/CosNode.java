package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class CosNode extends OperatorNode {

    public CosNode() {
        super("cos", 1);
    }

    public CosNode(List<Node> children) {
        super("cos", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.cos(children.get(0).evaluate(variables));
    }
}
