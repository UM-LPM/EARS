package org.um.feri.ears.individual.representations.gp.behaviour;

public class Selector extends CompositeNode {

    public static final int MAX_CHILDREN = 5;
    public Selector() {
        this(BehaviourTreeNodeType.SELECTOR);
    }

    public Selector(BehaviourTreeNodeType nodeType) {
        super(nodeType, MAX_CHILDREN);
    }
}
