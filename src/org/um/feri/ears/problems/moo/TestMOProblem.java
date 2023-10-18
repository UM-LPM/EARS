package org.um.feri.ears.problems.moo;

import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead.D_MOEAD;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.problems.moo.zdt.ZDT1;
import org.um.feri.ears.quality_indicator.GenerationalDistance;
import org.um.feri.ears.quality_indicator.InvertedGenerationalDistance;

public class TestMOProblem {
    public static void main(String[] args) {
    	ZDT1 zdt = new ZDT1();
    	ParetoSolution filePareto = new ParetoSolution();
    	filePareto.loadObjectivesFromFile("test_pareto//ZDT1_middle.dat");
    	//filePareto.displayData("Uniform",zdt.getName(), zdt);
    	//filePareto.displayAllUnaryQulaityIndicators(zdt);

    	

    	Task<NumberSolution<Double>, DoubleProblem> t1 = new Task<>(new UnconstrainedProblem1(), StopCriterion.EVALUATIONS, 300000, 500, 300, 0.0001);
    	Task<NumberSolution<Double>,DoubleProblem> t2 = new Task<>(new UnconstrainedProblem1(), StopCriterion.EVALUATIONS, 300000, 500, 300, 0.0001);



    	D_MOEAD moead = new D_MOEAD();
    	D_IBEA ibea = new D_IBEA();
    	moead.setDisplayData(true);
    	ibea.setDisplayData(true);
    	try {
    		ParetoSolution best1 = moead.execute(t1);
    		ParetoSolution best2 = ibea.execute(t2);

    		best1.evaluate(new GenerationalDistance(t1.problem.getNumberOfObjectives(), t1.problem.getReferenceSetFileName()));
    		best2.evaluate(new GenerationalDistance(t1.problem.getNumberOfObjectives(), t1.problem.getReferenceSetFileName()));

    		System.out.println("GD:");
    		System.out.println(best1.getEval());
    		System.out.println(best2.getEval());

    		best1.evaluate(new InvertedGenerationalDistance(t1.problem.getNumberOfObjectives(), t1.problem.getReferenceSetFileName()));
    		best2.evaluate(new InvertedGenerationalDistance(t1.problem.getNumberOfObjectives(), t1.problem.getReferenceSetFileName()));

    		System.out.println("IGD:");
    		System.out.println(best1.getEval());
    		System.out.println(best2.getEval());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	
    }
}
