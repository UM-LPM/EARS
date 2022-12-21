package org.um.feri.ears.algorithms.so.bfo;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class Bacteria extends NumberSolution<Double> implements Comparable<Bacteria> {
	Bacteria prev;
	double health;
	double c;
	
	public Bacteria(Task t, double c) throws StopCriterionException {
		super(t.getRandomEvaluatedSolution());
		prev = this;
		health = 0;
		this.c = c;
	}
	
	public Bacteria(Bacteria b) throws StopCriterionException {
		super(b);
		prev = b.prev;
		health = b.health;
		c = b.c;
	}

	public Bacteria(NumberSolution<Double> eval, Bacteria b) throws StopCriterionException {
		super(eval);
		prev = new Bacteria(b);
		this.health += this.health + prev.getEval();
		this.c = b.c;
	}

	@Override
	public int compareTo(Bacteria b) {
		if (health < b.health)
			return -1;
		else if (health > b.health)
			return 1;
		return 0;
	}
}
