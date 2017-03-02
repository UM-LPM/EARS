package org.um.feri.ears.algorithms.so.fss;

import java.util.Comparator;

import org.um.feri.ears.problems.Task;

/*
 * Copyright (c) 2011 Murilo Rebelo Pontes
 * murilo.pontes@gmail.com
 * 
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

public class FSSComparatorByBestFitness implements Comparator<FishSolution>{
	
	Task task;
	
	public FSSComparatorByBestFitness(Task task){
		super();
		this.task = task;
	}

	@Override
	public int compare(FishSolution o1, FishSolution o2) {
		
		if (task.isFirstBetter(o1.best, o2.best)) {
			return -1;
		}

		if (task.isFirstBetter(o2.best, o1.best)) {
			return 1;
		}

		return 0;
	}

}
