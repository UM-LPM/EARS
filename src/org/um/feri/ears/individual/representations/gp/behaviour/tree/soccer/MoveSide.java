package org.um.feri.ears.individual.representations.gp.behaviour.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.Property;

import java.util.List;

public class MoveSide extends ActionNode {
    public enum MoveSideDirection {
        Left,
        Right,
        NoAction,
        Random
    }

    public MoveSide() {
        this(BehaviourTreeNodeType.MOVE_SIDE, List.of(
                new Property("moveSideDirection",0, MoveSideDirection.values().length)
        ));
    }

    public MoveSide(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
