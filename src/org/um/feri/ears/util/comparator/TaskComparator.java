package org.um.feri.ears.util.comparator;

import java.util.Comparator;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

public class TaskComparator implements Comparator<NumberSolution<Double>>{
	
	Task task;
	
	public TaskComparator(Task task) {
		super();
		this.task = task;
	}

	@Override
	public int compare(NumberSolution<Double> sol1, NumberSolution<Double> sol2) {
		
		if (task.isFirstBetter(sol1, sol2)) {
			return -1;
		}

		if (task.isFirstBetter(sol2, sol1)) {
			return 1;
		}

		return 0;
	}
}
