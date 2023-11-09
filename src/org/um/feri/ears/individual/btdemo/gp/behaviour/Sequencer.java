package org.um.feri.ears.individual.btdemo.gp.behaviour;

public class Sequencer extends CompositeNode {

    public static final int MAX_CHILDREN = 5;

    public Sequencer() {
        this(BehaviourTreeNodeType.SEQUENCER);
    }

    public Sequencer(BehaviourTreeNodeType nodeType) {
        super(nodeType, MAX_CHILDREN);
    }
}
