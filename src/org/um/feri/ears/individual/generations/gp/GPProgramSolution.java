package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.algorithms.gp.GPAlgorithmExecutor;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.EncapsulatedNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.EncapsulatedNodeDefinition;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.random.RNG;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class GPProgramSolution implements Serializable {

    public static final long serialVersionUID = 5655931880676281285L;

    protected List<EncapsulatedNodeDefinition> encapsulatedNodeDefinitions;

    public GPProgramSolution() {
        this.encapsulatedNodeDefinitions = new ArrayList<>();;
    }

    public abstract ProgramSolution generate(ProgramProblem programProblem, int startDepth, String treeName);

    public abstract Node generateRandomTerminalNode(ProgramProblem programProblem);

    public abstract Configuration.InitPopGeneratorMethod getInitPopGeneratorMethod();

    public List<EncapsulatedNodeDefinition> getEncapsulatedNodeDefinitions() {
        return encapsulatedNodeDefinitions;
    }

    public void setEncapsulatedNodeDefinitions(List<EncapsulatedNodeDefinition> encapsulatedNodeDefinitions) {
        this.encapsulatedNodeDefinitions = encapsulatedNodeDefinitions;
    }

    public void addEncapsulatedNodeDefinition(List<EncapsulatedNodeDefinition> encapsulatedNodeDefinitions) {
        this.encapsulatedNodeDefinitions.addAll(encapsulatedNodeDefinitions);
    }

    /**
     * Assigns a random EncapsulatedNodeDefinition to a EncapsulatedNode (If passed Node is not a EncapsulatedNode, it will be returned as is)
     * @param node Node to assign EncapsulatedNodeDefinition to
     * @return Modified Node
     */
    public Node assignEncapsulatedNodeToEncapsulatedNode(Node node) {
        if(node instanceof EncapsulatedNode encapsulatedNode){
            if(encapsulatedNodeDefinitions.isEmpty()){
                throw new RuntimeException("EncapsulatedNodeDefinitions are empty");
            }
            // Select a random EncapsulatedNodeDefinition and set it to EncapsulatedNode
            EncapsulatedNodeDefinition encapsulatedNodeDefinition = encapsulatedNodeDefinitions.get(RNG.nextInt(encapsulatedNodeDefinitions.size()));
            encapsulatedNode.setEncapsulatedNode(encapsulatedNodeDefinition.getEncapsulatedNodeName(), encapsulatedNodeDefinition.getGoalNodeBehaviour().clone());
        }
        return node;
    }
}
