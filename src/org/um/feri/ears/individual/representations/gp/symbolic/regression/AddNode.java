package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class AddNode extends OperatorNode {

    public AddNode() {
        super("+");
    }

    public AddNode(List<Node> children) {
        super("+", 2, children);
    }

    @Override
    public double evaluate(Map<String, Double> variableValues) {
        return children.stream().mapToDouble(child -> child.evaluate(variableValues)).sum();
    }
}
