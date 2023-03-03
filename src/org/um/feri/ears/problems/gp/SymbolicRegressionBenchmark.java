package org.um.feri.ears.problems.gp;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2010.*;

public class SymbolicRegressionBenchmark extends Benchmark<ProgramSolution<Double>, ProgramSolution<Double>, ProgramProblem<Double>, GPAlgorithm> {
    protected int dimension = 1000; //recommended

    public SymbolicRegressionBenchmark() {
        this(1e-7);
    }

    public SymbolicRegressionBenchmark(double draw_limit) {
        super();
        String name = "Benchmark CEC 2010";
        this.drawLimit = draw_limit;
        stopCriterion = StopCriterion.EVALUATIONS;
        maxEvaluations = 5000;
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
                new Target().when("x", 6).targetIs(96)));

        sgp.setMaxTreeHeight(6);
        sgp.setMaxNodeChildrenNum(2);


        addTask(sgp, stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }
}