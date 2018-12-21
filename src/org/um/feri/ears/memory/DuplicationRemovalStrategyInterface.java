package org.um.feri.ears.memory;

import org.um.feri.ears.problems.Task;

public interface DuplicationRemovalStrategyInterface {
	abstract public void changeSolution(double x[]);
	abstract public boolean criteria4Change(int hits);
}
