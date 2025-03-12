package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class DivNode extends OperatorNode {

    public DivNode() {
        super("/");
    }
    public DivNode(List<Node> children) {
        super("/", 2, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        Double result = children.get(0).evaluate(variables) / children.get(1).evaluate(variables);
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return 1;
        }
        return result;
    }
}
