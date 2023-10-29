package org.um.feri.ears.individual.btdemo.gp.behaviour.soccer;

import org.um.feri.ears.individual.btdemo.gp.behaviour.BehaviourTreeNodeType;
import org.um.feri.ears.individual.btdemo.gp.behaviour.ConditionNode;
import org.um.feri.ears.individual.btdemo.gp.behaviour.DecoratorNode;
import org.um.feri.ears.individual.btdemo.gp.behaviour.Property;

import java.util.List;

public class RayHitObject extends ConditionNode {
    public enum AgentSide {
        Center ,
        Left,
        Right,
        NotDefined
    }

    public enum SoccerGameObject {
        Empty,
        SoccerBall,
        BlueStriker,
        PurpleStriker,
        GoalNetBlue,
        GoalNetPurple,
        Side,
        WallAngle
    }

    public RayHitObject() {
        this(BehaviourTreeNodeType.MOVE_SIDE, List.of(
                new Property("targetGameObject",0, SoccerGameObject.values().length),
                new Property("side",0, AgentSide.values().length)
        ));
    }

    public RayHitObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
