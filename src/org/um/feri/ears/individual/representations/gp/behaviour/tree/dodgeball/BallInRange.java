package org.um.feri.ears.individual.representations.gp.behaviour.tree.dodgeball;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class BallInRange extends ActionNode {
    public BallInRange() {
        this(BehaviourTreeNodeType.BALL_IN_RANGE, List.of());
    }

    public BallInRange(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

