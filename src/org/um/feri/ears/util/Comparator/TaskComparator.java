package org.um.feri.ears.util.Comparator;

import java.util.Comparator;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Task;

public class TaskComparator implements Comparator<DoubleSolution>{
	
	Task task;
	
	public TaskComparator(Task task) {
		super();
		this.task = task;
	}

	@Override
	public int compare(DoubleSolution sol1, DoubleSolution sol2) {
		
		if (task.isFirstBetter(sol1, sol2)) {
			return -1;
		}

		if (task.isFirstBetter(sol2, sol1)) {
			return 1;
		}

		return 0;
	}
}
