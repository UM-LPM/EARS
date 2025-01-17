package org.um.feri.ears.util.gp_stats;

import org.um.feri.ears.individual.representations.gp.FinalIndividualFitness;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.HashMap;

public class GPProgramSolutionSimple implements Serializable {

    public static final long serialVersionUID = 4802911306124645524L;

    protected long individualId;
    protected double[] objectives;
    protected FinalIndividualFitness finalIndividualFitness;
    protected int changesCount; // Number of changes made to the solution
    protected int treeSize;
    protected int treeDepth;
    protected int terminalNodes;
    protected int functionNodes;

    protected String treeDotString;

    protected HashMap<String, Integer> nodeCounts; // { "Sequencer": 24, "Selector": 12, "MoveForward": 5, ... }
    protected String configurationName;

    public GPProgramSolutionSimple(ProgramSolution programSolution, boolean setIndividualMatchResults) {
        this.individualId = programSolution.getID();
        this.objectives = programSolution.getObjectives();
        this.finalIndividualFitness = new FinalIndividualFitness(programSolution.getFitness(), setIndividualMatchResults);
        this.changesCount = programSolution.getChangesCount();
        this.treeSize = programSolution.getTree().treeSize();
        this.treeDepth = programSolution.getTree().treeMaxDepth();
        this.terminalNodes = programSolution.getTree().getTerminalNodes().size();
        this.functionNodes = programSolution.getTree().getFunctionNodes().size();

        //this.treeDotString = programSolution.getTree().toDotString(); // TODO Is this required ?
        this.treeDotString = ""; // TODO Remove this
        this.nodeCounts = programSolution.getTree().getNodeCounts();
        this.configurationName = programSolution.getConfigurationName();
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

    public HashMap<String, Integer> getNodeCounts() {
        return nodeCounts;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

}
