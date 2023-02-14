package org.um.feri.ears.operators;

import org.um.feri.ears.problems.*;

public interface CrossoverOperator<Type extends Number, Task extends TaskBase, Sol extends Solution> extends Operator<Sol[], Sol[], Task> {
	
	public abstract void setCurrentSolution(NumberSolution<Type> current);

}