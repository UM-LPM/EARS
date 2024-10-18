package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import java.util.List;

public class ActionNode extends BehaviourTreeNode {

    public ActionNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public ActionNode(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties, 0);
    }

    @Override
    public String setNodeStyle(){
        String nodeStyle = "";

        nodeStyle = "shape=parallelogram";

        return  nodeStyle;
    }
}