package org.um.feri.ears.individual.btdemo.gp.behaviour.soccer;

import org.um.feri.ears.individual.btdemo.gp.behaviour.ActionNode;
import org.um.feri.ears.individual.btdemo.gp.behaviour.BehaviourTreeNodeType;
import org.um.feri.ears.individual.btdemo.gp.behaviour.Property;

import java.util.List;

public class Rotate extends ActionNode {
    public enum RotateDirection {
        Left,
        Right,
        NoAction,
        Random
    }

    public Rotate() {
        this(BehaviourTreeNodeType.ROTATE, List.of(
                new Property("rotateDirection",0, RotateDirection.values().length)
        ));
    }

    public Rotate(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
