package org.um.feri.ears.operators;

import org.um.feri.ears.problems.*;

public interface CrossoverOperator<Type extends Number, Task extends TaskBase, Solution extends SolutionBase> extends Operator<Solution[], Solution[], Task> {
	
	public abstract void setCurrentSolution(NumberSolution<Type> current);

}