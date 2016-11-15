package org.um.feri.ears.experiment.so.tk;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class LSAIndividual extends DoubleSolution 
{
	//private boolean evalviran = false;
	
	
	//public boolean getEvalviran(){ return evalviran;}
	//public void setEvalviran(boolean value){ evalviran = value;}
	
	public LSAIndividual(DoubleSolution eval) 
	{
		super(eval);	
		//evalviran = true;
		
		//v = new double[eval.getX().length];	
		//v_after = new double[eval.getX().length];
	}
	
	public LSAIndividual(LSAIndividual eval) 
	{
		super(eval);	
		//this.evalviran = eval.evalviran;
		
		//v = new double[eval.v.length];	
		//System.arraycopy(eval.v, 0, v, 0, eval.v.length);
		//v_after = new double[eval.v_after.length];
		//System.arraycopy(eval.v_after, 0, v_after, 0, eval.v_after.length);
		
		//masa = eval.masa;
	
	}

	public LSAIndividual(Task t) throws StopCriteriaException 
	{
		super(t.getRandomSolution());
		//evalviran = true;

	}	
}
