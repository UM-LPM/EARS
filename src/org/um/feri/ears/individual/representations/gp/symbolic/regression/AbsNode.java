package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class AbsNode extends OperatorNode {

    public AbsNode() {
        super("abs", 1);
    }

    public AbsNode(List<Node> children) {
        super("abs", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.abs(children.get(0).evaluate(variables));
    }
}
