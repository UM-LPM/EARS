package org.um.feri.ears.individual.representations.gp.behaviour.tree;

public class Sequencer extends CompositeNode {

    public static int MAX_CHILDREN = 3;

    public Sequencer() {
        this(BehaviourTreeNodeType.SEQUENCER);
    }

    public Sequencer(BehaviourTreeNodeType nodeType) {
        super(nodeType, MAX_CHILDREN);
    }
}
