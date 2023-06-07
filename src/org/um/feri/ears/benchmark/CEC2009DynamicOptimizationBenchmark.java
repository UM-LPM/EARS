package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.dynamic.cec2009.BasicFunction;
import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;
import org.um.feri.ears.problems.dynamic.cec2009.DynamicCompositionProblem;
import org.um.feri.ears.problems.dynamic.cec2009.DynamicProblem;

import java.util.ArrayList;

/**
 * Generalized Dynamic Benchmark Generator
 * <p>
 * Original source code:
 * - https://github.com/P-N-Suganthan/CEC2009-Dynamic-Optimization
 * <p>
 * Two different instances of generators:
 * - Dynamic Composition Benchmark Generator for continuous problems
 * - Dynamic Rotation Benchmark Generator for continuous problems
 * <p>
 * Six types of changes of control parameters in GDBG:
 * - small step change
 * - large step change
 * - random change
 * - chaotic change
 * - recurrent change
 * - recurrent change with noisy
 * <p>
 * Two types of environmental changes:
 * - dimensional changes
 * - non-dimensional changes
 */

public class CEC2009DynamicOptimizationBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    private final int numberOfPeaksOrFunctions;
    private final Double heightNormalizeSeverity;   // the constant number for normalizing all basic functions with similar height
    private final Double minHeight, maxHeight;  // peak height
    private final Double chaoticConstant;    // value must be between 1.0 and 4.0
    private final int minDimension, maxDimension;
    private final int changeFrequency;  // number of evaluations between two successive changes
    private final int numberOfChanges;  // the number of changes = how many times the problem will change
    private final Double gLowerLimit, gUpperLimit;    // search range // TODO: nisem prepričani kaj je to; spodnja in zgornja meja dinamičnega problema "kot celota"?

    public CEC2009DynamicOptimizationBenchmark() {
        super();
        name = "Benchmark CEC 2009 Dynamic Optimization";
        dimension = 10;
        numberOfPeaksOrFunctions = 10;
        heightNormalizeSeverity = 2000.0;
        minHeight = 10.0;
        maxHeight = 100.0;
        chaoticConstant = 3.67;
        minDimension = 5;
        maxDimension = 15;
        changeFrequency = 10000;
        numberOfChanges = 60;
        gLowerLimit = -5.0;
        gUpperLimit = 5.0;
    }

    @Override
    public void initAllProblems() {
        ArrayList<DynamicProblem> problems = new ArrayList<>();

        /*
            TODO:
                DynamicCompositionProblem
                    - problems for all basic functions (Sphere, Rastrigin, Griewank ...) - first for loop in C++
                    - problems for all change types (small step, large step, u_random ...) - first inner loop in C++
                    - two problems with different number of peaks (10 and 50) for Sphere - second inner loop in C++
         */
        problems.add(new DynamicCompositionProblem("DynamicCompositionProblemSphereSmallStepPeaks10",
                dimension, // numberOfDimensions
                -1, // TODO: numberOfGlobalOptimums - ali vem to vnaprej pri dinamičnem problemu? je to numberOfPeaksOrFunctions?
                -1, // TODO: numberOfObjectives
                -1, // TODO: numberOfConstraints
                numberOfPeaksOrFunctions, // numberOfPeaksOrFunctions
                minHeight, maxHeight,
                chaoticConstant,
                ChangeType.SMALL_STEP,
                0,  // periodicity
                false,  // isDimensionChanged
                minDimension, maxDimension,
                changeFrequency,
                5,   // heightSeverity
                heightNormalizeSeverity,
                gLowerLimit, gUpperLimit,
                BasicFunction.SPHERE
        ));
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }
}
