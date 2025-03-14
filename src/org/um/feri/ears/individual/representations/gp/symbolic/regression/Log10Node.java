package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class Log10Node extends OperatorNode {

    public Log10Node() {
        super("log10", 1);
    }

    public Log10Node(List<Node> children) {
        super("log10", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double result = children.get(0).evaluate(variables);
        return result > 0 ? Math.log10(result) : 0;
    }
}
