package org.um.feri.ears.individual.representations.gp.behaviour.tree;

public class Selector extends CompositeNode {

    public static int MAX_CHILDREN = 3;
    public Selector() {
        this(BehaviourTreeNodeType.SELECTOR);
    }

    public Selector(BehaviourTreeNodeType nodeType) {
        super(nodeType, MAX_CHILDREN);
    }
}
