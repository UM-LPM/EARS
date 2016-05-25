package org.um.feri.ears.operators;

import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.MOSolutionBase;


public interface CrossoverOperator<Type, Task extends MOTask> extends Operator<MOSolutionBase<Type>[],MOSolutionBase<Type>[], Task> {
	
	public abstract void setCurrentSolution(MOSolutionBase<Type> current);

}