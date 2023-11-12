package org.um.feri.ears.individual.representations.gp.symbolic;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public abstract class OperatorNode extends Node {

    public OperatorNode(String name) {
        super(2, new ArrayList<>(), true, name);
    }

    public OperatorNode(List<Node> children, String name) {
        super(2, children, true, name);
    }
}

