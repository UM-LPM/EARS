package org.um.feri.ears.experiments;

import java.util.Arrays;

import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;

public class ReferencePointFinder {

	public static void main(String[] args) {

		Double[] dReference;
		Integer[] iReference;
		
		int numEval = 3000000;
		
		CITOProblem problem = new CITOProblem("OO_MyBatis");
		IntegerMOTask t = new IntegerMOTask(EnumStopCriteria.EVALUATIONS, numEval, 0.0001,  problem);
		
		iReference = new Integer[problem.getNumberOfObjectives()];
		dReference = new Double[problem.getNumberOfObjectives()];
		
		for(int j = 0; j < problem.getNumberOfObjectives(); j++)
		{
			iReference[j] = Integer.MIN_VALUE;
			dReference[j] = Double.MIN_VALUE;
		}
		
		
		for (int i = 0; i < numEval; i++) {
			try {
				MOSolutionBase<Integer> solution = t.getRandomMOIndividual();
				
				for(int j = 0; j < problem.getNumberOfObjectives(); j++)
				{
					if(iReference[j] < solution.getObjective(j))
					{
						iReference[j] = (int) solution.getObjective(j);
					}
				}
				
			} catch (StopCriteriaException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(Arrays.toString(iReference));

	}

}
