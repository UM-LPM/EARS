package org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.movement.Rotate;

import java.util.List;

public class RotateTurret extends ActionNode {
    private enum RotateDirection {
        Left,
        Right//,
        //Random
    }

    public RotateTurret() {
        this(BehaviourTreeNodeType.ROTATE_TURRET, List.of(
                new Property("rotateDirection",1, Rotate.RotateDirection.values().length + 1)
        ));
    }

    public RotateTurret(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

