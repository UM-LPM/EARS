package org.um.feri.ears.algorithms.so.fss;

import java.util.Comparator;

/*
 * Copyright (c) 2011 Murilo Rebelo Pontes
 * murilo.pontes@gmail.com
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */


public class FSSComparatorByFitnessGain implements Comparator<FishSolution> {

    @Override
    public int compare(FishSolution o1, FishSolution o2) {
		return Double.compare(o1.delta_f, o2.delta_f);
	}

}
