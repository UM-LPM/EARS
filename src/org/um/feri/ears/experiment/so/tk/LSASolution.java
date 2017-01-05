package org.um.feri.ears.experiment.so.tk;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class LSASolution extends DoubleSolution 
{
	
	//nekaj test
	public LSASolution(DoubleSolution eval) 
	{
		super(eval);	
		//evalviran = true;
		//v = new double[eval.getX().length];	
		//v_after = new double[eval.getX().length];
	}
	
	public LSASolution(LSASolution eval) 
	{
		super(eval);	
		//this.evalviran = eval.evalviran;
		
		//v = new double[eval.v.length];	
		//System.arraycopy(eval.v, 0, v, 0, eval.v.length);
		//v_after = new double[eval.v_after.length];
		//System.arraycopy(eval.v_after, 0, v_after, 0, eval.v_after.length);
		
		//masa = eval.masa;
	
	}

	public LSASolution(Task t) throws StopCriteriaException 
	{
		super(t.getRandomSolution());
		//evalviran = true;

	}	
}
