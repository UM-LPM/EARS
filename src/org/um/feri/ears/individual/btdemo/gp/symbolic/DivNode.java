package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.List;
import java.util.Map;

public class DivNode extends OperatorNode {
    public DivNode(List<Node> children) {
        super(children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double result = children.get(0).evaluate(variables);
        for (int i = 1; i < children.size(); i++) {
            double divisor = children.get(i).evaluate(variables);
            if (divisor != 0) {
                result /= divisor;
            } else {
                throw new ArithmeticException("Division by zero");
            }
        }
        return result;
    }
}
