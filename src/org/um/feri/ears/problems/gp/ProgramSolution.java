package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.FinalIndividualFitness;
import org.um.feri.ears.individual.representations.gp.IndividualMatchResult;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramSolution extends Solution {

    protected Tree tree;

    protected List<ProgramSolution> parents;

    protected FinalIndividualFitness Fitness;

    protected int[] NodeCallFrequencyCount; // TODO Remove this when moved to FinalIndividualFitness

    protected boolean isDirty; // Flag to indicate if the solution has been modified (GPOperators, etc.)

    protected int changesCount; // Number of changes made to the solution

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
        this.Fitness = new FinalIndividualFitness();
        this.NodeCallFrequencyCount = new int[]{};
        this.isDirty = false;
        this.changesCount = 0;
    }

    public ProgramSolution(ProgramSolution s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
        Fitness = new FinalIndividualFitness(s.Fitness);
        NodeCallFrequencyCount = s.NodeCallFrequencyCount.clone(); // TODO Remove this when moved to FinalIndividualFitness
        isDirty = s.isDirty;
        changesCount = s.changesCount;
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

    public FinalIndividualFitness getFitness(){
        return Fitness;
    }

    public void setFitness(FinalIndividualFitness fitness){
        this.Fitness = fitness;
    }

    public Map<String, Double> getFitnessesCombined(){
        HashMap<String, Double> fitnessesCombined = new HashMap<>();

        for(IndividualMatchResult individualMatchResult : Fitness.getIndividualMatchResults()){
            for(Map.Entry<String, Double> entry : individualMatchResult.individualValues.entrySet()){
                if(fitnessesCombined.containsKey(entry.getKey())){
                    fitnessesCombined.put(entry.getKey(), fitnessesCombined.get(entry.getKey()) + entry.getValue());
                }
                else {
                    fitnessesCombined.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return fitnessesCombined;
    }

    // TODO Remove this (when moved to FinalIndividualFitness)
    public void setNodeCallFrequencyCount(int[] nodeCallFrequencyCount){
        this.NodeCallFrequencyCount = nodeCallFrequencyCount;
    }

    // TODO Remove this (when moved to FinalIndividualFitness)
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

    public void increaseChangesCount(){
        changesCount++;
    }

    public int getChangesCount(){
        return changesCount;
    }
}