package org.um.feri.ears.util;

import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmRunStats implements Serializable {

    protected List<ProgramSolution> bestGenSolutions;
    protected List<ProgramSolution> bestGenSolutionsConvergenceGraph;
    protected List<ProgramSolution> bestGenSolutionsMasterTournament;

    protected ArrayList<Double> bestOverallFitnesses;
    protected ArrayList<Double> avgGenFitnesses;
    protected ArrayList<Double> avgGenTreeDepths;
    protected ArrayList<Double> avgGenTreeSizes;
    protected ArrayList<Double> bestGenFitnesses;

    public GPAlgorithmRunStats() {
        bestOverallFitnesses = new ArrayList<>();
        avgGenFitnesses = new ArrayList<>();
        avgGenTreeDepths = new ArrayList<>();
        avgGenTreeSizes = new ArrayList<>();
        bestGenFitnesses = new ArrayList<>();
    }

    public GPAlgorithmRunStats(List<ProgramSolution> bestGenSolutions, List<ProgramSolution> bestGenSolutionsConvergenceGraph, List<ProgramSolution> bestGenSolutionsMasterTournament, ArrayList<Double> bestOverallFitnesses, ArrayList<Double> avgGenFitnesses, ArrayList<Double> avgGenTreeDepths, ArrayList<Double> avgGenTreeSizes, ArrayList<Double> bestGenFitnesses) {
        this.bestGenSolutions = bestGenSolutions;
        this.bestGenSolutionsConvergenceGraph = bestGenSolutionsConvergenceGraph;
        this.bestGenSolutionsMasterTournament = bestGenSolutionsMasterTournament;
        this.bestOverallFitnesses = bestOverallFitnesses;
        this.avgGenFitnesses = avgGenFitnesses;
        this.avgGenTreeDepths = avgGenTreeDepths;
        this.avgGenTreeSizes = avgGenTreeSizes;
        this.bestGenFitnesses = bestGenFitnesses;
    }

    /**
     * Returns the best fitness of the whole run.
     */
    public Double getBestOverallFitness() {
        return bestOverallFitnesses.get(bestOverallFitnesses.size() - 1);
    }

    /**
     * Returns the best avg fitness of all generations.
     */
    public Double getBestAvgGenFitness() {
        return avgGenFitnesses.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
    }

    public ArrayList<Double> getBestOverallFitnesses() {
        return bestOverallFitnesses;
    }

    public void setBestOverallFitnesses(ArrayList<Double> bestOverallFitnesses) {
        this.bestOverallFitnesses = bestOverallFitnesses;
    }

    public ArrayList<Double> getAvgGenFitnesses() {
        return avgGenFitnesses;
    }

    public void setAvgGenFitnesses(ArrayList<Double> avgGenFitnesses) {
        this.avgGenFitnesses = avgGenFitnesses;
    }

    public ArrayList<Double> getAvgGenTreeDepths() {
        return avgGenTreeDepths;
    }

    public void setAvgGenTreeDepths(ArrayList<Double> avgGenTreeDepths) {
        this.avgGenTreeDepths = avgGenTreeDepths;
    }

    public ArrayList<Double> getAvgGenTreeSizes() {
        return avgGenTreeSizes;
    }

    public void setAvgGenTreeSizes(ArrayList<Double> avgGenTreeSizes) {
        this.avgGenTreeSizes = avgGenTreeSizes;
    }

    public ArrayList<Double> getBestGenFitnesses() {
        return bestGenFitnesses;
    }

    public void setBestGenFitnesses(ArrayList<Double> bestGenFitnesses) {
        this.bestGenFitnesses = bestGenFitnesses;
    }

    public static ArrayList<Double> getBestRunFitnesses(ArrayList<GPAlgorithmRunStats> gpAlgorithmRunStats){
        ArrayList<Double> bestRunFitnesses = new ArrayList<>();
        for (GPAlgorithmRunStats stats: gpAlgorithmRunStats) {
            bestRunFitnesses.add(stats.getBestOverallFitness());
        }

        return bestRunFitnesses;
    }

    public static ArrayList<Double> getBestRunAvgFitnesses(ArrayList<GPAlgorithmRunStats> gpAlgorithmRunStats){
        ArrayList<Double> bestRunAvgFitnesses = new ArrayList<>();
        for (GPAlgorithmRunStats stats: gpAlgorithmRunStats) {
            bestRunAvgFitnesses.add(stats.getBestAvgGenFitness());
        }

        return bestRunAvgFitnesses;
    }

    public ProgramSolution getBestRunSolution() {
        return bestGenSolutions.get(bestGenSolutions.size() - 1);
    }

    public ProgramSolution getBestRunSolution(int gen) {
        if(gen < 0 || gen >= bestGenSolutions.size()){
            return null;
        }

        return bestGenSolutions.get(gen);
    }

    public int getBestGenSolutionSize() {
        return bestGenSolutions.size();
    }
}
