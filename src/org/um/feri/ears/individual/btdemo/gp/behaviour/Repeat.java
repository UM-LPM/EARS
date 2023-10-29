package org.um.feri.ears.individual.btdemo.gp.behaviour;

import org.um.feri.ears.individual.btdemo.gp.behaviour.soccer.RayHitObject;

import java.util.List;

public class Repeat extends DecoratorNode {
    public Repeat() {
        this(BehaviourTreeNodeType.REPEAT, List.of(
                new Property("restartOnSuccess",0, 1),
                new Property("restartOnFailure",0, 1)
        ));
    }

    public Repeat(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties, 1);
    }
}
