package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class DecoratorNode extends BehaviourTreeNode {

    public DecoratorNode(BehaviourTreeNodeType nodeType, int arity){
        this(nodeType, null, arity);
    }

    public DecoratorNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity){
        this(nodeType, properties , arity, null);
    }

    public DecoratorNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children) {
        super(nodeType, properties, arity, children, true);
    }

    @Override
    public String setNodeStyle(){
        String nodeStyle = "";

        nodeStyle = "shape=diamond";

        return  nodeStyle;
    }
}
