package org.um.feri.ears.memory;

import java.util.Arrays;

import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class DuplicationRemovalStrategyRandom extends DuplicationRemovalStrategy {
	int maxHits;
	public DuplicationRemovalStrategyRandom(int maxHits) {
		this.maxHits = maxHits;
	}

	@Override
	public void changeSolution(double[] x) {
		double[] tmp = t.getRandomVariables();
		for (int i=0; i < x.length; i++) {
			if (Util.nextDouble()<0.2)
				x[i] = tmp[i];
		}

	}
	@Override
	public boolean criteria4Change(int hits) {
		if (hits>maxHits) return true;
		return false;
	}
	public String toString( ) {
		StringBuffer sb = new StringBuffer();
		sb.append("Random duplicate strategy ");
		sb.append(" with max hits ").append(maxHits);
		return sb.toString();
	}
}
