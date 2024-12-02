package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public abstract class OperatorNode extends Node {

    public OperatorNode(String name) {
        this(name,2);
    }

    public OperatorNode(String name, int arity) {
        this(name, arity, new ArrayList<>());
    }

    public OperatorNode(String name, int arity, List<Node> children) {
        super(name, arity, children, true);
    }
}

