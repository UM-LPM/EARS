package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.individual.representations.gp.FinalIndividualFitness;

import java.io.Serializable;

public class GPProgramSolutionSimple implements Serializable {

    private int individualId;
    private double[] objectives;
    private FinalIndividualFitness finalIndividualFitness;
    private int changesCount; // Number of changes made to the solution
    private int treeSize;
    private int treeDepth;
    private int terminalNodes;
    private int functionNodes;

    private String treeDotString;

    public GPProgramSolutionSimple(int individualId, double[] objectives, FinalIndividualFitness finalIndividualFitness, int changesCount, int treeSize, int treeDepth, int terminalNodes, int functionNodes, String treeDotString) {
        this.individualId = individualId;
        this.objectives = objectives;
        this.finalIndividualFitness = finalIndividualFitness;
        this.changesCount = changesCount;
        this.treeSize = treeSize;
        this.treeDepth = treeDepth;
        this.terminalNodes = terminalNodes;
        this.functionNodes = functionNodes;
        this.treeDotString = treeDotString;
    }

    public int getIndividualId() {
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
