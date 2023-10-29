package org.um.feri.ears.algorithms;

import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.ProgramSolution2;

import java.util.ArrayList;
import java.util.List;

public abstract class GPAlgorithm2 extends Algorithm<ProgramSolution2, ProgramSolution2, ProgramProblem2> {

    protected ArrayList<Double> bestGenFitness;
    protected ArrayList<Double> avgGenFitness;
    protected ArrayList<Double> avgGenTreeHeight;
    protected ArrayList<Double> avgGenTreeSize;
    public abstract ProgramSolution2 executeStep() throws StopCriterionException;

    public abstract ProgramSolution2 executeGeneration() throws StopCriterionException;

    public abstract List<ProgramSolution2> getPopulation();

    public abstract ProgramSolution2 getBest();

    public ArrayList<Double> getBestGenFitness() {
        return bestGenFitness;
    }

    public void setBestGenFitness(ArrayList<Double> bestGenFitness) {
        this.bestGenFitness = bestGenFitness;
    }

    public ArrayList<Double> getAvgGenFitness() {
        return avgGenFitness;
    }

    public void setAvgGenFitness(ArrayList<Double> avgGenFitness) {
        this.avgGenFitness = avgGenFitness;
    }

    public ArrayList<Double> getAvgGenTreeHeight() {
        return avgGenTreeHeight;
    }

    public ArrayList<Double> getAvgGenTreeSize() {
        return avgGenTreeSize;
    }

}
