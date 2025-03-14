package org.um.feri.ears.memory;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class DuplicationRemovalStrategyRandom extends DuplicationRemovalStrategy {
    int maxHits;

    public DuplicationRemovalStrategyRandom(int maxHits) {
        this.maxHits = maxHits;
    }


    @Override
    public void changeSolution(NumberSolution<Double> solution) {
        double[] tmp = t.problem.generateRandomVariables();
        var list = solution.getVariables(); //hope this is not deep copy
        for (int i = 0; i < list.size(); i++) {
            if (RNG.nextDouble() < 0.2)
                list.set(i, tmp[i]);
        }
    }

    @Override
    public boolean criteria4Change(int hits) {
		return hits > maxHits;
	}

    public String toString() {
		return "Random duplicate strategy " +
				" with max hits " + maxHits;
    }
}
