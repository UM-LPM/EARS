package org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors;

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
        Object1,
        Object2,
        Object3,
        Object4,
        Object5,
        Object6,
        Object7
    }

    public RayHitObject() {
        this(BehaviourTreeNodeType.RAY_HIT_OBJECT, List.of(
                new Property("targetGameObject",0, 7), // TODO replace with TargetGameObject.values().length // TODO change 1 back to 0
                new Property("side",0, AgentSide.values().length),
                new Property("rayIndex",0, 17) // Number of rays (check in Unity)
        ));
    }

    public RayHitObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
