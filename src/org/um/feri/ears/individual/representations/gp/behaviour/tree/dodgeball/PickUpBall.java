package org.um.feri.ears.individual.representations.gp.behaviour.tree.dodgeball;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class PickUpBall extends ActionNode {

    public PickUpBall() {
        this(BehaviourTreeNodeType.PICKUP_BALL, List.of(
                new Property("pickupBall",1, 2, 1)
        ));
    }

    public PickUpBall(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

