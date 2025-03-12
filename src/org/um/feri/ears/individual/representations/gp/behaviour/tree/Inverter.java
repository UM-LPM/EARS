package org.um.feri.ears.individual.representations.gp.behaviour.tree;

public class Inverter extends DecoratorNode {
    public Inverter() {
        this(BehaviourTreeNodeType.INVERTER);
    }

    public Inverter(BehaviourTreeNodeType nodeType) {
        super(nodeType, 1);
    }
}
