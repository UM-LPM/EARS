package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.um.feri.ears.individual.btdemo.gp.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeNode extends BehaviourTreeNode {
    private List<Node> children;

    public CompositeNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public CompositeNode(BehaviourTreeNodeType nodeType, List<Node> children){
        this(nodeType, children, null);
    }

    public CompositeNode(BehaviourTreeNodeType nodeType, List<Node> children, List<Property> properties) {
        super(nodeType, properties);
        this.children = children;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }

    @JsonProperty("Children")
    public void setChildren(List<Node> children) {
        this.children = new ArrayList<>(children);
    }
    @JsonProperty("Children")
    public List<Node> getChildren() {
        return children;
    }
}
