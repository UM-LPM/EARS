package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.HashMap;

public class Fitness implements Serializable {
    public float Value;
    public HashMap<String, Float> IndividualValues;


    public Fitness() {
        this.Value = 0f;
        this.IndividualValues = new HashMap<>();
    }

    public Fitness(float startValue) {
        this.Value = startValue;
        this.IndividualValues = new HashMap<>();
    }

    public float GetFitness() {
        return Value;
    }

    public HashMap<String, Float> GetIndividualFitnessValues() {
        return IndividualValues;
    }
}
