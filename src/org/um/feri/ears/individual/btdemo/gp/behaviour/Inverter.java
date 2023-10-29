package org.um.feri.ears.individual.btdemo.gp.behaviour;

public class Inverter extends DecoratorNode {
    public Inverter() {
        this(BehaviourTreeNodeType.INVERTER);
    }

    public Inverter(BehaviourTreeNodeType nodeType) {
        super(nodeType, 1);
    }
}
