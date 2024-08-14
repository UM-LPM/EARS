package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.util.GraphPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EncapsulatedNode extends BehaviourTreeNode {

    private String encapsulatedNodName;

    public EncapsulatedNode(){
        this(BehaviourTreeNodeType.ENCAPSULATED_NODE, "None");
    }

    public EncapsulatedNode(String encapsulatedNodName){
        this(BehaviourTreeNodeType.ENCAPSULATED_NODE, encapsulatedNodName);
    }
    public EncapsulatedNode(BehaviourTreeNodeType nodeType, String encapsulatedNodName){
        this(nodeType, null, 0, encapsulatedNodName);
    }

    public EncapsulatedNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, String encapsulatedNodName){
        this(nodeType, properties , arity, new ArrayList<>(), encapsulatedNodName);
    }

    public EncapsulatedNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children, String encapsulatedNodName) {
        super(nodeType, properties, arity, children, true);
        this.encapsulatedNodName = encapsulatedNodName;
    }


    @Override
    public String setNodeStyle(){
        String nodeStyle = "";

        nodeStyle = "shape=doubleoctagon";

        return  nodeStyle;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }

    @Override
    public void setTreeNodes(GraphPrinter gp) {
        gp.addln(id + "[label=<"+ this.name +"<BR /><FONT POINT-SIZE=\"10\">" + this.encapsulatedNodName + "</FONT>>, "+ this.setNodeStyle() + "]");

        // TODO Remove this after testing
        for (Node next : children) {
            next.setTreeNodes(gp);
        }
    }

    public void setEncapsulatedNode(String encapsulatedNodeName, Node encapsulatedNodeBehaviour){
        this.encapsulatedNodName = encapsulatedNodeName;
        this.children.clear();
        this.children.add(encapsulatedNodeBehaviour);
    }
}
