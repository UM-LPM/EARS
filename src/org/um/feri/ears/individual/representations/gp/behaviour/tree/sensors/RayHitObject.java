package org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.ConditionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class RayHitObject extends ConditionNode {

    public static final long serialVersionUID = -2309355124233620126L;

    public static int TARGET_GAME_OBJECT_COUNT = 7;
    public static int RAY_INDEX_COUNT = 17;

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
        Object7,
        Object8,
        Object9,
        Object10,
        Object11,
        Object12,
        Object13,
        Object14,
        Object15,
        Object16,
        Object17,
        Object18,
        Object19,
        Object20,
        Object21,
        Object22,
        Object23,
        Object24,
        Object25,
        Object26,
        Object27,
        Object28,
        Object29,
        Object30,
        Object31
    }

    private enum ObjectTeamType
    {
        Default, // Detect any object
        Teammate, // Detect your teams object
        Opponent // Detect opponent teams object
    }

    public RayHitObject() {
        this(BehaviourTreeNodeType.RAY_HIT_OBJECT, List.of(
                new Property("targetGameObject",0, TARGET_GAME_OBJECT_COUNT),
                new Property("side",0, AgentSide.values().length),
                new Property("rayIndex",0, RAY_INDEX_COUNT), // Number of rays (check in Unity)
                new Property("targetTeamType",0, ObjectTeamType.values().length) // Team identifier
        ));
    }

    public RayHitObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
