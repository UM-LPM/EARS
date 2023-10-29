package org.um.feri.ears.individual.btdemo.gp.behaviour;

import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConditionNode extends BehaviourTreeNode {

    public ConditionNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public ConditionNode(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties, 0);
    }

}