package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinalIndividualFitness implements Serializable {
    public int individualID;
    public double value;

    public List<IndividualMatchResult> individualMatchResults;
    public IndividualMatchResult avgMatchResult;
    public HashMap<String, Double> additionalValues;

    public FinalIndividualFitness(){
        this.individualID = -1;
        this.value = 0;
        this.individualMatchResults = new ArrayList<>();
        this.additionalValues = new HashMap<>();
    }

    public FinalIndividualFitness(FinalIndividualFitness fitness, boolean setIndividualMatchResults){
        this.individualID = fitness.individualID;
        this.value = fitness.value;

        this.individualMatchResults = new ArrayList<>();
        if(setIndividualMatchResults)
            for (IndividualMatchResult imr : fitness.individualMatchResults) {
                this.individualMatchResults.add(new IndividualMatchResult(imr));
            }

        this.avgMatchResult = new IndividualMatchResult(fitness.avgMatchResult);

        this.additionalValues = new HashMap<>();
        if(fitness.additionalValues != null)
            this.additionalValues.putAll(fitness.additionalValues);
    }

    public List<IndividualMatchResult> getIndividualMatchResults() {
        return individualMatchResults;
    }

    public HashMap<String, Double> getAdditionalValues() {
        return additionalValues;
    }

    public Double getAdditionalValue(String key){
        if(additionalValues.containsKey(key))
            return additionalValues.get(key);
        else throw new IllegalArgumentException("Key not found in additionalValues");
    }

    public IndividualMatchResult getAvgMatchResult() {
        return avgMatchResult;
    }

    public void resetAdditionalData(){
        this.additionalValues = null;
    }
}
