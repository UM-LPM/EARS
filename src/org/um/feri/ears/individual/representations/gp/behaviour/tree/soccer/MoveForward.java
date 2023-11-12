package org.um.feri.ears.individual.representations.gp.behaviour.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.Property;

import java.util.List;


public class MoveForward extends ActionNode {
    public enum MoveForwardDirection {
        Forward,
        Backward,
        NoAction,
        Random
    }

    public MoveForward() {
        this(BehaviourTreeNodeType.MOVE_FORWARD, List.of(
                new Property("moveForwardDirection",0, MoveForwardDirection.values().length)
        ));
    }

    public MoveForward(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
