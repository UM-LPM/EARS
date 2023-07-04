package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.dynamic.cec2009.BasicFunction;
import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;

/**
 * Generalized Dynamic Benchmark Generator (GDBG)
 * <p></p>
 * Two different instances of generators:
 * <ul>
 * <li>Dynamic Rotation Benchmark Generator for continuous problems</li>
 * <li>Dynamic Composition Benchmark Generator for continuous problems</li>
 * </ul>
 * Six types of changes of control parameters in GDBG:
 * <ul>
 * <li>small step change</li>
 * <li>large step change</li>
 * <li>random change</li>
 * <li>chaotic change</li>
 * <li>recurrent change</li>
 * <li>recurrent change with noisy</li>
 * </ul>
 * Two types of environmental changes:
 * <ul>
 * <li>dimensional changes</li>
 * <li>non-dimensional changes</li>
 * </ul>
 * Original source code (C++):
 * <a href="https://github.com/P-N-Suganthan/CEC2009-Dynamic-Optimization">https://github.com/P-N-Suganthan/CEC2009-Dynamic-Optimization</a>
 */
public class CEC2009DynamicBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    public static MyRandom myRandom = new MyRandom();

    private final int numberOfPeaksOrFunctions = 10;
    /**
     * The constant number for normalizing all basic functions with similar height.
     */
    private final double heightNormalizeSeverity = 2000.0;
    private final double minPeakHeight = 10.0, maxPeakHeight = 100.0;
    private final double minPeakWidth = 1.0, maxPeakWidth = 10.0;
    /**
     * Value must be between 1.0 and 4.0.
     */
    private final double chaoticConstant = 3.67;
    private final int minDimensions = 5, maxDimensions = 15;
    /**
     * Number of evaluations between two successive changes.
     */
    private final int changeFrequency = 10000;
    /**
     * How many times the problem will change.
     */
    private final int numberOfChanges = 60;
    /**
     * Search range.
     */
    private final double gLowerLimit = -5.0, gUpperLimit = 5.0;
    private final int changeFrequencyPerDimension = 10000;

    public CEC2009DynamicBenchmark() {
        super();
        name = "Benchmark CEC 2009 Dynamic Optimization";
        dimension = 10;
    }

    @Override
    public void initAllProblems() {
        addRotationProblem(ChangeType.SMALL_STEP, 10, 0, false, "DOPRotationSmallStepPeaks10");
        addRotationProblem(ChangeType.SMALL_STEP, 50, 0, false, "DOPRotationSmallStepPeaks50");
        addRotationProblem(ChangeType.LARGE_STEP, 10, 0, false, "DOPRotationLargeStepPeaks10");
        addRotationProblem(ChangeType.LARGE_STEP, 50, 0, false, "DOPRotationLargeStepPeaks50");
        addRotationProblem(ChangeType.U_RANDOM, 10, 0, false, "DOPRotationRandomPeaks10");
        addRotationProblem(ChangeType.U_RANDOM, 50, 0, false, "DOPRotationRandomPeaks50");
        addRotationProblem(ChangeType.RECURRENT, 10, 12, false, "DOPRotationRecurrentPeaks10");
        addRotationProblem(ChangeType.RECURRENT, 50, 12, false, "DOPRotationRecurrentPeaks50");
        addRotationProblem(ChangeType.RECURRENT_NOISY, 10, 12, false, "DOPRotationRecurrentNoisyPeaks10");
        addRotationProblem(ChangeType.RECURRENT_NOISY, 50, 12, false, "DOPRotationRecurrentNoisyPeaks50");
        addRotationProblem(ChangeType.CHAOTIC, 10, 0, false, "DOPRotationChaoticPeaks10");
        addRotationProblem(ChangeType.CHAOTIC, 50, 0, false, "DOPRotationChaoticPeaks50");
        addRotationProblem(ChangeType.U_RANDOM, 10, 0, true, "DOPRotationRandomPeaks10DimensionChanging");
        addRotationProblem(ChangeType.U_RANDOM, 50, 0, true, "DOPRotationRandomPeaks10DimensionChanging");

        addCompositionProblem(BasicFunction.SPHERE, ChangeType.SMALL_STEP, 0, false, "DOPCompositionSphereSmallStepPeaks10");
        addCompositionProblem(BasicFunction.SPHERE, ChangeType.LARGE_STEP, 0, false, "DOPCompositionSphereLargeStepPeaks10");
        addCompositionProblem(BasicFunction.SPHERE, ChangeType.U_RANDOM, 0, false, "DOPCompositionSphereRandomPeaks10");
        addCompositionProblem(BasicFunction.SPHERE, ChangeType.RECURRENT, 12, false, "DOPCompositionSphereRecurrentPeaks10");
        addCompositionProblem(BasicFunction.SPHERE, ChangeType.RECURRENT_NOISY, 12, false, "DOPCompositionSphereRecurrentNoisyPeaks10");
        addCompositionProblem(BasicFunction.SPHERE, ChangeType.CHAOTIC, 0, false, "DOPCompositionSphereChaoticPeaks10");
        addCompositionProblem(BasicFunction.SPHERE, ChangeType.U_RANDOM, 0, true, "DOPCompositionSphereRandomPeaks10DimensionChanging");

        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.SMALL_STEP, 0, false, "DOPCompositionRastriginSmallStepPeaks10");
        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.LARGE_STEP, 0, false, "DOPCompositionRastriginLargeStepPeaks10");
        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.U_RANDOM, 0, false, "DOPCompositionRastriginRandomPeaks10");
        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.RECURRENT, 12, false, "DOPCompositionRastriginRecurrentPeaks10");
        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.RECURRENT_NOISY, 12, false, "DOPCompositionRastriginRecurrentNoisyPeaks10");
        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.CHAOTIC, 0, false, "DOPCompositionRastriginChaoticPeaks10");
        addCompositionProblem(BasicFunction.RASTRIGIN, ChangeType.U_RANDOM, 0, true, "DOPCompositionRastriginRandomPeaks10DimensionChanging");

        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.SMALL_STEP, 0, false, "DOPCompositionGriewankSmallStepPeaks10");
        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.LARGE_STEP, 0, false, "DOPCompositionGriewankLargeStepPeaks10");
        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.U_RANDOM, 0, false, "DOPCompositionGriewankRandomPeaks10");
        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.RECURRENT, 12, false, "DOPCompositionGriewankRecurrentPeaks10");
        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.RECURRENT_NOISY, 12, false, "DOPCompositionGriewankRecurrentNoisyPeaks10");
        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.CHAOTIC, 0, false, "DOPCompositionGriewankChaoticPeaks10");
        addCompositionProblem(BasicFunction.GRIEWANK, ChangeType.U_RANDOM, 0, true, "DOPCompositionGriewankRandomPeaks10DimensionChanging");

        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.SMALL_STEP, 0, false, "DOPCompositionAckleySmallStepPeaks10");
        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.LARGE_STEP, 0, false, "DOPCompositionAckleyLargeStepPeaks10");
        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.U_RANDOM, 0, false, "DOPCompositionAckleyRandomPeaks10");
        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.RECURRENT, 12, false, "DOPCompositionAckleyRecurrentPeaks10");
        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.RECURRENT_NOISY, 12, false, "DOPCompositionAckleyRecurrentNoisyPeaks10");
        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.CHAOTIC, 0, false, "DOPCompositionAckleyChaoticPeaks10");
        addCompositionProblem(BasicFunction.ACKLEY, ChangeType.U_RANDOM, 0, true, "DOPCompositionAckleyRandomPeaks10DimensionChanging");

        addCompositionProblem(BasicFunction.MIXED, ChangeType.SMALL_STEP, 0, false, "DOPCompositionMixedSmallStepPeaks10");
        addCompositionProblem(BasicFunction.MIXED, ChangeType.LARGE_STEP, 0, false, "DOPCompositionMixedLargeStepPeaks10");
        addCompositionProblem(BasicFunction.MIXED, ChangeType.U_RANDOM, 0, false, "DOPCompositionMixedRandomPeaks10");
        addCompositionProblem(BasicFunction.MIXED, ChangeType.RECURRENT, 12, false, "DOPCompositionMixedRecurrentPeaks10");
        addCompositionProblem(BasicFunction.MIXED, ChangeType.RECURRENT_NOISY, 12, false, "DOPCompositionMixedRecurrentNoisyPeaks10");
        addCompositionProblem(BasicFunction.MIXED, ChangeType.CHAOTIC, 0, false, "DOPCompositionMixedChaoticPeaks10");
        addCompositionProblem(BasicFunction.MIXED, ChangeType.U_RANDOM, 0, true, "DOPCompositionMixedRandomPeaks10DimensionChanging");
    }

    private void addRotationProblem(ChangeType changeType, int numberOfPeaksOrFunctions, int periodicity, boolean dimensionChanging, String problemName) {
        DynamicRotationProblem problem = new DynamicRotationProblem(
                problemName,
                dimension, // numberOfDimensions
                1, // numberOfGlobalOptima
                1, // numberOfObjectives
                0, // numberOfConstraints
                numberOfPeaksOrFunctions,
                minPeakHeight, maxPeakHeight,
                chaoticConstant,
                changeType,
                periodicity,
                dimensionChanging,
                minDimensions, maxDimensions,
                minPeakWidth, maxPeakWidth,
                changeFrequency,
                5,   // heightSeverity
                gLowerLimit, gUpperLimit,
                0.5 // widthSeverity
        );
        int maxEvaluations = calculateNumberOfEvaluations(problem);
        addTask(problem, stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }

    private void addCompositionProblem(BasicFunction basicFunction, ChangeType changeType, int periodicity, boolean dimensionChanging, String problemName) {
        DynamicCompositionProblem problem = new DynamicCompositionProblem(
                problemName,
                dimension, // numberOfDimensions
                1, // numberOfGlobalOptima
                1, // numberOfObjectives
                0, // numberOfConstraints
                numberOfPeaksOrFunctions,
                minPeakHeight, maxPeakHeight,
                chaoticConstant,
                changeType,
                periodicity,
                dimensionChanging,
                minDimensions, maxDimensions,
                changeFrequency,
                5,   // heightSeverity
                heightNormalizeSeverity,
                gLowerLimit, gUpperLimit,
                basicFunction
        );
        int maxEvaluations = calculateNumberOfEvaluations(problem);
        addTask(problem, stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }

    private int calculateNumberOfEvaluations(DynamicProblem p) {
        int maxEvaluations = 0;

        if (p.isDimensionChanging()) {
            boolean isDimensionIncreasing = p.isDimensionIncreasing();
            int currentNumberOfDimensions = p.getNumberOfDimensions();

            for (int i = 0; i < numberOfChanges; i++) {
                maxEvaluations += (p.getChangeFrequency() * currentNumberOfDimensions);

                if (currentNumberOfDimensions == p.getMinDimensions()) {
                    isDimensionIncreasing = true;
                }
                if (currentNumberOfDimensions == p.getMaxDimensions()) {
                    isDimensionIncreasing = false;
                }
                if (isDimensionIncreasing) {
                    currentNumberOfDimensions++;
                } else {
                    currentNumberOfDimensions--;
                }
            }
        } else {
            maxEvaluations = p.getChangeFrequency() * p.getNumberOfDimensions() * numberOfChanges;
        }

        return maxEvaluations;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new DynamicTask<>(problem, stopCriterion, maxEvaluations, time, maxIterations, changeFrequencyPerDimension));
    }

    public static void main(String[] args) {
        //CEC2009DynamicOptimizationBenchmark benchmark = new CEC2009DynamicOptimizationBenchmark();
        //benchmark.initAllProblems();

        DynamicRotationProblem problem1 = new DynamicRotationProblem("DynamicCompositionProblemSphereSmallStepPeaks10",
                10, // numberOfDimensions
                1, // numberOfGlobalOptima
                1, // numberOfObjectives
                0, // numberOfConstraints
                10,
                10.0, 100.0,
                3.67,
                ChangeType.CHAOTIC,
                0,  // periodicity
                true,  // isDimensionChanged
                5, 15,
                1.0, 10.0,
                10000,
                5,   // heightSeverity
                -5.0, 5.0,
                0.5
        );

        int changeCounter = 0;
        for (int g = 1; g <= problem1.getChangeFrequency() * 60; g++) {
            if (g % problem1.getChangeFrequency() == 0) {
                // generate a solution
                Double[] x = new Double[problem1.getNumberOfDimensions()];
                for (int i = 0; i < problem1.getNumberOfDimensions(); i++) {
                    x[i] = -5.0 + (5.0 - (-5.0)) * myRandom.nextDouble();
                }
                double fit = problem1.eval(x);
                System.out.println(fit);
                problem1.performChange(changeCounter++);

                //if (problem1.isDimensionChanging()) {
                //    problem1.changeDimension(changeCounter);
                //}
            }
        }
    }
}
