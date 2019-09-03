package org.um.feri.ears.memory;

import org.um.feri.ears.problems.Task;

abstract public class DuplicationRemovalStrategy {
	Task t;
	abstract public void changeSolution(double x[]);
	abstract public boolean criteria4Change(int hits);
	public boolean forceIncEvaluation() {
		return false;
	}
	public void setTask(Task t) {
		this.t = t;
	}
}
