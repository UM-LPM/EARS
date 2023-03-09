package org.um.feri.ears.operators;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Solution;

public interface MutationOperator<P extends Problem<S>, S extends Solution> extends Operator<S, S, P> {
	
	void setProbability(double mutationProbability);
}

