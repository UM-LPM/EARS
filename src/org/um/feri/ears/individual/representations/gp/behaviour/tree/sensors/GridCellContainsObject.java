package org.um.feri.ears.individual.representations.gp.behaviour.tree.sensors;

import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.ConditionNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;

import java.util.List;

public class GridCellContainsObject extends ConditionNode {

    public static int GRID_SIZE_X = 5;
    public static int GRID_SIZE_Y = 5;

    private enum TargetGameObject2D {
        Agent,
        Indestructible,
        Destructible,
        Bomb,
        Explosion,
        PowerUp,
        Empty
    }

    public GridCellContainsObject() {
        this(BehaviourTreeNodeType.GRID_CELL_CONTAINS_OBJECT, List.of(
                new Property("targetGameObject",0, GridCellContainsObject.TargetGameObject2D.values().length),
                new Property("gridPositionX",0, GRID_SIZE_X),
                new Property("gridPositionY",0, GRID_SIZE_Y)
        ));
    }

    public GridCellContainsObject(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties);
    }
}
