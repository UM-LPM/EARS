package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import java.util.List;

public class ConditionNode extends BehaviourTreeNode {

    public ConditionNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public ConditionNode(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties, 0);
    }

}