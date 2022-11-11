package org.um.feri.ears.operators;

import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;


public interface CrossoverOperator<Type extends Number, Task extends TaskBase, Solution extends SolutionBase> extends Operator<Solution[], Solution[], Task> {
	
	public abstract void setCurrentSolution(MOSolutionBase<Type> current);

}