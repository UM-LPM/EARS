package org.um.feri.ears.util;

import java.util.HashMap;
import java.util.List;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class ParetoSolutionCache {
	
	
	public String version = "1.0";
	public int limit = 100;
	//public HashMap<String,List<List<MOSolutionBase<Double>>>> data = new HashMap<String,List<List<MOSolutionBase<Double>>>>();
	public HashMap<String,List<List<double[]>>> data = new HashMap<String,List<List<double[]>>>();

}
