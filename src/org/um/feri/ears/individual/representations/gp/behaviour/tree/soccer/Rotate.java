package org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class Rotate extends ActionNode {
    public enum RotateDirection {
        Left,
        Right,
        Random
    }

    public Rotate() {
        this(BehaviourTreeNodeType.ROTATE, List.of(
                new Property("rotateDirection",1, RotateDirection.values().length + 1)
        ));
    }

    public Rotate(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
