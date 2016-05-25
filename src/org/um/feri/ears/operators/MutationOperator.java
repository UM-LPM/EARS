package org.um.feri.ears.operators;

import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.MOSolutionBase;

public interface MutationOperator<Type, Task extends MOTask> extends Operator<MOSolutionBase<Type>, MOSolutionBase<Type>, Task> {
	
	public void setProbability(double mutationProbability);
}

