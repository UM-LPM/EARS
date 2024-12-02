package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class PowNode extends OperatorNode {

    public PowNode() {
        super("^");
    }
    public PowNode(List<Node> children) {
        super("^", 2, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double base = children.get(0).evaluate(variables);
        double exponent = children.get(1).evaluate(variables);

        // Handle zero base with negative exponent
        if (base == 0 && exponent < 0) {
            return 1;
        }

        // Handle negative base with fractional exponent
        if (base < 0 && exponent != Math.floor(exponent)) {
            return 1;
        }

        return Math.pow(base, exponent);
    }
}