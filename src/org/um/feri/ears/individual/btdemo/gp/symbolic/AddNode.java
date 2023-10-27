package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.List;
import java.util.Map;

public class AddNode extends OperatorNode {
    public AddNode(List<Node> children) {
        super(children);
    }

    @Override
    public double evaluate(Map<String, Double> variableValues) {
        return children.stream().mapToDouble(child -> child.evaluate(variableValues)).sum();
    }
}
