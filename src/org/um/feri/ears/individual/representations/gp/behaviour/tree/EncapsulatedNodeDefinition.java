package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;

public class EncapsulatedNodeDefinition {
    private String encapsulatedNodeName;
    private Node encapsulatedNodeBehaviour;

    public EncapsulatedNodeDefinition(String encapsulatedNodeName, Node encapsulatedNodeBehaviour) {
        this.encapsulatedNodeName = encapsulatedNodeName;
        this.encapsulatedNodeBehaviour = encapsulatedNodeBehaviour;
    }

    public String getEncapsulatedNodeName() {
        return encapsulatedNodeName;
    }

    public void setEncapsulatedNodeName(String encapsulatedNodeName) {
        this.encapsulatedNodeName = encapsulatedNodeName;
    }

    public Node getGoalNodeBehaviour() {
        return encapsulatedNodeBehaviour;
    }

    public void setGoalNodeBehaviour(Node encapsulatedNodeBehaviour) {
        this.encapsulatedNodeBehaviour = encapsulatedNodeBehaviour;
    }

}
