package org.um.feri.ears.operators;

import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;

public interface MutationOperator<Type, Task extends TaskBase, Solution extends SolutionBase<Type>> extends Operator<Solution, Solution, Task> {
	
	public void setProbability(double mutationProbability);
}

