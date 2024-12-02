package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class LogNode extends OperatorNode {

    public LogNode() {
        super("log", 1);
    }

    public LogNode(List<Node> children) {
        super("log", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double result = children.get(0).evaluate(variables);
        return result > 0 ? Math.log(result) : 0;
    }
}
