package org.um.feri.ears.util;

import java.util.Comparator;

import org.um.feri.ears.problems.DoubleSolution;


public class FitnessComparator implements Comparator<DoubleSolution>{
	   
    /**
	* Compares two solutions.
	* @param o1 Object representing the first <code>Solution</code>.
	* @param o2 Object representing the second <code>Solution</code>.
	* @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
	* respectively.
	*/
	public int compare(DoubleSolution o1, DoubleSolution o2) {
		if (o1 == null)
			return 1;
		else if (o2 == null)
			return -1;

		double fitness1 = o1.getEval();
		double fitness2 = o2.getEval();
		if (fitness1 < fitness2) {
			return -1;
		}

		if (fitness1 > fitness2) {
			return 1;
		}

		return 0;
	}
}
