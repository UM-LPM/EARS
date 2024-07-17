package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.generations.gp.GPProgramSolution;
import org.um.feri.ears.individual.generations.gp.GPRampedHalfAndHalf;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.operators.gp.*;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.individual.representations.gp.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ProgramProblem extends Problem<ProgramSolution> {

    public static final long serialVersionUID = 2857391887500457501L;

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
    protected int maxTreeStartDepth;

    protected int maxTreeEndDepth;
    protected int maxTreeSize;

    protected GPOperator treeDepthPruningOperator;
    protected GPOperator expansionOperator;
    protected GPOperator treeSizePruningOperator;

    protected GPProgramSolution programSolutionGenerator;

    protected String[] evalEnvInstanceURIs;
    protected String jsonBodyDestFolderPath;

    // Default constructor
    public ProgramProblem(String name) {
        super(name, 1, 1, 0);
        this.baseFunctionNodeTypes = new ArrayList<>();
        this.baseTerminalNodeTypes = new ArrayList<>();
        this.minTreeDepth = 2;
        this.maxTreeStartDepth = 10;
        this.maxTreeEndDepth = 100;
        this.maxTreeSize = 1000;

        this.treeDepthPruningOperator = new GPDepthBasedTreePruningOperator();
        this.expansionOperator = new GPTreeExpansionOperator();
        this.programSolutionGenerator = new GPRandomProgramSolution();
        this.treeSizePruningOperator = new GPTreeSizePruningOperator();
        this.solutionTreeType = Tree.TreeType.SYMBOLIC;
    }

    // Constructor with all parameters
    public ProgramProblem(String name, List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeDepth, int maxTreeStartDepth, int maxTreeEndDepth, int maxTreeSize, GPOperator pruningOperator, GPOperator expansionOperator, GPOperator treeSizePruningOperator, GPProgramSolution programSolutionGenerator, Tree.TreeType treeType, String treeName, String[] evalEnvInstanceURIs) {
        super(name, 1, 1, 0);
        setBaseFunctionNodeTypes(baseFunctionNodeTypes);
        setBaseTerminalNodeTypes(baseTerminalNodeTypes);
        this.solutionTreeType = treeType;
        this.treeName = treeName;

        this.minTreeDepth = minTreeDepth;
        this.maxTreeStartDepth = maxTreeStartDepth;
        this.maxTreeEndDepth = maxTreeEndDepth;
        this.maxTreeSize = maxTreeSize;

        this.treeDepthPruningOperator = pruningOperator;
        this.expansionOperator = expansionOperator;
        this.treeSizePruningOperator = treeSizePruningOperator;
        this.programSolutionGenerator = programSolutionGenerator;

        this.evalEnvInstanceURIs = evalEnvInstanceURIs;
    }


    public List<Class<? extends Node>> getBaseFunctionNodeTypes() {
        return baseFunctionNodeTypes;
    }

    public void setBaseFunctionNodeTypes(List<Class<? extends Node>> baseFunctionNodeTypes) {
        this.baseFunctionNodeTypes = baseFunctionNodeTypes;
    }

    public void setBaseFunctionNodeTypesFromStringList(List<String> baseFunctionNodeTypes) {
        this.baseFunctionNodeTypes = new ArrayList<>();
        String packagePrefix = "org.um.feri.ears.individual.representations.gp.behaviour.tree.";
        for (String nodeType : baseFunctionNodeTypes) {
            try {
                this.baseFunctionNodeTypes.add((Class<? extends Node>) Class.forName(packagePrefix + nodeType.trim()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Class<? extends Node>> getBaseTerminalNodeTypes() {
        return baseTerminalNodeTypes;
    }

    public void setBaseTerminalNodeTypes(List<Class<? extends Node>> baseTerminalNodeTypes) {
        this.baseTerminalNodeTypes = baseTerminalNodeTypes;
    }

    public void setBaseTerminalNodeTypesFromStringList(List<String> baseTerminalNodeTypes) {
        this.baseTerminalNodeTypes = new ArrayList<>();
        String packagePrefix = "org.um.feri.ears.individual.representations.gp.behaviour.tree.";
        for (String nodeType : baseTerminalNodeTypes) {
            try {
                this.baseTerminalNodeTypes.add((Class<? extends Node>) Class.forName(packagePrefix + nodeType.trim()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Class<? extends  Node> getRandomNodeType(){
        ArrayList<Class<? extends  Node>> nodeTypes = new ArrayList<>();
        nodeTypes.addAll(baseFunctionNodeTypes);
        nodeTypes.addAll(baseTerminalNodeTypes);

        return nodeTypes.get(RNG.nextInt(nodeTypes.size()));
    }

    public Tree.TreeType getSolutionTreeType() {
        return solutionTreeType;
    }

    public int getMaxTreeStartDepth() {
        return maxTreeStartDepth;
    }

    public int getMaxTreeEndDepth() {
        return maxTreeEndDepth;
    }


    public void setMaxTreeStartDepth(int maxTreeStartDepth) {
        this.maxTreeStartDepth = maxTreeStartDepth;
    }

    public void setMaxTreeEndDepth(int maxTreeEndDepth) {
        this.maxTreeEndDepth = maxTreeEndDepth;
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

    public void setProgramSolutionGenerator(Configuration.InitPopGeneratorMethod initPopGeneratorMethod) {
        if(initPopGeneratorMethod == Configuration.InitPopGeneratorMethod.Random){
            this.programSolutionGenerator = new GPRandomProgramSolution();
        }
        else if(initPopGeneratorMethod == Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod){
            this.programSolutionGenerator = new GPRampedHalfAndHalf();
        }
    }


    public void setEvalEnvInstanceURIs(String[] evalEnvInstanceURIs) {
        this.evalEnvInstanceURIs = evalEnvInstanceURIs;
    }

    public String[] getEvalEnvInstanceURIs() {
        return evalEnvInstanceURIs;
    }

    public void setJsonBodyDestFolderPath(String jsonBodyDestFolderPath){
        this.jsonBodyDestFolderPath = jsonBodyDestFolderPath;
    }

    public String getJsonBodyDestFolderPath(){
        return jsonBodyDestFolderPath;
    }

    @Override
    public boolean isFeasible(ProgramSolution solution){
        int treeDepth = solution.getTree().treeDepth();
        int treeSize = solution.getTree().treeSize();

        // TODO add support for treeSize feasibility ???
        return treeDepth <= this.getMaxTreeEndDepth() && treeDepth >= this.getMinTreeDepth(); // && treeSize <= this.getMaxTreeSize();
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
        ProgramSolution solution = this.programSolutionGenerator.generate(this, 1, treeName);

        if(!isFeasible(solution))
            System.out.println("Solution is not feasible");
            //makeFeasible(solution);

        return solution;
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
        if(treeSizePruningOperator == null)
            return;

        this.treeSizePruningOperator.execute(solution, this);
    }

    @Override
    public void bulkEvaluate(List<ProgramSolution> solutions) {
        for (ProgramSolution solution : solutions) {
            evaluate(solution);
        }
    }



}
