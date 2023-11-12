package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class CompositeNode extends BehaviourTreeNode {

    public CompositeNode(BehaviourTreeNodeType nodeType, int arity){
        this(nodeType, null, arity);
    }

    public CompositeNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity){
        this(nodeType, properties , arity, null);
    }

    public CompositeNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children) {
        super(nodeType, properties, arity, children, false);
    }
}
