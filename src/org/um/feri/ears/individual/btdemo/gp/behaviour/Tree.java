package org.um.feri.ears.individual.btdemo.gp.behaviour;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;

import java.io.Serializable;
import java.util.Map;

public abstract class Tree implements Serializable {
    public enum TreeType{
        SYMBOLIC,
        BEHAVIOUR
    }

    protected String name;
    protected Node rootNode;

    public Tree(String name) {
        this(name,null);
    }

    public Tree(String name, Node rootNode) {
        this.rootNode = rootNode;
        this.name = name;
    }

    @JsonProperty("RootNode")
    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
    @JsonProperty("RootNode")
    public Node getRootNode() {
        return rootNode;
    }
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int treeHeight() {
        return rootNode.treeHeight();
    }

    public int treeSize() {
        return rootNode.treeSize();
    }

    public abstract double evaluate(Map<String, Double> variables);
    public abstract Tree clone();

    public void printTree(){
        printTree(rootNode, "");
    }

    public void printTree(Node node, String indent){
        if (node instanceof ConstNode) {
            System.out.println(indent + "ConstNode(" + ((ConstNode) node).evaluate(null) + ")");
        } else if (node instanceof VarNode) {
            System.out.println(indent + "VarNode(" + ((VarNode) node).getName() + ")");
        } else if (node instanceof OperatorNode) {
            if (node instanceof AddNode) {
                System.out.println(indent + "AddNode");
            } else if (node instanceof SubNode) {
                System.out.println(indent + "SubNode");
            } else if (node instanceof MulNode) {
                System.out.println(indent + "MulNode");
            } else if (node instanceof DivNode) {
                System.out.println(indent + "DivNode");
            }
            for (Node child : ((OperatorNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        } else if (node instanceof RootNode) {
            System.out.println(indent + "RootNode");
            for (Node child : ((RootNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        } else if (node instanceof ActionNode) {
            System.out.println(indent + "ActionNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
        } else if (node instanceof ConditionNode) {
            System.out.println(indent + "ConditionNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
        } else if (node instanceof CompositeNode) {
            System.out.println(indent + "CompositeNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
            for (Node child : ((CompositeNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        } else if (node instanceof DecoratorNode) {
            System.out.println(indent + "DecoratorNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
            for (Node child : ((DecoratorNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        }
    }

}
