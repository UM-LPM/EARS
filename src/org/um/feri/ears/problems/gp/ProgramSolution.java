package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Fitness;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramSolution extends Solution {

    protected Tree tree;

    public List<ProgramSolution> parents;

    public HashMap<String, Fitness> Fitnesses; // Detailed fitness values for analysis

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

    public HashMap<String, Fitness> getFitnesses(){
        return Fitnesses;
    }

    public void setFitnesses(HashMap<String, Fitness> values){
        this.Fitnesses = values;
    }


}