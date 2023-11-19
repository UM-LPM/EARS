package org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class MoveSide extends ActionNode {
    public enum MoveSideDirection {
        Left,
        Right,
        Random
    }

    public MoveSide() {
        this(BehaviourTreeNodeType.MOVE_SIDE, List.of(
                new Property("moveSideDirection",1, MoveSideDirection.values().length + 1)
        ));
    }

    public MoveSide(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
