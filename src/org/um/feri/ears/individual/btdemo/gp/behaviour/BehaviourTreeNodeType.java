package org.um.feri.ears.individual.btdemo.gp.behaviour;

public enum BehaviourTreeNodeType {
    ROOT("RootNode"),
    REPEAT("Repeat"),
    SELECTOR("Selector"),
    SEQUENCER("Sequencer"),
    INVERTER("Inverter"),
    MOVE_FORWARD("MoveForward"),
    MOVE_SIDE("MoveSide"),
    ROTATE("Rotate"),
    RAY_HIT_OBJECT("RayHitObject");

    private final String value;

    BehaviourTreeNodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String behaviourTreeNodeTypeToString(BehaviourTreeNodeType behaviourTreeNodeType){
        if (behaviourTreeNodeType == null) {
            return null;
        }
        return behaviourTreeNodeType.name();
    }

    public static BehaviourTreeNodeType behaviourTreeNodeTypeFromString(String behaviourTreeNodeTypeString) {
            if (behaviourTreeNodeTypeString == null) {
            return null;
        }
        return BehaviourTreeNodeType.valueOf(behaviourTreeNodeTypeString);
    }
}
