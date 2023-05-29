package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.individual.representations.gp.MathOp;
import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.OperationType;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;

public class SymbolicRegressionBenchmark extends SOBenchmark<ProgramSolution<Double>, ProgramSolution<Double>, ProgramProblem<Double>, GPAlgorithm> {
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
    protected void addTask(ProgramProblem<Double> problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {

        SymbolicRegressionProblem sgp = new SymbolicRegressionProblem();
        sgp.setBaseFunctions(Utils.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST, MathOp.PI));
        sgp.setBaseTerminals(Utils.list(Op.define("x", OperationType.VARIABLE)));
        sgp.setEvalData(Utils.list( new Target().when("x", 0).targetIs(0),
                new Target().when("x", 1).targetIs(11),
                new Target().when("x", 2).targetIs(24),
                new Target().when("x", 3).targetIs(39),
                new Target().when("x", 4).targetIs(56),
                new Target().when("x", 5).targetIs(75),
                new Target().when("x", 6).targetIs(96),
                new Target().when("x", 7).targetIs(119),
                new Target().when("x", 8).targetIs(144),
                new Target().when("x", 9).targetIs(171),
                new Target().when("x", 10).targetIs(200)));

        sgp.setMaxTreeHeight(8);
        sgp.setMinTreeHeight(2);

        SymbolicRegressionProblem sgp2 = new SymbolicRegressionProblem();
        sgp2.setBaseFunctions(Utils.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST, MathOp.PI));
        sgp2.setBaseTerminals(Utils.list(Op.define("x", OperationType.VARIABLE)));
        sgp2.setEvalData(Util.list( new Target().when("x", 0).targetIs(12),
                new Target().when("x", 1).targetIs(23),
                new Target().when("x", 2).targetIs(44),
                new Target().when("x", 3).targetIs(81),
                new Target().when("x", 4).targetIs(140),
                new Target().when("x", 5).targetIs(227),
                new Target().when("x", 6).targetIs(348),
                new Target().when("x", 7).targetIs(509),
                new Target().when("x", 8).targetIs(716),
                new Target().when("x", 9).targetIs(975),
                new Target().when("x", 10).targetIs(1292)));

        sgp2.setMaxTreeHeight(10);
        sgp2.setMinTreeHeight(2);


        //addTask(sgp, stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(sgp2, stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }
}