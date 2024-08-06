package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTreeNodeType;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.Property;
import org.um.feri.ears.util.GraphPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoalNode extends BehaviourTreeNode {

    private String goalName;

    public GoalNode(){
        this(BehaviourTreeNodeType.GOAL_NODE, "None");
    }

    public GoalNode(String goalName){
        this(BehaviourTreeNodeType.GOAL_NODE, goalName);
    }
    public GoalNode(BehaviourTreeNodeType nodeType, String goalName){
        this(nodeType, null, 0, goalName);
    }

    public GoalNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, String goalName){
        this(nodeType, properties , arity, new ArrayList<>(), goalName);
    }

    public GoalNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children, String goalName) {
        super(nodeType, properties, arity, children, true);
        this.goalName = goalName;
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
        gp.addln(id + "[label=<"+ this.name +"<BR /><FONT POINT-SIZE=\"10\">" + this.goalName + "</FONT>>, "+ this.setNodeStyle() + "]");

        // TODO Remove this after testing
        for (Node next : children) {
            next.setTreeNodes(gp);
        }
    }

    public void setGoal(String goalName, Node goalBehaviour){
        this.goalName = goalName;
        this.children.clear();
        this.children.add(goalBehaviour);
    }
}
