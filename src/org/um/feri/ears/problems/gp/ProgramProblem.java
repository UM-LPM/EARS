package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.OperationType;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator;
import org.um.feri.ears.operators.gp.GPOperator;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.List;

public abstract class ProgramProblem<T> extends Problem<ProgramSolution<T>> {

    /**
     * List of base functions which can be used during when generating tree individuals
     */
    protected List<Op<T>> baseFunctions;

    /**
     * List of base terminals which can be used during when generating tree individuals
     */
    protected List<Op<T>> baseTerminals;

    protected List<Op<T>> simpleFunctions;
    protected List<Op<T>> complexFunctions;

    protected int minTreeHeight;
    protected int maxTreeHeight;
    protected int maxTreeNodes; // TODO add support for max tree nodes (feasible)

    protected GPOperator<T> pruningOperator;
    protected GPOperator<T> expansionOperator;

    protected GPProgramSolution<T> programSolutionGenerator;


    // Default constructor
    public ProgramProblem(String name) {
        super(name, 1, 1, 0);
        this.baseFunctions = new ArrayList<>();
        this.baseTerminals = new ArrayList<>();
        this.minTreeHeight = 2;
        this.maxTreeHeight = 100;
        this.maxTreeNodes = 1000;

        this.pruningOperator = new GPDepthBasedTreePruningOperator<>();
        this.expansionOperator = new GPTreeExpansionOperator<>();
        this.programSolutionGenerator = new GPRandomProgramSolution<>();
    }

    // Constructor with all parameters
    public ProgramProblem(String name, List<Op<T>> baseFunctions, List<Op<T>> baseTerminals, int minTreeHeight, int maxTreeHeight, int maxTreeNodes, GPOperator<T> pruningOperator, GPOperator<T> expansionOperator, GPProgramSolution<T> programSolutionGenerator) {
        super(name, 1, 1, 0);
        setBaseFunctions(baseFunctions);
        setBaseTerminals(baseTerminals);
        this.minTreeHeight = minTreeHeight;
        this.maxTreeHeight = maxTreeHeight;
        this.maxTreeNodes = maxTreeNodes;

        this.pruningOperator = pruningOperator;
        this.expansionOperator = expansionOperator;
        this.programSolutionGenerator = programSolutionGenerator;
    }


    public List<Op<T>> getBaseFunctions() {
        return baseFunctions;
    }

    public void setBaseFunctions(List<Op<T>> baseFunctions) {
        this.baseFunctions = baseFunctions;
        setComplexFunctions();
        setSimpleFunctions();
    }

    public List<Op<T>> getBaseTerminals() {
        return baseTerminals;
    }

    public void setBaseTerminals(List<Op<T>> baseTerminals) {
        this.baseTerminals = baseTerminals;
    }

    public List<Op<T>> getComplexFunctions() {
        return complexFunctions;
    }

    public void setComplexFunctions() {
        this.complexFunctions = this.baseFunctions.stream().filter(x -> x.isComplex()).toList();
    }

    public List<Op<T>> getSimpleFunctions() {
        return simpleFunctions;
    }

    public void setSimpleFunctions() {
        this.complexFunctions = this.baseFunctions.stream().filter(x -> x.isComplex()).toList();
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

    public GPOperator<T> getPruningOperator() {
        return pruningOperator;
    }

    public void setPruningOperator(GPOperator<T> pruningOperator) {
        this.pruningOperator = pruningOperator;
    }

    public GPOperator<T> getExpansionOperator() {
        return expansionOperator;
    }

    public void setExpansionOperator(GPOperator<T> expansionOperator) {
        this.expansionOperator = expansionOperator;
    }

    public int getMaxTreeNodes() {
        return maxTreeNodes;
    }

    public void setMaxTreeNodes(int maxTreeNodes) {
        this.maxTreeNodes = maxTreeNodes;
    }

    public GPProgramSolution<T> getProgramSolutionGenerator() {
        return programSolutionGenerator;
    }

    public void setProgramSolutionGenerator(GPProgramSolution<T> programSolutionGenerator) {
        this.programSolutionGenerator = programSolutionGenerator;
    }

    @Override
    public boolean isFeasible(ProgramSolution<T> solution){
        int treeHeight = solution.getProgram().treeHeight();
        return treeHeight <= this.getMaxTreeHeight() && treeHeight >= this.getMinTreeHeight();
    }

    @Override
    public void makeFeasible(ProgramSolution<T> solution){
        expandProgramSolution(solution);
        pruneProgramSolution(solution);
    }
    @Override
    public ProgramSolution<T> getRandomEvaluatedSolution() {
        ProgramSolution<T> solution = getRandomSolution();
        evaluate(solution);
        return solution;
    }

    public ProgramSolution<T> getRandomSolution() {
        return this.programSolutionGenerator.generate(this, false, 1);
    }

    public void pruneProgramSolution(ProgramSolution<T> solution){
        this.pruningOperator.execute(solution, this);
    }

    public void expandProgramSolution(ProgramSolution<T> solution){
        this.expansionOperator.execute(solution, this);
    }

    @Override
    public void bulkEvaluate(List<ProgramSolution<T>> solutions) {
        for (ProgramSolution<T> solution : solutions) {
            evaluate(solution);
        }
    }




}
