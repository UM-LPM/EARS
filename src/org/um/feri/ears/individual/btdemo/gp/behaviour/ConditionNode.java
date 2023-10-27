package org.um.feri.ears.individual.btdemo.gp.behaviour;

import java.util.List;
import java.util.Map;

public class ConditionNode extends BehaviourTreeNode {

    public ConditionNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public ConditionNode(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }
}