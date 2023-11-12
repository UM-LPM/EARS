package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPProgramSolution2;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution2;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.List;

public abstract class ProgramProblem2 extends Problem<ProgramSolution2> {

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

    protected int minTreeHeight;
    protected int maxTreeHeight;
    protected int maxTreeNodes; // TODO add support for max tree nodes (feasible)

    protected GPOperator2 pruningOperator;
    protected GPOperator2 expansionOperator;

    protected GPProgramSolution2 programSolutionGenerator;

    // Default constructor
    public ProgramProblem2(String name) {
        super(name, 1, 1, 0);
        this.baseFunctionNodeTypes = new ArrayList<>();
        this.baseTerminalNodeTypes = new ArrayList<>();
        this.minTreeHeight = 2;
        this.maxTreeHeight = 100;
        this.maxTreeNodes = 1000;

        this.pruningOperator = new GPDepthBasedTreePruningOperator2();
        this.expansionOperator = new GPTreeExpansionOperator2();
        this.programSolutionGenerator = new GPRandomProgramSolution2();
        this.solutionTreeType = Tree.TreeType.SYMBOLIC;
    }

    // Constructor with all parameters
    public ProgramProblem2(String name, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeHeight, int maxTreeHeight, int maxTreeNodes, GPOperator2 pruningOperator, GPOperator2 expansionOperator, GPProgramSolution2 programSolutionGenerator, Tree.TreeType treeType, String treeName) {
        super(name, 1, 1, 0);
        setBaseFunctionNodeTypes(baseFunctionNodeTypes);
        setBaseTerminalNodeTypes(baseTerminalNodeTypes);
        this.solutionTreeType = treeType;
        this.treeName = treeName;

        this.minTreeHeight = minTreeHeight;
        this.maxTreeHeight = maxTreeHeight;
        this.maxTreeNodes = maxTreeNodes;

        this.pruningOperator = pruningOperator;
        this.expansionOperator = expansionOperator;
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

    public int getMaxTreeHeight() {
        return maxTreeHeight;
    }


    public void setMaxTreeHeight(int maxTreeHeight) {
        this.maxTreeHeight = maxTreeHeight;
    }

    public int getMinTreeHeight() {
        return minTreeHeight;
    }

    public void setMinTreeHeight(int minTreeHeight) {
        this.minTreeHeight = minTreeHeight;
    }

    public GPOperator2 getPruningOperator() {
        return pruningOperator;
    }

    public void setPruningOperator(GPOperator2 pruningOperator) {
        this.pruningOperator = pruningOperator;
    }

    public GPOperator2 getExpansionOperator() {
        return expansionOperator;
    }

    public void setExpansionOperator(GPOperator2 expansionOperator) {
        this.expansionOperator = expansionOperator;
    }

    public int getMaxTreeNodes() {
        return maxTreeNodes;
    }

    public void setMaxTreeNodes(int maxTreeNodes) {
        this.maxTreeNodes = maxTreeNodes;
    }

    public GPProgramSolution2 getProgramSolutionGenerator() {
        return programSolutionGenerator;
    }

    public void setProgramSolutionGenerator(GPProgramSolution2 programSolutionGenerator) {
        this.programSolutionGenerator = programSolutionGenerator;
    }

    @Override
    public boolean isFeasible(ProgramSolution2 solution){
        int treeHeight = solution.getTree().treeHeight();
        return treeHeight <= this.getMaxTreeHeight() && treeHeight >= this.getMinTreeHeight();
    }

    @Override
    public void makeFeasible(ProgramSolution2 solution){
        expandProgramSolution(solution);
        pruneProgramSolution(solution); // TODO fix problem with pruning
    }
    @Override
    public ProgramSolution2 getRandomEvaluatedSolution() {
        ProgramSolution2 solution = getRandomSolution();
        evaluate(solution);
        return solution;
    }

    @Override
    public ProgramSolution2 getRandomSolution() {
        return this.programSolutionGenerator.generate(this, 1, treeName);
    }

    public void pruneProgramSolution(ProgramSolution2 solution){
        this.pruningOperator.execute(solution, this);
    }

    public void expandProgramSolution(ProgramSolution2 solution){
        this.expansionOperator.execute(solution, this);
    }

    @Override
    public void bulkEvaluate(List<ProgramSolution2> solutions) {
        for (ProgramSolution2 solution : solutions) {
            evaluate(solution);
        }
    }



}
