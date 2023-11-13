package org.um.feri.ears.individual.representations.gp.behaviour.tree.soccer;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.ConditionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class RayHitObject extends ConditionNode {
    public enum AgentSide {
        Center ,
        Left,
        Right,
        BackCenter,
        BackLeft,
        BackRight
    }

    public enum SoccerGameObject {
        SoccerBall,
        BlueStriker,
        PurpleStriker,
        GoalNetBlue,
        GoalNetPurple,
        Side,
        WallAngle
    }

    public RayHitObject() {
        this(BehaviourTreeNodeType.RAY_HIT_OBJECT, List.of(
                new Property("targetGameObject",0, SoccerGameObject.values().length),
                new Property("side",0, AgentSide.values().length)
        ));
    }

    public RayHitObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
