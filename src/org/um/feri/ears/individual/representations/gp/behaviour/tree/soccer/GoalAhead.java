package org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.ActionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.ConditionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class GoalAhead extends ConditionNode {
    private enum GoalType
    {
        Team,
        Oponent
    }

    public GoalAhead() {
        this(BehaviourTreeNodeType.GOAL_AHEAD, List.of(
                new Property("goalType",0, GoalAhead.GoalType.values().length)
        ));
    }

    public GoalAhead(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}