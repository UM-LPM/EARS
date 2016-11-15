package org.um.feri.ears.operators;

import java.util.EnumMap;

import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.util.Util;

public class PermutationSwapMutation implements MutationOperator<Integer, IntegerMOTask, MOSolutionBase<Integer>>{

	private double mutationProbability;
	  /**
	   * Constructor
	   */
	  public PermutationSwapMutation(double mutationProbability) {
	    if ((mutationProbability < 0) || (mutationProbability > 1)) {
	      System.err.println("Mutation probability value invalid: " + mutationProbability) ;
	    }
	    this.mutationProbability = mutationProbability;
	  }
	
	@Override
	public MOSolutionBase<Integer> execute(MOSolutionBase<Integer> solution, IntegerMOTask tb) {
	    doMutation(solution, tb);
	    return solution;
	}
	
	  /**
	   * Performs the operation
	 * @param tb 
	   */
	  public void doMutation(MOSolutionBase<Integer> solution, IntegerMOTask tb) {
	    int permutationLength ;
	    permutationLength = tb.getDimensions();

	    if ((permutationLength != 0) && (permutationLength != 1)) {
	      if (Util.rnd.nextDouble() < mutationProbability) {
	        int pos1 = Util.nextInt(0, permutationLength - 1);
	        int pos2 = Util.nextInt(0, permutationLength - 1);

	        while (pos1 == pos2) {
	          if (pos1 == (permutationLength - 1))
	            pos2 = Util.nextInt(0, permutationLength - 2);
	          else
	            pos2 = Util.nextInt(pos1, permutationLength - 1);
	        }

	        Integer temp = solution.getValue(pos1);
	        solution.setValue(pos1, solution.getValue(pos2));
	        solution.setValue(pos2, temp);
	      }
	    }
	  }

	@Override
	public void setProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
		
	}
	
	@Override
	public EnumMap<EnumAlgorithmParameters, String> getOperatorParameters() {
		EnumMap<EnumAlgorithmParameters, String> para = new EnumMap<EnumAlgorithmParameters, String>(EnumAlgorithmParameters.class);
		para.put(EnumAlgorithmParameters.P_M, mutationProbability+"");
		return para;
	}

}
