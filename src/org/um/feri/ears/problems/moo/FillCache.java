package org.um.feri.ears.problems.moo;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_STM;
import org.um.feri.ears.algorithms.moo.nsga3.D_NSGAIII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.zdt.*;
import org.um.feri.ears.util.Cache;

import java.util.ArrayList;

public class FillCache {

    public static void main(String[] args) {

        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        /*players.add(new D_MOEAD());
        players.add(new D_NSGAII());
        players.add(new D_NSGAIII());
        players.add(new D_PESAII());*/


        players.add(new D_GDE3());
        players.add(new D_MOEAD_STM());
        players.add(new D_SPEA2());
        players.add(new D_IBEA());
        players.add(new D_NSGAIII());

        MOAlgorithm.setCaching(Cache.SAVE);
        MOAlgorithm.setRunWithOptimalParameters(true);
		
        
        /*Task<NumberSolution<Double>, DoubleProblem> t1 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1());
        Task<NumberSolution<Double>, DoubleProblem> t2 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem2());
        Task<NumberSolution<Double>, DoubleProblem> t3 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem3());
        Task<NumberSolution<Double>, DoubleProblem> t4 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem4());
        Task<NumberSolution<Double>, DoubleProblem> t5 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem5());
        Task<NumberSolution<Double>, DoubleProblem> t6 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem6());
        Task<NumberSolution<Double>, DoubleProblem> t7 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem7());
        Task<NumberSolution<Double>, DoubleProblem> t8 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem8());
        Task<NumberSolution<Double>, DoubleProblem> t9 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem9());
        Task<NumberSolution<Double>, DoubleProblem> t10 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem10());*/
        Task<NumberSolution<Double>, DoubleProblem> t1 = new Task<>(new ZDT1(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        Task<NumberSolution<Double>,DoubleProblem> t2 = new Task<>(new ZDT2(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        Task<NumberSolution<Double>,DoubleProblem> t3 = new Task<>(new ZDT3(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        Task<NumberSolution<Double>,DoubleProblem> t4 = new Task<>(new ZDT4(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        Task<NumberSolution<Double>,DoubleProblem> t5 = new Task<>(new ZDT6(), StopCriterion.EVALUATIONS, 300000, 500, 300);

        //Task<NumberSolution<Double>, DoubleProblem> t1 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG1(2));
        /*Task<NumberSolution<Double>, DoubleProblem> t2 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG2(2));
        Task<NumberSolution<Double>, DoubleProblem> t3 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG3(2));
        Task<NumberSolution<Double>, DoubleProblem> t4 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG4(2));
        Task<NumberSolution<Double>, DoubleProblem> t5 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG5(2));
        Task<NumberSolution<Double>, DoubleProblem> t6 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG6(2));
        Task<NumberSolution<Double>, DoubleProblem> t7 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG7(2));
        Task<NumberSolution<Double>, DoubleProblem> t8 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG8(2));
        Task<NumberSolution<Double>, DoubleProblem> t9 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG9(2));
        Task<NumberSolution<Double>, DoubleProblem> t10 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ1(2));
        Task<NumberSolution<Double>, DoubleProblem> t11 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ2(2));
        Task<NumberSolution<Double>, DoubleProblem> t12 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ3(2));
        Task<NumberSolution<Double>, DoubleProblem> t13 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ4(2));
        Task<NumberSolution<Double>, DoubleProblem> t14 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ5(2));
        Task<NumberSolution<Double>, DoubleProblem> t15 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ6(2));
        Task<NumberSolution<Double>, DoubleProblem> t16 = new Task<Double>(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ7(2));*/

        ArrayList<Task<NumberSolution<Double>,DoubleProblem>> tasks = new ArrayList<>();
        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);
        tasks.add(t4);
        tasks.add(t5);
//    	tasks.add(t6);
//    	tasks.add(t7);
//    	tasks.add(t8);
//    	tasks.add(t9);
//    	tasks.add(t10);


        for (int i = 0; i < 100; i++) {
            System.out.println("-------------------------------------");
            System.out.println("Run: " + i);
            System.out.println("-------------------------------------");
            for (Task<NumberSolution<Double>,DoubleProblem> task : tasks) {
                System.out.println(task.getProblemName());
                for (MOAlgorithm alg : players) {
                    System.out.println("Alg: " + alg.getAlgorithmInfo().getAcronym());
                    try {
                        alg.execute(task.clone());
                    } catch (StopCriterionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
