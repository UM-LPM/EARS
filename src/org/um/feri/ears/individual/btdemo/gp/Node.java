package org.um.feri.ears.individual.btdemo.gp;

import org.um.feri.ears.individual.btdemo.gp.behaviour.*;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;
import org.um.feri.ears.individual.representations.gp.TreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Node {

    public static int CURRENT_ID = 1;

    private int id;

    public Node() {
        this.id = Node.CURRENT_ID;
        Node.CURRENT_ID += 1;
    }

    public abstract double evaluate(Map<String, Double> variables);

    public static void printTree(Node node, String indent) {
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
            printTree(((RootNode) node).getChild(), indent + "  ");
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
            printTree(((DecoratorNode) node).getChild(), indent + "  ");
        }
    }
}