package org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.ConditionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class RayHitObject extends ConditionNode {
    private enum AgentSide {
        Center ,
        Left,
        Right
    }

    private enum AgentSideExtended {
        Center ,
        Left,
        Right,
        BackCenter,
        BackLeft,
        BackRight
    }

    private enum TargetGameObject {
        Agent,
        Wall,
        Obstacle,
        Target1,
        Target2,
        Target3,
        Target4,
        Target5,
        Target6,
        Target7,
        Unknown
    }

    public RayHitObject() {
        this(BehaviourTreeNodeType.RAY_HIT_OBJECT, List.of(
                new Property("targetGameObject",0, 3), // TODO replace with TargetGameObject.values().length
                new Property("side",0, AgentSide.values().length)
        ));
    }

    public RayHitObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
