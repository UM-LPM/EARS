package org.um.feri.ears.individual.representations.gp.behaviour;

import org.um.feri.ears.individual.representations.gp.Node;

import static java.util.Objects.requireNonNull;

public class RootNode extends BehaviourTreeNode {

    public RootNode(){
        this(BehaviourTreeNodeType.ROOT);
    }

    public RootNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public RootNode(BehaviourTreeNodeType nodeType, Node child){
        super(nodeType, null, 1);
    }
}
