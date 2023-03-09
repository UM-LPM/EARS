package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.RandomWalkGPAlgorithm;
import org.um.feri.ears.benchmark.SymbolicRegressionBenchmark;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;
import java.io.IOException;
import java.util.ArrayList;

public class GeneticProgrammingExample {
    public static void main(String[] args) throws IOException {

        //Test TreeNode individual generator
        SymbolicRegressionProblem sgp = new SymbolicRegressionProblem();
        sgp.setBaseFunctions(Util.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST));
        sgp.setBaseTerminals(Util.list(Op.define("x", OperationType.VARIABLE)));
        sgp.setEvalData(Util.list( new Target().when("x", 0).targetIs(0),
                new Target().when("x", 1).targetIs(11),
                new Target().when("x", 2).targetIs(24),
                new Target().when("x", 3).targetIs(39),
                new Target().when("x", 4).targetIs(56),
                new Target().when("x", 5).targetIs(75),
                new Target().when("x", 6).targetIs(96)));

        sgp.setMaxTreeHeight(6);
        sgp.setMaxNodeChildrenNum(2); // TODO not in use

        //GP algorithm execution example
        /*Task<ProgramSolution<Double>, ProgramProblem<Double>> symbolicRegression = new Task<>(sgp, StopCriterion.EVALUATIONS, 1000, 0, 0);


        GPAlgorithm alg = new DefaultGPAlgorithm();

        try {
            ProgramSolution<Double> sol = alg.execute(symbolicRegression);
            System.out.println("Best fitness: " + sol.getEval());
            System.out.println("AncestorCount: " + sol.getProgram().ancestors().getAncestorCount());
            System.out.println("Tree Depth: " + sol.getProgram().treeHeight());
            sol.getProgram().displayTree("TestBTree");
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }*/


        ArrayList<GPAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new RandomWalkGPAlgorithm());
        algorithms.add(new DefaultGPAlgorithm());

        SymbolicRegressionBenchmark benchmark = new SymbolicRegressionBenchmark();

        benchmark.addAlgorithms(algorithms);  // register the algorithms in the benchmark

        benchmark.run(5); //start the tournament with 10 runs/repetitions

        /*ProgramSolution<Double> ps = sgp.getRandomSolution();
        sgp.eval(ps);
        System.out.println("Fitness: " + ps.getEval());
        System.out.println("AncestorCount: " + ps.getProgram().ancestors().getAncestorCount());
        System.out.println("TestNode: " + ps.getProgram().ancestorAt(5).getOperation().name());
        ps.getProgram().displayTree("TestBTree");*/


        /*
        // SinglePointCrossover example
        Task<Double> symbolicRegression = new Task<>(sgp, StopCriterion.EVALUATIONS, 100, 0, 0);

        ProgramSolution<Double> ps1 = sgp.getRandomSolution();
        ProgramSolution<Double> ps2 = sgp.getRandomSolution();

        ps1.getProgram().displayTree("TestBTree");
        ps2.getProgram().displayTree("TestBTree");

        SinglePointCrossover<Double> spc = new SinglePointCrossover<>();
        ProgramSolution[] sol = spc.execute(new ProgramSolution[]{ ps1, ps2}, symbolicRegression);

        sol[0].getProgram().displayTree("TestBTree");
        sol[1].getProgram().displayTree("TestBTree");*/

        //SingleTreeNodeMutation example
        /*Task<Double> symbolicRegression = new Task<>(sgp, StopCriterion.EVALUATIONS, 100, 0, 0);

        ProgramSolution<Double> ps1 = sgp.getRandomSolution();
        ps1.getProgram().displayTree("TestBTree");

        SingleTreeNodeMutation<Double> stnm = new SingleTreeNodeMutation<>(0.4);
        stnm.execute(ps1, symbolicRegression);

        ps1.getProgram().displayTree("TestBTree");*/

        /*
        // Tree generation example
        TreeNode<Double> root = new TreeNode<>();
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

        /*
        // Tree copy example
        ProgramSolution<Double> sol1 = sgp.getRandomSolution();
        ProgramSolution<Double> sol2 = new ProgramSolution<>(sol1);

        TreeNode<Double> childNode = new TreeNode<>(MathOp.PI.apply(null));
        sol2.getProgram().childAt(0).setOperation(Op.define(childNode.getCoefficient().toString(), OperationType.TERMINAL, 0, v -> childNode.getCoefficient()));

        sol1.getProgram().displayTree("TestBtTree");
        sol2.getProgram().displayTree("TestBtTree");*/

    }
}