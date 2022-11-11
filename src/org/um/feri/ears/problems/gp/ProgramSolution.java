package org.um.feri.ears.problems.gp;

import org.um.feri.ears.problems.SolutionBase;

import java.util.ArrayList;
import java.util.List;

public class ProgramSolution<T> extends SolutionBase {
    protected TreeNode<T> program; //Solution
    protected double eval; //SolutionFitness

    public List<ProgramSolution<T>> parents;

    public ProgramSolution() {

    }

    public ProgramSolution(ProgramSolution<T> s) {
        super(s);

        eval = s.eval;
        program = s.program;
        parents = new ArrayList<>();
    }


    public void setProgram(TreeNode<T> program) {
        this.program = program;
    }

    public void setEval(double eval) {
        this.eval = eval;
    }

    public TreeNode<T> getProgram() {
        return program;
    }

    public double getEval() {
        return eval;
    }

}