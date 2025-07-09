package org.um.feri.ears.individual.representations.gp.behaviour.tree.robostrike;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class HealthLevelBellow extends ActionNode {
    private enum HealthLevel
    {
        Low,
        Medium,
        High
    }

    public HealthLevelBellow() {
        this(BehaviourTreeNodeType.HEALTH_LEVEL_BELLOW, List.of(
                new Property("healthLevel",0, HealthLevel.values().length)
        ));
    }

    public HealthLevelBellow(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}

