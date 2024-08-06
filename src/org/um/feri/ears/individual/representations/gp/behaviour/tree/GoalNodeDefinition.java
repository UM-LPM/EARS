package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import org.um.feri.ears.individual.representations.gp.Node;

public class GoalNodeDefinition {
    private String goalName;
    private Node goalNodeBehaviour;

    public GoalNodeDefinition(String goalName, Node goalNodeBehaviour) {
        this.goalName = goalName;
        this.goalNodeBehaviour = goalNodeBehaviour;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Node getGoalNodeBehaviour() {
        return goalNodeBehaviour;
    }

    public void setGoalNodeBehaviour(Node goalNodeBehaviour) {
        this.goalNodeBehaviour = goalNodeBehaviour;
    }

}
