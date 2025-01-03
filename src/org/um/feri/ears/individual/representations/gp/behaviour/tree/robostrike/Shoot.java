package org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class Shoot extends ActionNode {

    public Shoot() {
        this(BehaviourTreeNodeType.SHOOT, List.of(
                new Property("shoot",1, 2, 1)
        ));
    }

    public Shoot(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

