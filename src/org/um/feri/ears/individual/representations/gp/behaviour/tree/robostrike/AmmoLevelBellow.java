package org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class AmmoLevelBellow extends ActionNode {
    private enum AmmoLevel
    {
        Low,
        Medium,
        High
    }

    public AmmoLevelBellow() {
        this(BehaviourTreeNodeType.AMMO_LEVEL_BELLOW, List.of(
                new Property("ammoLevel",1, AmmoLevel.values().length + 1)
        ));
    }

    public AmmoLevelBellow(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

