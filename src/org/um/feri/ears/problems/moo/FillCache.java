package org.um.feri.ears.problems.moo;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_STM;
import org.um.feri.ears.algorithms.moo.nsga3.D_NSGAIII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
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
		
        
        /*DoubleMOTask t1 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1());
        DoubleMOTask t2 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem2());
        DoubleMOTask t3 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem3());
        DoubleMOTask t4 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem4());
        DoubleMOTask t5 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem5());
        DoubleMOTask t6 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem6());
        DoubleMOTask t7 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem7());
        DoubleMOTask t8 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem8());
        DoubleMOTask t9 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem9());
        DoubleMOTask t10 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem10());*/
        DoubleMOTask t1 = new DoubleMOTask(new ZDT1(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        DoubleMOTask t2 = new DoubleMOTask(new ZDT2(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        DoubleMOTask t3 = new DoubleMOTask(new ZDT3(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        DoubleMOTask t4 = new DoubleMOTask(new ZDT4(), StopCriterion.EVALUATIONS, 300000, 500, 300);
        DoubleMOTask t5 = new DoubleMOTask(new ZDT6(), StopCriterion.EVALUATIONS, 300000, 500, 300);

        //DoubleMOTask t1 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG1(2));
        /*DoubleMOTask t2 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG2(2));
        DoubleMOTask t3 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG3(2));
        DoubleMOTask t4 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG4(2));
        DoubleMOTask t5 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG5(2));
        DoubleMOTask t6 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG6(2));
        DoubleMOTask t7 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG7(2));
        DoubleMOTask t8 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG8(2));
        DoubleMOTask t9 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new WFG9(2));
        DoubleMOTask t10 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ1(2));
        DoubleMOTask t11 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ2(2));
        DoubleMOTask t12 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ3(2));
        DoubleMOTask t13 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ4(2));
        DoubleMOTask t14 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ5(2));
        DoubleMOTask t15 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ6(2));
        DoubleMOTask t16 = new DoubleMOTask(EnumStopCriterion.EVALUATIONS, 300000, 0.0001, new DTLZ7(2));*/

        ArrayList<DoubleMOTask> tasks = new ArrayList<DoubleMOTask>();
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
            for (DoubleMOTask task : tasks) {
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
