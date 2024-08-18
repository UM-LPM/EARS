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

    public List<ProgramSolution> parents;

    public HashMap<String, Fitness> Fitnesses; // Detailed fitness values for analysis

    public int[] NodeCallFrequencyCount;

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
        this.Fitnesses = new HashMap<>();
    }

    public ProgramSolution(ProgramSolution s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
        Fitnesses = new HashMap<>();
        Fitnesses.putAll(s.Fitnesses);
        NodeCallFrequencyCount = s.NodeCallFrequencyCount.clone();
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
}