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
    protected int minTreeHeight;
    protected int maxTreeHeight;
    protected int maxNodeChildrenNum;
    protected boolean useAllBaseFunctions;
    protected boolean useAllBaseTerminals;

    public ProgramProblem(String name) {
        super(name, 1, 1, 0);
        this.baseFunctions = new ArrayList<>();
        this.baseTerminals = new ArrayList<>();
    }

    public List<Op<T>> getBaseFunctions() {
        return baseFunctions;
    }

    public void setBaseFunctions(List<Op<T>> baseFunctions) {
        this.baseFunctions = baseFunctions;
    }

    public List<Op<T>> getBaseTerminals() {
        return baseTerminals;
    }

    public void setBaseTerminals(List<Op<T>> baseTerminals) {
        this.baseTerminals = baseTerminals;
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

    public int getMaxNodeChildrenNum() {
        return maxNodeChildrenNum;
    }

    public void setMaxNodeChildrenNum(int maxNodeChildrenNum) {
        this.maxNodeChildrenNum = maxNodeChildrenNum;
    }

    public boolean isUseAllBaseFunctions() {
        return useAllBaseFunctions;
    }

    public void setUseAllBaseFunctions(boolean useAllBaseFunctions) {
        this.useAllBaseFunctions = useAllBaseFunctions;
    }

    public boolean isUseAllBaseTerminals() {
        return useAllBaseTerminals;
    }

    public void setUseAllBaseTerminals(boolean useAllBaseTerminals) {
        this.useAllBaseTerminals = useAllBaseTerminals;
    }
    @Override
    public boolean isFeasible(ProgramSolution<T> solution){
        //TODO
        return true;
    }

    @Override
    public void makeFeasible(ProgramSolution<T> solution){
        //TODO
    }
    @Override
    public ProgramSolution<T> getRandomEvaluatedSolution() {
        ProgramSolution<T> solution = getRandomSolution();
        evaluate(solution);
        return solution;
    }

    @Override
    public ProgramSolution<T> getRandomSolution() {
        ProgramSolution<T> newSolution = new ProgramSolution<>(numberOfObjectives);
        //Generate random valid solution
        /*Postopek generiranja drevesa...
            Generiram en node (prvi more bit funckija, ker drugace se bo tukaj koncalo)
            Nato pa gradim drevo po en node naprej, dokler ne pridem do samih takšnih voz., ki so terminali
        */

        //In first we select one from baseFunctions, otherwise the tree would stop here (we also exclude functions that are actually constants)
        List<Op<T>> filteredOperations = this.baseFunctions.stream().filter(x -> (!x.isVariable() && !x.isTerminal() && !x.isConstant())).toList();
        Op<T> op = filteredOperations.get(Util.rnd.nextInt(filteredOperations.size()));
        TreeNode<T> rootNode = new TreeNode<>(op);

        for(int i = 0; i < rootNode.getOperation().arity(); i++){
            generateSubTree(rootNode, i, 0);
        }

        newSolution.program = rootNode;
        return newSolution;
    }

    private void generateSubTree(TreeNode<T> node, int index, int currentDepth) {
        //Randomly select one terminal or one function
        if(currentDepth < this.maxTreeHeight - 1 && Util.rnd.nextInt(2) == 0) { //Function
            Op<T> op = this.baseFunctions.get(Util.rnd.nextInt(this.baseFunctions.size()));

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
            TreeNode<T> child = new TreeNode<T>(t);
            node.insert(index, child);
        }

    }

}