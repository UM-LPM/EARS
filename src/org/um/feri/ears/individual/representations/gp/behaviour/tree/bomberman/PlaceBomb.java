package org.um.feri.ears.individual.representations.gp.behaviour.tree.bomberman;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class PlaceBomb extends ActionNode {

    public PlaceBomb() {
        this(BehaviourTreeNodeType.PLACE_BOMB, List.of(
                new Property("placeBomb",1, 2, 1)
        ));
    }

    public PlaceBomb(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

