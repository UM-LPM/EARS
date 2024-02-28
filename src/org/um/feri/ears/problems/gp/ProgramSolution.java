package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.List;

public class ProgramSolution extends Solution {

    protected Tree tree;

    public List<ProgramSolution> parents;

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
    }

    public ProgramSolution(ProgramSolution s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
    }

    @Override
    public ProgramSolution copy() {
        return new ProgramSolution(this);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Tree getTree() {
        return tree;
    }


}