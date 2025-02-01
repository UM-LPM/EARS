package org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.ConditionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class GridCellContainsObject extends ConditionNode {

    public static int GRID_SIZE_X = 5;
    public static int GRID_SIZE_Y = 5;
    public static int TARGET_GAME_OBJECT_COUNT = 9;

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

    public GridCellContainsObject() {
        this(BehaviourTreeNodeType.GRID_CELL_CONTAINS_OBJECT, List.of(
                new Property("targetGameObject",0, GridCellContainsObject.TARGET_GAME_OBJECT_COUNT),
                new Property("gridPositionX",0, GRID_SIZE_X),
                new Property("gridPositionY",0, GRID_SIZE_Y)
        ));
    }

    public GridCellContainsObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
