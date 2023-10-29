package org.um.feri.ears.individual.btdemo.gp.symbolic;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.representations.gp.TreeNode;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public abstract class OperatorNode extends Node {

    public OperatorNode() {
        super(2, new ArrayList<>(), true);
    }

    public OperatorNode(List<Node> children) {
        super(2, children, true);
    }
}

