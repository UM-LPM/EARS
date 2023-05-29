package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.OperationType;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Util;

public class GPRandomProgramSolution<T> extends GPProgramSolution<T> {

    private ProgramProblem<T> programProblem;

    public ProgramSolution<T> generate(ProgramProblem<T> programProblem, boolean isRoot, int currentDepth) {
        this.programProblem = programProblem;
        return getRandomSolution(isRoot, currentDepth);
    }

    public ProgramSolution<T> getRandomSolution(boolean isRoot, int currentDepth) {
        //System.out.println("Current depth: " + currentDepth);
        ProgramSolution<T> newSolution = new ProgramSolution<>(this.programProblem.getNumberOfObjectives());

        // First we select one from baseFunctions, otherwise the tree would stop here (we also exclude functions that are actually constants). Only if we are not at the root of the tree
        Op<T> op;
        if(!isRoot && (currentDepth >= this.programProblem.getMinTreeHeight()))
            op = this.programProblem.getBaseFunctions().get(Util.rnd.nextInt(this.programProblem.getBaseFunctions().size()));
        else
            op = this.programProblem.getComplexFunctions().get(Util.rnd.nextInt(this.programProblem.getComplexFunctions().size()));

        TreeNode<T> rootNode = new TreeNode<>(op);

        if(currentDepth < this.programProblem.getMaxTreeHeight()) { //Function
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
            Op<T> t = this.programProblem.getBaseTerminals().get(Util.rnd.nextInt(this.programProblem.getBaseTerminals().size()));
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
        if((currentDepth < this.programProblem.getMaxTreeHeight() - 1 && Util.rnd.nextInt(2) == 0) || !(currentDepth >= this.programProblem.getMinTreeHeight() - 1)) { //Function
            Op<T> op;
            if(!(currentDepth > this.programProblem.getMinTreeHeight()))
                op = this.programProblem.getComplexFunctions().get(Util.rnd.nextInt(this.programProblem.getComplexFunctions().size()));
            else
                op = this.programProblem.getBaseFunctions().get(Util.rnd.nextInt(this.programProblem.getBaseFunctions().size()));

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
            Op<T> t = this.programProblem.getBaseTerminals().get(Util.rnd.nextInt(this.programProblem.getBaseTerminals().size()));
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

}
