package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.constrained.*;

public class RPCOe1Benchmark extends Benchmark {
    public RPCOe1Benchmark() {
        super();
        name = "Solving Real parameter Constrained Optimization with maximum evaluation condition";
        shortName = "RPCOe1";
        info = "Number of tests 4 \n Most dimensions=2\n Compare if difference<=E-10 is tie.";
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {
        addTask(new TLBOBenchmarkFunction1(), stopCriterion, 200000, 0, maxIterations);
        addTask(new TLBOBenchmarkFunction2(), stopCriterion, 200000, 0, maxIterations);
        addTask(new TLBOBenchmarkFunction3(), stopCriterion, 200000, 0, maxIterations);
        addTask(new TLBOBenchmarkFunction4(), stopCriterion, 200000, 0, maxIterations);
        addTask(new TLBOBenchmarkFunction5(), stopCriterion, 200000, 0, maxIterations);
    }
}
