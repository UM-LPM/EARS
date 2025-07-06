package org.um.feri.ears.individual.representations.gp.behaviour.tree.dodgeball;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class BallInHand extends ActionNode {

    public BallInHand() {
        this(BehaviourTreeNodeType.BALL_IN_HAND, List.of());
    }

    public BallInHand(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

