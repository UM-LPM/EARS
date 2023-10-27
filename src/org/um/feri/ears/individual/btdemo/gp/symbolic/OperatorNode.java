package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.List;

public abstract class OperatorNode extends Node {
    protected final List<Node> children;

    public OperatorNode(List<Node> children) {
        this.children = children;
    }

    public List<Node> getChildren() {
        return children;
    }
}

