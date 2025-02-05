package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class MulNode extends OperatorNode {

    public MulNode() {
        super("*");
    }
    public MulNode(List<Node> children) {
        super("*", 2, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return children.stream().mapToDouble(child -> child.evaluate(variables)).reduce(1, (a, b) -> a * b);
    }
}
