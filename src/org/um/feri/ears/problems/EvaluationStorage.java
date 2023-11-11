package org.um.feri.ears.problems;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.util.Util;

import java.io.Serializable;

public class EvaluationStorage {

    public EvaluationStorage(){}

    public EvaluationStorage(String randomGenerator, long seed, NumberAlgorithm algorithm, DoubleProblem problem, int resolution, int numberOfRuns, int evaluationsPerRun) {
        this.randomGenerator = randomGenerator;
        this.seed = seed;
        algorithmName = algorithm.getAlgorithmInfo().getAcronym();
        algorithmVersion = algorithm.getVersion();
        algorithmParameters = algorithm.getParametersAsString();
        problemName = problem.getName();
        problemVersion = problem.getVersion();
        problemDimensions = problem.getNumberOfDimensions();
        upperLimit = Util.toDoubleArray(problem.upperLimit);
        lowerLimit = Util.toDoubleArray(problem.lowerLimit);
        this.resolution = resolution;
        this.numberOfRuns = numberOfRuns;
        this.evaluationsPerRun = evaluationsPerRun;
        evaluations = new Evaluation[numberOfRuns][evaluationsPerRun];
    }

    public String randomGenerator;
    public long seed;
    public String algorithmName;
    public String algorithmVersion;
    public String algorithmParameters;
    public String problemName;
    public String problemVersion;
    public int problemDimensions;
    public double[] upperLimit;
    public double[] lowerLimit;
    public int resolution; //store every nth evaluation
    public int numberOfRuns;
    public int evaluationsPerRun;
    public Evaluation[][] evaluations;

    public static class Evaluation implements Serializable {

        public Evaluation() {}

        public Evaluation(int evalNum, int iteration, long time, double fitness) {
            this.evalNum = evalNum;
            this.iteration = iteration;
            this.time = time;
            this.fitness = fitness;
        }

        public int evalNum;
        public int iteration;
        public long time; // nano time
        public double fitness;
    }
}