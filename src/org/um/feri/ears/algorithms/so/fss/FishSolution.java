package org.um.feri.ears.algorithms.so.fss;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.util.Util;

public class FishSolution extends DoubleSolution{
	
	public DoubleSolution neighbor = null;
	public DoubleSolution best = null;
	public DoubleSolution current = null;
	public double weight_now = Double.NaN;
	public double weight_past = Double.NaN;
	public double[] delta_x = null;
	public double delta_f = Double.NaN;
	public double fitness_gain_normalized = Double.NaN;
	
	public boolean individual_move_success=false;
	public boolean instinctive_move_success = false;
	public boolean volitive_move_success = false;
	
	
	public FishSolution (DoubleSolution s)
	{
		current = s;
		neighbor = new DoubleSolution(s);
		best = new DoubleSolution(s);
		 
		delta_x = new double[s.getVariables().length];
		//init fish weight
		weight_now = Util.nextDouble(FSS.fish_weight_min,FSS.fish_weight_max);
		weight_past = weight_now;
	}
}
