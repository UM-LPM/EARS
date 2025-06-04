package org.um.feri.ears.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.yaml.snakeyaml.util.Tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPAlgorithmRunStats implements Serializable {

    //protected List<ProgramSolution> bestGenSolutions;
    protected List<ImmutablePair<Integer, ProgramSolution>> bestGenSolutions;
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

    public GPAlgorithmRunStats(List<ImmutablePair<Integer, ProgramSolution>> bestGenSolutions, List<ProgramSolution> bestGenSolutionsConvergenceGraph, List<ProgramSolution> bestGenSolutionsMasterTournament, ArrayList<Double> bestOverallFitnesses, ArrayList<Double> avgGenFitnesses, ArrayList<Double> avgGenTreeDepths, ArrayList<Double> avgGenTreeSizes, ArrayList<Double> bestGenFitnesses) {
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

    public ImmutablePair<Integer, ProgramSolution> getBestRunSolution() {
        return bestGenSolutions.get(bestGenSolutions.size() - 1);
    }

    public ImmutablePair<Integer, ProgramSolution> getBestRunSolution(int gen){
        return getBestRunSolution(gen, -1);
    }

    public ImmutablePair<Integer, ProgramSolution> getBestRunSolution(int gen, int evals ) {
        if(bestGenSolutions == null || bestGenSolutions.size() == 0) {
            throw new IllegalArgumentException("No solutions found in the run stats.");
        }
        if(gen == -1 && evals > 0){
            // Find ProgramSolution where evaluations are the closest to the given evals
            int closestIndex = 0;
            int closestDiff = Math.abs(bestGenSolutions.get(closestIndex).left - evals);

            for (int i = 1; i < bestGenSolutions.size(); i++) {
                int diff = Math.abs(bestGenSolutions.get(i).left - evals);
                if (diff <= closestDiff) {
                    closestDiff = diff;
                    closestIndex = i;
                }
            }

            return bestGenSolutions.get(closestIndex);
        }
        else if(gen >= 0 && gen < bestGenSolutions.size()){
            // Return the solution for the given generation
            return bestGenSolutions.get(gen);
        } else {
            throw new IllegalArgumentException("Invalid generation index: " + gen);

        }
    }

    public List<ImmutablePair<Integer, ProgramSolution>> getBestGenSolutions(){
        return bestGenSolutions;
    }

    public int getBestGenSolutionSize() {
        return bestGenSolutions.size();
    }
}
