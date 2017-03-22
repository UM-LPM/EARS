package org.um.feri.ears.experiment.so.tk;


import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class ECBOSolution extends DoubleSolution
{
	double v[];
	double v_after[];
	double new_x[];
	
	double masa;
	boolean stacionarni;
	
	
	
	
	
	public ECBOSolution(DoubleSolution eval) 
	{
		super(eval);	
		v = new double[eval.getVariables().length];	
		v_after = new double[eval.getVariables().length];
	}
	
	public ECBOSolution(ECBOSolution eval) 
	{
		super(eval);	
		
		v = new double[eval.v.length];	
		System.arraycopy(eval.v, 0, v, 0, eval.v.length);
		v_after = new double[eval.v_after.length];
		System.arraycopy(eval.v_after, 0, v_after, 0, eval.v_after.length);
		
		masa = eval.masa;
	
	}
	
	public ECBOSolution(Task t) throws StopCriteriaException 
	{
		super(t.getRandomSolution());
		v = new double[t.getNumberOfDimensions()];
		v_after = new double[t.getNumberOfDimensions()];
	}
	
	public double[] getV() {
		return v;
	}
	
	public double getM() {
		return masa;
	}
	
	public double[] getNoviX() {
		double[] xx = new double[new_x.length];
		System.arraycopy(new_x, 0, xx, 0, new_x.length);
		return xx;
	}
	
	public boolean isStacionar()
	{
		return stacionarni;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	
	//za contains metodo
	public boolean equals(Object object) 
	{
        //if (object != null && object instanceof CBOIndividual) 
        {
        	ECBOSolution thing = (ECBOSolution) object;
        	
        	if(this.getEval() == thing.getEval())
        		return true;
        	else 
        		return false;
        	
        }
    }
}