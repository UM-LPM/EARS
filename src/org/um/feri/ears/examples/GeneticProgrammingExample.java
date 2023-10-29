package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.RandomWalkGPAlgorithm;
import org.um.feri.ears.individual.generations.gp.GPRandomProgramSolution;
import org.um.feri.ears.individual.representations.gp.MathOp;
import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.OperationType;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.operators.gp.GPDepthBasedTreePruningOperator;
import org.um.feri.ears.operators.gp.GPTreeExpansionOperator;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.*;
import org.um.feri.ears.util.Util;
import java.io.IOException;
import java.util.ArrayList;

public class GeneticProgrammingExample {
    public static void main(String[] args) throws IOException {

        // y=x^2 + 10x
        SymbolicRegressionProblem sgp = new SymbolicRegressionProblem(
                Utils.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST),
                Utils.list(Op.define("x", OperationType.VARIABLE)),
                3,
                8,
                200,
                new GPDepthBasedTreePruningOperator<>(),
                new GPTreeExpansionOperator<>(),
                Util.list( new Target().when("x", 0).targetIs(0),
                    new Target().when("x", 1).targetIs(11),
                    new Target().when("x", 2).targetIs(24),
                    new Target().when("x", 3).targetIs(39),
                    new Target().when("x", 4).targetIs(56),
                    new Target().when("x", 5).targetIs(75),
                    new Target().when("x", 6).targetIs(96),
                    new Target().when("x", 7).targetIs(119),
                    new Target().when("x", 8).targetIs(144),
                    new Target().when("x", 9).targetIs(171),
                    new Target().when("x", 10).targetIs(200)),
                new GPRandomProgramSolution<>()
        );


        // y=x^3 + 2x^2 + 8x + 12
        SymbolicRegressionProblem sgp2 = new SymbolicRegressionProblem(
                Utils.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST),
                Utils.list(Op.define("x", OperationType.VARIABLE)),
                3,
                8,
                200,
                new GPDepthBasedTreePruningOperator<>(),
                new GPTreeExpansionOperator<>(),
                Util.list( new Target().when("x", 0).targetIs(12),
                    new Target().when("x", 1).targetIs(23),
                    new Target().when("x", 2).targetIs(44),
                    new Target().when("x", 3).targetIs(81),
                    new Target().when("x", 4).targetIs(140),
                    new Target().when("x", 5).targetIs(227),
                    new Target().when("x", 6).targetIs(348),
                    new Target().when("x", 7).targetIs(509),
                    new Target().when("x", 8).targetIs(716),
                    new Target().when("x", 9).targetIs(975),
                    new Target().when("x", 10).targetIs(1292)),
                new GPRandomProgramSolution<>()
        );

        //y=4x^3 -15x^2 +8x - 12
        SymbolicRegressionProblem sgp3 = new SymbolicRegressionProblem(
                Utils.list(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.CONST),
                Utils.list(Op.define("x", OperationType.VARIABLE)),
                3,
                15,
                200,
                new GPDepthBasedTreePruningOperator<>(),
                new GPTreeExpansionOperator<>(),
                Util.list( new Target().when("x", 0).targetIs(-12),
                        new Target().when("x", 1).targetIs(-15),
                        new Target().when("x", 2).targetIs(-4),
                        new Target().when("x", 3).targetIs(27),
                        new Target().when("x", 4).targetIs(100),
                        new Target().when("x", 5).targetIs(203),
                        new Target().when("x", 6).targetIs(340),
                        new Target().when("x", 7).targetIs(515),
                        new Target().when("x", 8).targetIs(723),
                        new Target().when("x", 9).targetIs(995),
                        new Target().when("x", 10).targetIs(1308)),
                new GPRandomProgramSolution<>() // TODO -> this is GROW strategy, change naming
        );

        //GP algorithm execution example
        Task<ProgramSolution<Double>, ProgramProblem<Double>> symbolicRegression = new Task<>(sgp2, StopCriterion.EVALUATIONS, 30000, 0, 0);


        GPAlgorithm alg = new DefaultGPAlgorithm(100, 0.95, 0.025, 2);
        RandomWalkGPAlgorithm rndAlg = new RandomWalkGPAlgorithm();

//        try {
//            GPAlgorithm alg2 = new DefaultGPAlgorithm(100, 0.95, 0.025, 2, symbolicRegression);
//            /*alg2.executeStep();
//            alg2.executeStep();
//            alg2.executeStep();
//            alg2.executeStep();
//            alg2.executeStep();*/
//            /*alg2.executeGeneration();
//            alg2.executeGeneration();
//            alg2.executeGeneration();*/
//
//            for(int i = 0; i < 10; i++){
//                ProgramSolution<Double> solution = null;
//                while(solution == null){
//                    solution = alg2.executeGeneration();
//                }
//                System.out.println("Best fitness (DefaultGpAlgorithm) (for i = " + i + ") -> " + solution.getEval());
//                System.out.println("Number of iterations -> " + symbolicRegression.getNumberOfIterations());
//                alg2.resetToDefaultsBeforeNewRun();
//            }
//            //solution.getProgram().displayTree("TestBTree", true);
//
//        } catch (StopCriterionException e) {
//            e.printStackTrace();
//        }

        /*try {
            long startTime = System.currentTimeMillis();
            System.out.println("Starting DefaultGpAlgorithm");
            ArrayList<ProgramSolution<Double>> solutions = new ArrayList<>();
            ArrayList<Double> solutionsRnd = new ArrayList<>();
            ProgramSolution<Double> sol;
            for (int i = 0; i < 20; i++){
                sol = alg.execute(symbolicRegression);
                solutions.add(sol);
                System.out.println("Best fitness (DefaultGpAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                alg.resetToDefaultsBeforeNewRun();

                sol = rndAlg.execute(symbolicRegression);
                solutionsRnd.add(sol.getEval());
                System.out.println("Best fitness (RandomWalkAlgorithm) (for i = " + i + ") -> " + sol.getEval());
                rndAlg.resetToDefaultsBeforeNewRun();
                alg.resetToDefaultsBeforeNewRun();
            }

            ProgramSolution<Double> maxSol = solutions.get(0);
            double maxRnd = Double.MAX_VALUE;
            int wins = 0;
            for (int i = 0; i < solutions.size(); i++){
                if(solutions.get(i).getEval() < maxSol.getEval())
                    maxSol = solutions.get(i);
                if(solutionsRnd.get(i) < maxRnd)
                    maxRnd = solutionsRnd.get(i);

                if(solutions.get(i).getEval() < solutionsRnd.get(i))
                    wins++;
            }

            System.out.println("=====================================");
            System.out.println("Best fitness (DefaultGpAlgorithm)  -> " + maxSol.getEval());
            System.out.println("Best fitness (RandomWalkAlgorithm) -> " + maxRnd);
            System.out.println("Wins (DefaultGpAlgorithm) -> " + wins);

            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            System.out.println("Elapsed time: " + elapsedTime + " s");
            maxSol.getProgram().displayTree("TestBTree", true);
            //ProgramSolution<Double> sol = alg.execute(symbolicRegression);
            //System.out.println("Best fitness: " + sol.getEval());
            //System.out.println("AncestorCount: " + sol.getProgram().ancestors().getAncestorCount());
            //System.out.println("Tree Depth: " + sol.getProgram().treeHeight());
            //sol.getProgram().displayTree("TestBTree");
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }*/


//        ArrayList<GPAlgorithm> algorithms = new ArrayList<>();
//        algorithms.add(new RandomWalkGPAlgorithm());
//        algorithms.add(new DefaultGPAlgorithm());
//
//        SymbolicRegressionBenchmark benchmark = new SymbolicRegressionBenchmark();
//
//        benchmark.addAlgorithms(algorithms);  // register the algorithms in the benchmark
//
//        benchmark.run(30); //start the tournament with 10 runs/repetitions


//        long startTime = System.currentTimeMillis();
//        ProgramSolution<Double> ps = sgp3.getRandomSolution();
////        for (int i =0; i < 100000; i++){
////            int a = ps.getProgram().treeHeight();
////        }
////        System.out.println("Fitness: " + ps.getEval());
////        System.out.println("AncestorCount: " + ps.getProgram().ancestors().getAncestorCount());
////        System.out.println("Tree Depth: " + ps.getProgram().treeHeight());
////        TreeAncestor<Double> ancestor = ps.getProgram().ancestorAt(5);
////        System.out.println("Ancestor at 5: " + ancestor.getTreeHeightPosition());
////        System.out.println("Ancestor at 5: " + ancestor.getTreeNode().getOperation().name());
//
//        ps.getProgram().displayTree("TestBTree", true);
//        sgp2.makeFeasible(ps);
//        ps.getProgram().displayTree("TestBTree", true);
//        //sgp2.evaluate(ps);
//        long elapsedTime = (System.currentTimeMillis() - startTime) ;
//
//        System.out.println("Elapsed time: " + elapsedTime + " ms");




        /*// SinglePointCrossover example
        Task<ProgramSolution<Double>, ProgramProblem<Double>> symbolicRegressionCross = new Task<>(sgp, StopCriterion.EVALUATIONS, 10000, 0, 0);

        ProgramSolution<Double> ps1 = sgp.getRandomSolution();
        ProgramSolution<Double> ps2 = sgp.getRandomSolution();

        ps1.getProgram().displayTree("TestBTree");
        ps2.getProgram().displayTree("TestBTree");

        SinglePointCrossover<Double> spc = new SinglePointCrossover<>(0.9);
        ProgramSolution[] sol = spc.execute(new ProgramSolution[]{ ps1, ps2}, symbolicRegressionCross.problem);

        sol[0].getProgram().displayTree("TestBTree");
        sol[1].getProgram().displayTree("TestBTree");*/

        //SingleTreeNodeMutation example
        /*Task<ProgramSolution<Double>, ProgramProblem<Double>> symbolicRegressionMut = new Task<>(sgp, StopCriterion.EVALUATIONS, 10000, 0, 0);

        ProgramSolution<Double> ps1 = sgp.getRandomSolution();
        ps1.getProgram().displayTree("TestBTree");

        SingleTreeNodeMutation<Double> stnm = new SingleTreeNodeMutation<>(1.0);
        stnm.execute(ps1, symbolicRegressionMut.problem);

        ps1.getProgram().displayTree("TestBTe");*/

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

        // Test program for prunning
//        TreeNode<Double> root = new TreeNode<>(MathOp.SUB); // -
//
//        root.insert(0, new TreeNode<>(MathOp.ADD)); // +
//        root.insert(1, new TreeNode<>(MathOp.SUB)); // -
//
//        root.childAt(0).insert(0, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//        root.childAt(0).insert(1, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//
//        root.childAt(1).insert(0, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//        root.childAt(1).insert(1, new TreeNode<>(MathOp.ADD)); // x
//
//        root.childAt(1).childAt(1).insert(0, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//        root.childAt(1).childAt(1).insert(1, new TreeNode<>(MathOp.ADD)); // +
//
//        root.childAt(1).childAt(1).childAt(1).insert(0, new TreeNode<>(MathOp.DIV)); // /
//        root.childAt(1).childAt(1).childAt(1).insert(1, new TreeNode<>(MathOp.DIV)); // /
//
//        root.childAt(1).childAt(1).childAt(1).childAt(0).insert(0, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//        root.childAt(1).childAt(1).childAt(1).childAt(0).insert(1, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//
//        root.childAt(1).childAt(1).childAt(1).childAt(1).insert(0, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//        root.childAt(1).childAt(1).childAt(1).childAt(1).insert(1, new TreeNode<>(Op.define("x", OperationType.VARIABLE))); // x
//
//        root.displayTree("TestBTree");
//        ProgramSolution<Double> ps = new ProgramSolution<>(1);
//        ps.setProgram(root);
//
//        sgp2.makeFeasible(ps);
//        ps.getProgram().displayTree("TestBTree");
    }
}