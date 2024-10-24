package org.um.feri.ears.individual.representations.gp.behaviour.tree;

public enum BehaviourTreeNodeType {
    ROOT("RootNode"),
    REPEAT("Repeat"),
    SELECTOR("Selector"),
    SEQUENCER("Sequencer"),
    INVERTER("Inverter"),
    ENCAPSULATED_NODE("Encapsulator"),
    MOVE_FORWARD("MoveForward"),
    MOVE_SIDE("MoveSide"),
    ROTATE("Rotate"),
    RAY_HIT_OBJECT("RayHitObject"),
    GRID_CELL_CONTAINS_OBJECT("GridCellContainsObject"),
    SHOOT("Shoot"),
    PLACE_BOMB("PlaceBomb"),
    ROTATE_TURRET("RotateTurret"),
    HEALTH_LEVEL_BELLOW("HealthLevelBellow"),
    SHIELD_LEVEL_BELLOW("ShieldLevelBellow"),
    AMMO_LEVEL_BELLOW("AmmoLevelBellow");

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
