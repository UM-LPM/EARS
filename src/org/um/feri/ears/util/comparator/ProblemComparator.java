package org.um.feri.ears.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Solution;

public class ProblemComparator<S extends Solution> implements Comparator<S>, Serializable {
	
	Problem<S> problem;
	
	public ProblemComparator(Problem<S> problem) {
		super();
		this.problem = problem;
	}

	@Override
	public int compare(S sol1, S sol2) {
		
		if (problem.isFirstBetter(sol1, sol2)) {
			return -1;
		}

		if (problem.isFirstBetter(sol2, sol1)) {
			return 1;
		}

		return 0;
	}
}
