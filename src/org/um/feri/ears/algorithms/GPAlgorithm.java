package org.um.feri.ears.algorithms;

import org.um.feri.ears.algorithms.gp.GPAlgorithmStep;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.util.ArrayList;
import java.util.List;

public abstract class GPAlgorithm extends Algorithm<ProgramSolution<Double>, ProgramSolution<Double>, ProgramProblem<Double>> {

    protected ArrayList<Double> bestGenFitness;
    protected ArrayList<Double> avgGenFitness;
    protected ArrayList<Double> avgGenTreeHeight;
    protected ArrayList<Double> avgGenTreeSize;
    public abstract ProgramSolution<Double> executeStep() throws StopCriterionException;

    public abstract ProgramSolution<Double> executeGeneration() throws StopCriterionException;

    public abstract List<ProgramSolution<Double>> getPopulation();

    public abstract ProgramSolution<Double> getBest();

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
