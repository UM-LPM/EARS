package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;

import java.util.Map;

public class SymbolicRegressionTree extends Tree {
    public SymbolicRegressionTree(String name) {
        super(name,null);
    }

    public SymbolicRegressionTree(String name, Node rootNode) {
        super(name,rootNode);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return rootNode.evaluate(variables);
    }

    @Override
    public SymbolicRegressionTree clone() {
        SymbolicRegressionTree cloned = new SymbolicRegressionTree(this.name);
        cloned.rootNode = this.rootNode.clone();
        return cloned;
    }


}
