package org.um.feri.ears.util;

import java.io.Serializable;
import java.util.ArrayList;

public class GPAlgorithmRunStats implements Serializable {

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

    public GPAlgorithmRunStats(ArrayList<Double> bestOverallFitnesses, ArrayList<Double> avgGenFitnesses, ArrayList<Double> avgGenTreeDepths, ArrayList<Double> avgGenTreeSizes, ArrayList<Double> bestGenFitnesses) {
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
}
