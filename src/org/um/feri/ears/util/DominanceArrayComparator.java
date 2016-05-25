package org.um.feri.ears.util;

import java.util.Comparator;


public class DominanceArrayComparator implements Comparator<double[]> {

	
    /**
    * Compares two arrays.
    * @param object1 Object representing the first array.
    * @param object2 Object representing the second array.
    * @return -1, or 0, or 1 if solution1 dominates solution2, both are 
    * non-dominated, or solution1  is dominated by solution2, respectively.
    */
	public int compare(double[] object1, double[] object2)
	{
		
		if (object1 == null)
			return 1;
		else if (object2 == null)
			return -1;

		double[] set1 = object1;
		double[] set2 = object2;
		
		int dominate1; // dominate1 indicates if some objective of set1
		// dominates the same objective in set. dominate2
		int dominate2; // is the complementary of dominate1.

		dominate1 = 0;
		dominate2 = 0;

		int flag; // stores the result of the comparison

		double value1, value2;
		for (int i = 0; i < set1.length; i++) {
			value1 = set1[i];
			value2 = set2[i];
			if (value1 < value2) {
				flag = -1;
			} else if (value1 > value2) {
				flag = 1;
			} else {
				flag = 0;
			}

			if (flag == -1) {
				dominate1 = 1;
			}

			if (flag == 1) {
				dominate2 = 1;
			}
		}

		if (dominate1 == dominate2) {
			return 0; // No one dominate the other
		}
		if (dominate1 == 1) {
			return -1; // solution1 dominate
		}
		return 1; // solution2 dominate
	}

}
