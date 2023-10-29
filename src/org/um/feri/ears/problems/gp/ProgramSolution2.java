package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.btdemo.gp.behaviour.Tree;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.List;

public class ProgramSolution2 extends Solution {

    protected Tree tree;

    public List<ProgramSolution2> parents;

    public ProgramSolution2(int numberOfObjectives) {
        super(numberOfObjectives);
    }

    public ProgramSolution2(ProgramSolution2 s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
    }

    @Override
    public ProgramSolution2 copy() {
        return new ProgramSolution2(this);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Tree getTree() {
        return tree;
    }


}