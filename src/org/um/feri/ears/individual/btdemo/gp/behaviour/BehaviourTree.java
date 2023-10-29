package org.um.feri.ears.individual.btdemo.gp.behaviour;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;

import java.util.Map;

public class BehaviourTree extends Tree {
    public BehaviourTree(String name) {
        super(name,null);
    }

    public BehaviourTree(String name, Node rootNode) {
        super(name,rootNode);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        // TODO Call API and execute one game and return the score (fitness)
        return -1;
    }

    @Override
    public BehaviourTree clone() {
        BehaviourTree cloned = new BehaviourTree(this.name);
        cloned.rootNode = this.rootNode.clone();
        return cloned;
    }


    public void insertRootNode(){
        Node oldRoot = this.rootNode;
        this.rootNode = new RootNode();
        this.rootNode.insert(0, oldRoot);
    }


}
