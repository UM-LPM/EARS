package org.um.feri.ears.util;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class FutureResult <T extends MOTask, Type extends Number> {
	
	public MOAlgorithm<T, Type> algorithm;
	public ParetoSolution<Type> result;

	public FutureResult(MOAlgorithm<T, Type> al, ParetoSolution<Type> res) {
		this.algorithm = al;
		this.result = res;
	}

}
