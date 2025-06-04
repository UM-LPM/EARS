package org.um.feri.ears.util;

import org.um.feri.ears.algorithms.gp.RequiredEvalsCalcMethod;
import org.um.feri.ears.individual.representations.gp.Target;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EARSConfiguration implements Serializable {

    public static final long serialVersionUID = -2562246291051823548L;

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
    public int TargetGameObjectCount;
    public int RayHitObjectRayCount;
    public int GridCellContainsObjectGridSizeX;
    public int GridCellContainsObjectGridSizeY;
    public String InitPopGeneratorMethod;
    public String Functions;
    public String Terminals;
    public String[] FeasibilityControlOperators;
    public String[] BloatControlOperators;
    public Target[] EvalData;
    public GPProblemEvaluatorType ProblemEvaluatorType;
    public LastEvalIndividualFitnessesRatingCompositionType LastEvalIndividualFitnessesRatingCompositionType;
    public int HallOfFameSize;
    public boolean BuildMasterTournament;
    public boolean BuildConvergenceGraph;

    public RequiredEvalsCalcMethod RequiredEvalsCalcMethod;
    public HashMap<String, Integer> RequiredEvalsCalcMethodParams;
}
