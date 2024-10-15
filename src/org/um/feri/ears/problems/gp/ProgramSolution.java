package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Fitness;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramSolution extends Solution {

    protected Tree tree;

    protected List<ProgramSolution> parents;

    protected HashMap<String, Fitness> Fitnesses; // Detailed fitness values for analysis
    protected double ratingStandardDeviation; // Standard deviation of the rating when rating is used as fitness

    protected int[] NodeCallFrequencyCount;

    protected boolean isDirty; // Flag to indicate if the solution has been modified (GPOperators, etc.)

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
        this.Fitnesses = new HashMap<>();
        this.NodeCallFrequencyCount = new int[]{};
        this.isDirty = false;
    }

    public ProgramSolution(ProgramSolution s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
        Fitnesses = new HashMap<>();
        Fitnesses.putAll(s.Fitnesses);
        NodeCallFrequencyCount = s.NodeCallFrequencyCount.clone();
        isDirty = s.isDirty;
        ratingStandardDeviation = s.ratingStandardDeviation;
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

    public Map<String, Float> getFitnessesCombined(){
        HashMap<String, Float> fitnessesCombined = new HashMap<>();

        if(Fitnesses == null)
            return fitnessesCombined;

        for (Map.Entry<String, Fitness> entry : Fitnesses.entrySet()) {
            for(Map.Entry<String, Float> entry2 : entry.getValue().GetIndividualFitnessValues().entrySet()){
                if(fitnessesCombined.containsKey(entry2.getKey())){
                    fitnessesCombined.put(entry2.getKey(), fitnessesCombined.get(entry2.getKey()) + entry2.getValue());
                }
                else {
                    fitnessesCombined.put(entry2.getKey(), entry2.getValue());
                }
            }
        }

        return fitnessesCombined;
    }

    public void setNodeCallFrequencyCount(int[] nodeCallFrequencyCount){
        this.NodeCallFrequencyCount = nodeCallFrequencyCount;
    }

    public int[] getNodeCallFrequencyCount(){
        return NodeCallFrequencyCount;
    }

    public void resetIsDirty(){
        isDirty = false;
    }

    public void setIsDirty(boolean isDirty){
        this.isDirty = isDirty;
    }

    public boolean isDirty(){
        return isDirty;
    }

    public void setRatingStandardDeviation(double ratingStandardDeviation) {
        this.ratingStandardDeviation = ratingStandardDeviation;
    }

    public double getRatingStandardDeviation() {
        return ratingStandardDeviation;
    }
}