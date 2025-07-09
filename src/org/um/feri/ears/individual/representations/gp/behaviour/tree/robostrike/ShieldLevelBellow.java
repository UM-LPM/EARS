package org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class ShieldLevelBellow extends ActionNode {
    private enum ShieldLevel
    {
        Low,
        Medium,
        High
    }

    public ShieldLevelBellow() {
        this(BehaviourTreeNodeType.SHIELD_LEVEL_BELLOW, List.of(
                new Property("shieldLevel",0, ShieldLevel.values().length)
        ));
    }

    public ShieldLevelBellow(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

