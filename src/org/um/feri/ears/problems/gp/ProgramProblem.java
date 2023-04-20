package org.um.feri.ears.problems.gp;

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

    public ProgramProblem(String name) {
        super(name, 1, 1, 0);
        this.baseFunctions = new ArrayList<>();
        this.baseTerminals = new ArrayList<>();
        this.minTreeHeight = 0;
        this.maxTreeHeight = 100;
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

    @Override
    public boolean isFeasible(ProgramSolution<T> solution){
        // TODO add more conditions if necesary
        int treeHeight = solution.getProgram().treeHeight();
        return treeHeight <= this.getMaxTreeHeight() && treeHeight >= this.getMinTreeHeight();
    }

    @Override
    public void makeFeasible(ProgramSolution<T> solution){
        // TODO add more if more conditions  exist
        pruneProgramSolution(solution);
        completeProgramSolution(solution);
    }
    @Override
    public ProgramSolution<T> getRandomEvaluatedSolution() {
        ProgramSolution<T> solution = getRandomSolution();
        evaluate(solution);
        return solution;
    }

    public ProgramSolution<T> getRandomSolution() {
        return getRandomSolution(true, 1);
    }

    public ProgramSolution<T> getRandomSolution(boolean isRoot, int currentDepth) {
        //System.out.println("Current depth: " + currentDepth);
        ProgramSolution<T> newSolution = new ProgramSolution<>(numberOfObjectives);

        // First we select one from baseFunctions, otherwise the tree would stop here (we also exclude functions that are actually constants). Only if we are not at the root of the tree
        Op<T> op;
        if(!isRoot && (currentDepth >= this.minTreeHeight))
            op = this.baseFunctions.get(Util.rnd.nextInt(this.baseFunctions.size()));
        else
            op = this.complexFunctions.get(Util.rnd.nextInt(this.complexFunctions.size()));

        TreeNode<T> rootNode = new TreeNode<>(op);

        if(currentDepth < this.maxTreeHeight) { //Function
            if (op.isConstant()) {
                rootNode = new TreeNode<>(op.apply(null));
                TreeNode<T> finalRootNode = rootNode;
                rootNode.setOperation(Op.define(rootNode.getCoefficient().toString(), OperationType.TERMINAL, 0, v -> finalRootNode.getCoefficient()));
            } else {
                for (int i = 0; i < rootNode.getOperation().arity(); i++) {
                    generateSubTree(rootNode, i, currentDepth);
                }
            }
        }else { //Terminal
            Op<T> t = this.baseTerminals.get(Util.rnd.nextInt(this.baseTerminals.size()));
            if(t.isConstant()){
                rootNode = new TreeNode<>(t.apply(null));
                TreeNode<T> finalRootNode = rootNode;
                rootNode.setOperation(Op.define(rootNode.getCoefficient().toString(), OperationType.TERMINAL, 0, v -> finalRootNode.getCoefficient()));
            }
            else{
                rootNode = new TreeNode<>(t);
            }
        }

        newSolution.setProgram(rootNode);
        return newSolution;
    }

    private void generateSubTree(TreeNode<T> node, int index, int currentDepth) {
        //Randomly select one terminal or one function
        if((currentDepth < this.maxTreeHeight - 1 && Util.rnd.nextInt(2) == 0) || !(currentDepth >= this.minTreeHeight - 1)) { //Function
            Op<T> op;
            if(!(currentDepth > this.minTreeHeight))
                op = this.complexFunctions.get(Util.rnd.nextInt(this.complexFunctions.size()));
            else
                op = this.baseFunctions.get(Util.rnd.nextInt(this.baseFunctions.size()));

            if(op.isConstant()){
                TreeNode<T> childNode = new TreeNode<>(op.apply(null));
                childNode.setOperation(Op.define(childNode.getCoefficient().toString(), OperationType.TERMINAL, 0, v -> childNode.getCoefficient()));
                node.insert(index, childNode);
            }
            else{
                TreeNode<T> childNode = new TreeNode<>(op);

                node.insert(index, childNode);
                for(int i = 0; i < childNode.getOperation().arity(); i++){
                    generateSubTree(childNode, i, currentDepth + 1);
                }
            }
        }
        else { //Terminal
            Op<T> t = this.baseTerminals.get(Util.rnd.nextInt(this.baseTerminals.size()));
            if(t.isConstant()){
                TreeNode<T> child = new TreeNode<>(t.apply(null));
                child.setOperation(Op.define(child.getCoefficient().toString(), OperationType.TERMINAL, 0, v -> child.getCoefficient()));
                node.insert(index, child);
            }
            else{
                TreeNode<T> child = new TreeNode<>(t);
                node.insert(index, child);
            }
        }
    }

    public void pruneProgramSolution(ProgramSolution<T> solution){
        pruneProgramHeight(solution.getProgram(), 1);
    }

    private void pruneProgramHeight(TreeNode<T> current, int currentHeight){
        if(currentHeight >= this.maxTreeHeight){
            //current.getChildren().clear();
            if(current.operation().isComplex()){
                ProgramSolution<T> newSolution = getRandomSolution(false, currentHeight);
                if(current.parent().isPresent()){
                    try {
                        current.parent().get().replace(current, newSolution.getProgram());
                    } catch (Exception e) {
                        throw new RuntimeException("Error replacing node");
                    }
                }

            }
        }else{
            for (TreeNode child : current.getChildren()) {
                pruneProgramHeight(child, currentHeight + 1);
            }
        }
    }

    public void completeProgramSolution(ProgramSolution<T> solution){
        completeProgramHeight(solution.getProgram(), 1);
    }

    private void completeProgramHeight(TreeNode<T> current, int currentHeight){
        if(currentHeight < this.minTreeHeight){
            if(!current.getOperation().isComplex()){
                ProgramSolution<T> newSolution = getRandomSolution(false, currentHeight);
                if(current.parent().isPresent()){
                    current.parent().get().replace(current, newSolution.getProgram());
                }
            }else {
                for (TreeNode child : current.getChildren()) {
                    completeProgramHeight(child, currentHeight + 1);
                }
            }
        }
    }







}
