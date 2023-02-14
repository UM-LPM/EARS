package org.um.feri.ears.operators;

import org.um.feri.ears.problems.Solution;
import org.um.feri.ears.problems.TaskBase;

public interface MutationOperator<Type, Task extends TaskBase, Sol extends Solution> extends Operator<Sol, Sol, Task> {
	
	public void setProbability(double mutationProbability);
}

