package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.util.GraphPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Encapsulator extends DecoratorNode {

    private String encapsulatedNodeName;

    public Encapsulator(){
        this(BehaviourTreeNodeType.ENCAPSULATED_NODE, "None");
    }

    public Encapsulator(String encapsulatedNodName){
        this(BehaviourTreeNodeType.ENCAPSULATED_NODE, encapsulatedNodName);
    }
    public Encapsulator(BehaviourTreeNodeType nodeType, String encapsulatedNodeName){
        this(nodeType, null, 0, encapsulatedNodeName);
    }

    public Encapsulator(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, String encapsulatedNodeName){
        this(nodeType, properties , arity, new ArrayList<>(), encapsulatedNodeName);
    }

    public Encapsulator(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children, String encapsulatedNodeName) {
        super(nodeType, properties, arity, children);
        this.encapsulatedNodeName = encapsulatedNodeName;
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
        gp.addln(id + "[label=<"+ this.name +"<BR /><FONT POINT-SIZE=\"10\">" + this.encapsulatedNodeName + "</FONT>>, "+ this.setNodeStyle() + "]");

        for (Node next : children) {
            next.setTreeNodes(gp);
        }
    }

    public void setEncapsulatedNode(String encapsulatedNodeName, Node encapsulatedNodeBehaviour){
        this.encapsulatedNodeName = encapsulatedNodeName;
        this.children.clear();
        this.children.add(encapsulatedNodeBehaviour);
    }
}
