package org.um.feri.ears.problems.gp;

import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.List;

public class ProgramSolution<T> extends Solution {
    protected TreeNode<T> program;

    public List<ProgramSolution<T>> parents;

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
    }

    public ProgramSolution(ProgramSolution<T> s) {
        super(s);
        program = s.program;
        parents = new ArrayList<>();
    }

    @Override
    public ProgramSolution<T> copy() {
        return new ProgramSolution<>(this);
    }

    public void setProgram(TreeNode<T> program) {
        this.program = program;
    }

    public TreeNode<T> getProgram() {
        return program;
    }
}