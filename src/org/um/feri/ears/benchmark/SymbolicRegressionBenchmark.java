package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.GPAlgorithm2;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution2;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator2;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator2;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;

import java.util.Arrays;
import java.util.List;

public class SymbolicRegressionBenchmark extends SOBenchmark<ProgramSolution2, ProgramSolution2, ProgramProblem2, GPAlgorithm2> {
    protected int dimension = 1000; //recommended

    public SymbolicRegressionBenchmark() {
        this(1e-7);
    }

    public SymbolicRegressionBenchmark(double draw_limit) {
        super();
        String name = "Benchmark CEC 2010";
        this.drawLimit = draw_limit;
        stopCriterion = StopCriterion.EVALUATIONS;
        maxEvaluations = 10000;
        maxIterations = 0;
    }

    @Override
    protected void addTask(ProgramProblem2 problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {
        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                DivNode.class,
                MulNode.class,
                SubNode.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                VarNode.class
        );

        VarNode.variables = Arrays.asList("x");

        List<Target> evalData = Utils.list( new Target().when("x", 0).targetIs(0),
                new Target().when("x", 1).targetIs(11),
                new Target().when("x", 2).targetIs(24),
                new Target().when("x", 3).targetIs(39),
                new Target().when("x", 4).targetIs(56),
                new Target().when("x", 5).targetIs(75),
                new Target().when("x", 6).targetIs(96),
                new Target().when("x", 7).targetIs(119),
                new Target().when("x", 8).targetIs(144),
                new Target().when("x", 9).targetIs(171),
                new Target().when("x", 10).targetIs(200));

        SymbolicRegressionProblem2 sgp = new SymbolicRegressionProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 2, 8, 200, new GPDepthBasedTreePruningOperator2(),
                new GPTreeExpansionOperator2(), new GPRandomProgramSolution2(), evalData);

        List<Target> evalData2 = Util.list( new Target().when("x", 0).targetIs(12),
                new Target().when("x", 1).targetIs(23),
                new Target().when("x", 2).targetIs(44),
                new Target().when("x", 3).targetIs(81),
                new Target().when("x", 4).targetIs(140),
                new Target().when("x", 5).targetIs(227),
                new Target().when("x", 6).targetIs(348),
                new Target().when("x", 7).targetIs(509),
                new Target().when("x", 8).targetIs(716),
                new Target().when("x", 9).targetIs(975),
                new Target().when("x", 10).targetIs(1292));


        SymbolicRegressionProblem2 sgp2 = new SymbolicRegressionProblem2(baseFunctionNodeTypes, baseTerminalNodeTypes, 2, 8, 200, new GPDepthBasedTreePruningOperator2(),
                new GPTreeExpansionOperator2(), new GPRandomProgramSolution2(), evalData2);


        //addTask(sgp, stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(sgp2, stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }
}