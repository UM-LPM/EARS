package org.um.feri.ears.util;

import java.util.Comparator;

public class ValueComparator implements Comparator {

	/**
	 * Stores the value of the index to compare
	 */
	private int index_;               

	/**
	 *  Constructor. 
	 *  Creates a new instance of ValueComparator 
	 */
	public ValueComparator(int index) {
		index_ = index;
	}

	/** 
	 * Compares the objects o1 and o2.
	 * @param o1 An object that reference a double[]
	 * @param o2 An object that reference a double[]
	 * @return -1 if o1 < o1, 1 if o1 > o2 or 0 in other case.
	 */
	public int compare(Object o1, Object o2) {
		//Cast to double [] o1 and o2.
		double [] pointOne = (double [])o1;
		double [] pointTwo = (double [])o2;

		if (pointOne[index_] < pointTwo[index_]) {
			return -1;
		} else if (pointOne[index_] > pointTwo[index_]) {
			return 1;
		} else {
			return 0;
		}
	} // compare
} // ValueComparator
