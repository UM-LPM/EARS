package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinalIndividualFitness implements Serializable {
    public int individualID;
    public double value;

    public List<IndividualMatchResult> individualMatchResults;
    public HashMap<String, Double> additionalValues;

    public FinalIndividualFitness(){
        this.individualID = -1;
        this.value = 0;
        this.individualMatchResults = new ArrayList<>();
        this.additionalValues = new HashMap<>();
    }

    public FinalIndividualFitness(FinalIndividualFitness fitness){
        this.individualID = fitness.individualID;
        this.value = fitness.value;

        this.individualMatchResults = new ArrayList<>();
        for (IndividualMatchResult imr : fitness.individualMatchResults) {
            this.individualMatchResults.add(new IndividualMatchResult(imr));
        }

        this.additionalValues = new HashMap<>();
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
}
