package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.representations.gp.FinalIndividualFitness;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.HashMap;

public class GPProgramSolutionSimple implements Serializable {

    private long individualId;
    private double[] objectives;
    private FinalIndividualFitness finalIndividualFitness;
    private int changesCount; // Number of changes made to the solution
    private int treeSize;
    private int treeDepth;
    private int terminalNodes;
    private int functionNodes;

    private String treeDotString;

    private HashMap<String, Integer> nodeCounts; // { "Sequencer": 24, "Selector": 12, "MoveForward": 5, ... }

    public GPProgramSolutionSimple(ProgramSolution programSolution){
        this.individualId = programSolution.getID();
        this.objectives = programSolution.getObjectives();
        this.finalIndividualFitness = programSolution.getFitness();
        this.changesCount = programSolution.getChangesCount();
        this.treeSize = programSolution.getTree().treeSize();
        this.treeDepth = programSolution.getTree().treeMaxDepth();
        this.terminalNodes = programSolution.getTree().getTerminalNodes().size();
        this.functionNodes = programSolution.getTree().getFunctionNodes().size();

        this.treeDotString = programSolution.getTree().toDotString();
        this.nodeCounts = programSolution.getTree().getNodeCounts();
    }

    public GPProgramSolutionSimple(long individualId, double[] objectives, FinalIndividualFitness finalIndividualFitness, int changesCount, Tree tree) {
        this.individualId = individualId;
        this.objectives = objectives;
        this.finalIndividualFitness = finalIndividualFitness;
        this.changesCount = changesCount;
        this.treeSize = tree.treeSize();
        this.treeDepth = tree.treeMaxDepth();
        this.terminalNodes = tree.getTerminalNodes().size();
        this.functionNodes = tree.getFunctionNodes().size();
        this.treeDotString = tree.toDotString();
    }

    public long getIndividualId() {
        return individualId;
    }

    public double[] getObjectives() {
        return objectives;
    }

    public FinalIndividualFitness getFinalIndividualFitness() {
        return finalIndividualFitness;
    }

    public int getChangesCount() {
        return changesCount;
    }

    public int getTreeSize() {
        return treeSize;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public int getTerminalNodes() {
        return terminalNodes;
    }

    public int getFunctionNodes() {
        return functionNodes;
    }

    public String getTreeDotString() {
        return treeDotString;
    }

}
