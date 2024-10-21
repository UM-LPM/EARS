package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import java.util.List;

public class Repeat extends DecoratorNode {
    public Repeat() {
        this(BehaviourTreeNodeType.REPEAT, List.of(
                new Property("restartOnSuccess",0, 2),
                new Property("restartOnFailure",0, 2)
        ));
    }

    public Repeat(BehaviourTreeNodeType nodeType, List<Property> properties) {
        super(nodeType, properties, 1);
    }
}
