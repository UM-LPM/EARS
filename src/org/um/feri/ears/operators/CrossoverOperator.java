package org.um.feri.ears.operators;

import org.um.feri.ears.problems.*;

public interface CrossoverOperator<P extends Problem<S>, S extends Solution> extends Operator<S[], S[], P> {
	
	void setCurrentSolution(S current);

}