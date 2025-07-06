package org.um.feri.ears.individual.representations.gp.behaviour.tree.dodgeball;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class ThrowBall extends ActionNode {

    public ThrowBall() {
        this(BehaviourTreeNodeType.THROW_BALL, List.of(
                new Property("throwBall",1, 2, 1)
        ));
    }

    public ThrowBall(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

