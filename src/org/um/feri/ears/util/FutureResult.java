package org.um.feri.ears.util;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class FutureResult {
	
	public MOAlgorithm algorithm;
	public ParetoSolution result;
	public MOTask task;

	public FutureResult(MOAlgorithm al, ParetoSolution res, MOTask task) {
		this.algorithm = al;
		this.result = res;
		this.task = task;
	}

}
