package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;
import java.util.Map;

public class SqrtNode extends OperatorNode {

    public SqrtNode() {
        super("sqrt", 1);
    }

    public SqrtNode(List<Node> children) {
        super("sqrt", 1, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double result = Math.sqrt(children.get(0).evaluate(variables));
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return 0;
        }
        return result;
    }
}
