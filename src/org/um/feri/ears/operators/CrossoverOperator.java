package org.um.feri.ears.operators;

import org.um.feri.ears.problems.*;

public interface CrossoverOperator<N extends Number, P extends Problem<S>, S extends Solution> extends Operator<S[], S[], P> {
	
	void setCurrentSolution(NumberSolution<N> current);

}