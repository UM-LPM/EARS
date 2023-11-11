package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.List;
import java.util.Map;

public class SubNode extends OperatorNode {

    public SubNode() {
        super("-");
    }
    public SubNode(List<Node> children) {
        super(children, "-");
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double result = children.get(0).evaluate(variables);
        for (int i = 1; i < children.size(); i++) {
            result -= children.get(i).evaluate(variables);
        }
        return result;
    }
}
