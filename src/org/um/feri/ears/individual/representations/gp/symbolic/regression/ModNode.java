package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class ModNode extends OperatorNode {

    public ModNode() {
        super("%");
    }
    public ModNode(List<Node> children) {
        super("%", 2, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double numerator = children.get(0).evaluate(variables);
        double denominator = children.get(1).evaluate(variables);

        if (denominator == 0) {
            return 0; // Default value when modulo operation is invalid
        }
        return numerator % denominator;
    }
}
