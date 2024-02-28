package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.List;

public abstract class ProgramProblem extends Problem<ProgramSolution> {

    /**
     * List of base function nodes which can be used during when generating tree individuals
     */
    List<Class<? extends Node>> baseFunctionNodeTypes;

    /**
     * List of base terminal nodes which can be used during when generating tree individuals
     */
    List<Class<? extends Node>> baseTerminalNodeTypes;

    //protected List<Op> simpleFunctions;
    //protected List<Op> complexFunctions;

    protected Tree.TreeType solutionTreeType;
    protected String treeName; // TODO add support for multiple treeNames in the future

    protected int minTreeDepth;
    protected int maxTreeDepth;
    protected int maxTreeSize;

    protected GPOperator treeDepthPruningOperator;
    protected GPOperator expansionOperator;
    protected GPOperator treeSizePruningOperator;

    protected GPProgramSolution programSolutionGenerator;

    // Default constructor
    public ProgramProblem(String name) {
        super(name, 1, 1, 0);
        this.baseFunctionNodeTypes = new ArrayList<>();
        this.baseTerminalNodeTypes = new ArrayList<>();
        this.minTreeDepth = 2;
        this.maxTreeDepth = 100;
        this.maxTreeSize = 1000;

        this.treeDepthPruningOperator = new GPDepthBasedTreePruningOperator();
        this.expansionOperator = new GPTreeExpansionOperator();
        this.programSolutionGenerator = new GPRandomProgramSolution();
        this.treeSizePruningOperator = new GPTreeSizePruningOperator();
        this.solutionTreeType = Tree.TreeType.SYMBOLIC;
    }

    // Constructor with all parameters
    public ProgramProblem(String name, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeDepth, int maxTreeSize, GPOperator pruningOperator, GPOperator expansionOperator, GPOperator treeSizePruningOperator, GPProgramSolution programSolutionGenerator, Tree.TreeType treeType, String treeName) {
        super(name, 1, 1, 0);
        setBaseFunctionNodeTypes(baseFunctionNodeTypes);
        setBaseTerminalNodeTypes(baseTerminalNodeTypes);
        this.solutionTreeType = treeType;
        this.treeName = treeName;

        this.minTreeDepth = minTreeDepth;
        this.maxTreeDepth = maxTreeDepth;
        this.maxTreeSize = maxTreeSize;

        this.treeDepthPruningOperator = pruningOperator;
        this.expansionOperator = expansionOperator;
        this.treeSizePruningOperator = treeSizePruningOperator;
        this.programSolutionGenerator = programSolutionGenerator;
    }


    public List<Class<? extends Node>> getBaseFunctionNodeTypes() {
        return baseFunctionNodeTypes;
    }

    public void setBaseFunctionNodeTypes(List<Class<? extends Node>> baseFunctionNodeTypes) {
        this.baseFunctionNodeTypes = baseFunctionNodeTypes;
    }

    public List<Class<? extends Node>> getBaseTerminalNodeTypes() {
        return baseTerminalNodeTypes;
    }

    public void setBaseTerminalNodeTypes(List<Class<? extends Node>> baseTerminalNodeTypes) {
        this.baseTerminalNodeTypes = baseTerminalNodeTypes;
    }

    public Tree.TreeType getSolutionTreeType() {
        return solutionTreeType;
    }

    public int getMaxTreeDepth() {
        return maxTreeDepth;
    }


    public void setMaxTreeDepth(int maxTreeDepth) {
        this.maxTreeDepth = maxTreeDepth;
    }

    public int getMinTreeDepth() {
        return minTreeDepth;
    }

    public void setMinTreeDepth(int minTreeDepth) {
        this.minTreeDepth = minTreeDepth;
    }

    public GPOperator getTreeDepthPruningOperator() {
        return treeDepthPruningOperator;
    }

    public void setTreeDepthPruningOperator(GPOperator treeDepthPruningOperator) {
        this.treeDepthPruningOperator = treeDepthPruningOperator;
    }

    public GPOperator getExpansionOperator() {
        return expansionOperator;
    }

    public void setExpansionOperator(GPOperator expansionOperator) {
        this.expansionOperator = expansionOperator;
    }

    public int getMaxTreeSize() {
        return maxTreeSize;
    }

    public void setMaxTreeSize(int maxTreeSize) {
        this.maxTreeSize = maxTreeSize;
    }

    public GPProgramSolution getProgramSolutionGenerator() {
        return programSolutionGenerator;
    }

    public void setProgramSolutionGenerator(GPProgramSolution programSolutionGenerator) {
        this.programSolutionGenerator = programSolutionGenerator;
    }

    @Override
    public boolean isFeasible(ProgramSolution solution){
        int treeDepth = solution.getTree().treeDepth();
        int treeSize = solution.getTree().treeSize();

        return treeDepth <= this.getMaxTreeDepth() && treeDepth >= this.getMinTreeDepth() && treeSize <= this.getMaxTreeSize();
    }

    @Override
    public void makeFeasible(ProgramSolution solution){
        expandProgramSolution(solution);
        treeDepthProgramSolutionPruning(solution);
        treeSizeProgramSolutionPruning(solution);
    }
    @Override
    public ProgramSolution getRandomEvaluatedSolution() {
        ProgramSolution solution = getRandomSolution();
        evaluate(solution);
        return solution;
    }

    @Override
    public ProgramSolution getRandomSolution() {
        return this.programSolutionGenerator.generate(this, 1, treeName);
    }

    public Node getRandomTerminalNode(){
        return this.programSolutionGenerator.generateRandomTerminalNode(this);
    }

    public void treeDepthProgramSolutionPruning(ProgramSolution solution){
        this.treeDepthPruningOperator.execute(solution, this);
    }

    public void expandProgramSolution(ProgramSolution solution){
        this.expansionOperator.execute(solution, this);
    }

    public void treeSizeProgramSolutionPruning(ProgramSolution solution){
        this.treeSizePruningOperator.execute(solution, this);
    }

    @Override
    public void bulkEvaluate(List<ProgramSolution> solutions) {
        for (ProgramSolution solution : solutions) {
            evaluate(solution);
        }
    }



}
