package org.um.feri.ears.util;

import org.um.feri.ears.individual.representations.gp.Target;

import java.io.Serializable;

public class EARSConfiguration implements Serializable {
    public GPAlgorithmType AlgorithmType;
    public GPProblemType ProblemType;
    public String ProblemName;
    public int FitnessEvaluations;
    public int Generations;

    public int PopSize;
    public float CrossoverProb;
    public float MutationProb;
    public int NumOfTournaments;
    public int MinTreeDepth;
    public int MaxTreeStartDepth;
    public int MaxTreeEndDepth;
    public int MaxTreeSize;
    public float ElitismProb;
    public int SeqSelNumOfChildren;
    public String InitPopGeneratorMethod;
    public String Functions;
    public String Terminals;
    public String[] FeasibilityControlOperators;
    public String[] BloatControlOperators;
    public Target[] EvalData;
    public GPProblemEvaluatorType ProblemEvaluatorType;
    public LastEvalIndividualFitnessesRatingCompositionType LastEvalIndividualFitnessesRatingCompositionType;
}
