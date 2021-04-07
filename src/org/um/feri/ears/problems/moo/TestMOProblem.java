package org.um.feri.ears.problems.moo;

import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead.D_MOEAD;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.problems.moo.zdt.ZDT1;
import org.um.feri.ears.qualityIndicator.GenerationalDistance;
import org.um.feri.ears.qualityIndicator.InvertedGenerationalDistance;
import org.um.feri.ears.util.Util;

public class TestMOProblem {
    public static void main(String[] args) {
    	Util.rnd.setSeed(System.currentTimeMillis());
    	
    	ZDT1 zdt = new ZDT1();
    	ParetoSolution filePareto = new ParetoSolution();
    	filePareto.loadObjectivesFromFile("test_pareto//ZDT1_middle.dat");
    	//filePareto.displayData("Uniform",zdt.getName(), zdt);
    	//filePareto.displayAllUnaryQulaityIndicators(zdt);

    	

    	DoubleMOTask t1 = new DoubleMOTask(new UnconstrainedProblem1(), StopCriterion.EVALUATIONS, 300000, 500, 300, 0.0001);
    	DoubleMOTask t2 = new DoubleMOTask(new UnconstrainedProblem1(), StopCriterion.EVALUATIONS, 300000, 500, 300, 0.0001);



    	D_MOEAD moead = new D_MOEAD();
    	D_IBEA ibea = new D_IBEA();
    	moead.setDisplayData(true);
    	ibea.setDisplayData(true);
    	try {
    		ParetoSolution best1 = moead.execute(t1);
    		ParetoSolution best2 = ibea.execute(t2);

    		best1.evaluate(new GenerationalDistance(t1.getNumberOfObjectives(), t1.getProblemFileName()));
    		best2.evaluate(new GenerationalDistance(t1.getNumberOfObjectives(), t1.getProblemFileName()));

    		System.out.println("GD:");
    		System.out.println(best1.getEval());
    		System.out.println(best2.getEval());

    		best1.evaluate(new InvertedGenerationalDistance(t1.getNumberOfObjectives(), t1.getProblemFileName()));
    		best2.evaluate(new InvertedGenerationalDistance(t1.getNumberOfObjectives(), t1.getProblemFileName()));

    		System.out.println("IGD:");
    		System.out.println(best1.getEval());
    		System.out.println(best2.getEval());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	
    }
}
