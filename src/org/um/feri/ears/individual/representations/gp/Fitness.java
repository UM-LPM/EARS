package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.HashMap;

public class Fitness implements Serializable {
    public float value;
    public HashMap<String, Float> individualValues;


    public Fitness() {
        this.value = 0f;
        this.individualValues = new HashMap<>();
    }

    public Fitness(float startValue) {
        this.value = startValue;
        this.individualValues = new HashMap<>();
    }

    public float GetFitness() {
        return value;
    }

    public HashMap<String, Float> GetIndividualFitnessValues() {
        return individualValues;
    }
}
