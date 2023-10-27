package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RootNode extends BehaviourTreeNode {

    protected Node child;

    public RootNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public RootNode(BehaviourTreeNodeType nodeType, Node child){
        super(nodeType, null);
        this.child = child;
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }

    @JsonIgnore
    public Node getChild() {
        return child;
    }
    @JsonIgnore
    public void setChild(Node child) {
        this.child = child;
    }

    @JsonProperty("Children")
    public List<Node> getChildArray() {
        return Collections.singletonList(child);
    }
}
