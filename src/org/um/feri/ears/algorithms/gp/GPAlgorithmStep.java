package org.um.feri.ears.algorithms.gp;

public enum GPAlgorithmStep {
    INITIALIZATION,
    ELITISM,
    SELECTION_AND_CROSSOVER,
    //CROSSOVER,
    MUTATION,
    // BLOAT_CONTROL, // TODO Add in the future + other methods
    EVALUATION,
}