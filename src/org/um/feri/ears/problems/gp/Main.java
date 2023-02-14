package org.um.feri.ears.problems.gp;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.benchmark.RPUOed30Benchmark;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        //Test TreeNode individual generator
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

        sgp.setMaxTreeHeight(4);
        sgp.setMaxNodeChildrenNum(2); //TODO se vec ne uporablja

        /*ProgramSolution<Double> ps = sgp.getRandomSolution();
        sgp.eval(ps);
        System.out.println("Fitness: " + ps.getEval());
        System.out.println("AncestorCount: " + ps.getSolution().ancestors().getAncestorCount());
        ps.getSolution().displayTree("TestBTree");*/

        //Algorithm execution
        //ps.setEval(Double.MAX_VALUE);

        Task<ProgramSolution<Double>, ProgramProblem<Double>> symbolicRegression = new Task<>(sgp, StopCriterion.EVALUATIONS, 5000, 0, 0);

        //GPAlgorithm alg = new DefaultGPAlgorithm();
        //RandomWalkGPAlgorithm alg = new RandomWalkGPAlgorithm();
                /*try {
            ProgramSolution<Double> sol = alg.execute(symbolicRegression);
            System.out.println("Best fitness: " + sol.getEval());
            sol.getProgram().displayTree("TestBTree");
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }*/

        ArrayList<GPAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new RandomWalkGPAlgorithm("1"));
        algorithms.add(new RandomWalkGPAlgorithm("2"));

        SymbolicRegressionBenchmark benchmark = new SymbolicRegressionBenchmark();

        benchmark.addAlgorithms(algorithms);  // register the algorithms in the benchmark

        benchmark.run(5); //start the tournament with 10 runs/repetitions




        /*final Op<Double> myop = MathOp.ADD;

        System.out.println(myop.apply(new Double[]{5.0, 6.0}));
        System.out.println(myop.type());*/

        /*TreeNode<Double> root = new TreeNode<>();
        root.operation = MathOp.ADD;

        TreeNode<Double> left = new TreeNode<>();
        left.coefficient = 4.0;
        left.operation = Op.define(left.coefficient.toString(), 0, v-> left.coefficient);

        TreeNode<Double> right = new TreeNode<>();
        right.operation = Op.define("x", -1, v -> null);
        //right.operation = MathOp.PI;
        //right.coeficient = 6.0;
        //right.operation = Op.define(left.coeficient.toString(), 0, v-> right.coeficient);

        root.insert(0, left);
        root.insert(1, right);

        ProgramSolution ps = new ProgramSolution();
        ps.setSolution(root);
        System.out.println("Fitness: " + sgp.eval(ps));

        ps.getSolution().displayTree("TestBTree");*/
    }
}