package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.HashMap;

public class FitnessIndividual implements Serializable {
    public float finalFitness; // Used to store final fitness (sum of all fitnesses from different game scenarios)
    public float finalFitnessStats; // Used to store final fitness calculated statistic (mean, std deviation, min, max,...)
    public HashMap<String, Fitness> fitnesses;
}
