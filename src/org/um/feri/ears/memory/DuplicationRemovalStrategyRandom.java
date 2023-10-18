package org.um.feri.ears.memory;

import org.um.feri.ears.util.random.RNG;

public class DuplicationRemovalStrategyRandom extends DuplicationRemovalStrategy {
    int maxHits;

    public DuplicationRemovalStrategyRandom(int maxHits) {
        this.maxHits = maxHits;
    }

    @Override
    public void changeSolution(double[] x) {
        double[] tmp = t.problem.getRandomVariables();
        for (int i = 0; i < x.length; i++) {
            if (RNG.nextDouble() < 0.2)
                x[i] = tmp[i];
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
