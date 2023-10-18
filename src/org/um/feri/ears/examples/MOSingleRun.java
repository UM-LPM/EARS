package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.moo.demo.D_DEMO;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead.D_MOEAD;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_STM;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.nsga3.D_NSGAIII;
import org.um.feri.ears.algorithms.moo.paes.D_PAES;
import org.um.feri.ears.algorithms.moo.pesa2MOEA.D_PESA2;
import org.um.feri.ears.algorithms.moo.pso.OMOPSO;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.zdt.ZDT6;

public class MOSingleRun {


	public static void main(String[] args) {
		
		D_NSGAII nsga2 = new D_NSGAII();
		D_MOEAD moead = new D_MOEAD();
		D_SPEA2 spea2 = new D_SPEA2();
		D_NSGAIII nsga3 = new D_NSGAIII();
		D_PESA2 pesa2 = new D_PESA2();
		D_PAES paes = new D_PAES();
		D_GDE3 gde3 = new D_GDE3();
		D_IBEA ibea = new D_IBEA();
		D_MOEAD_DRA moead_dra = new D_MOEAD_DRA();
		D_MOEAD_STM moead_stm = new D_MOEAD_STM();
		D_DEMO demo = new D_DEMO();
		OMOPSO pso = new OMOPSO();

		try {

			ZDT6 p = new ZDT6(10);


			Task<NumberSolution<Double>, DoubleProblem> task = new Task<>(p, StopCriterion.EVALUATIONS, 30000, 5000, 100);
			ParetoSolution best = moead.execute(task);
			best.saveObjectivesToCSVFile("test");

			//best.evaluate(new InvertedGenerationalDistance(p.getNumberOfObjectives(), p.getReferenceSetFileName()));

			System.out.println(best.getEval());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
