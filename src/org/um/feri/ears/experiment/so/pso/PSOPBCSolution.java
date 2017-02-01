package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOPBCSolution extends DoubleSolution {
	
	PSOPBCSolution Pbest;
	double hitrost[];

	public double[] getV(){
		return hitrost;
	}
	
	public PSOPBCSolution getPbest(){
		return Pbest;
	}
	
	public PSOPBCSolution(DoubleSolution i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	
	public PSOPBCSolution(Task tsk) throws StopCriteriaException {
		super(tsk.getRandomSolution());
		hitrost = new double[tsk.getNumberOfDimensions()];//Hitrost za vsako dimenzijo
		double l; double r;
		for (int i=0; i<tsk.getNumberOfDimensions(); i++) {
			l = -Math.abs(tsk.getUpperLimit()[i]-tsk.getLowerLimit()[i])/4; 
			r = Math.abs(tsk.getUpperLimit()[i]-tsk.getLowerLimit()[i])/4; 
		    hitrost[i] = Util.nextDouble(l,r);
		}
		Pbest = this;	
	}
	
	public PSOPBCSolution Cross(PSOPBCSolution Parent, Task tsk) throws StopCriteriaException {
			double pozicija[] = this.Pbest.getNewVariables();
			double pozicija2[] = Parent.Pbest.getNewVariables();
			
			for (int i=0; i<pozicija.length; i++) {
				pozicija[i] = (pozicija[i]+pozicija2[i])/2;
			}
			
			PSOPBCSolution vmesni = new PSOPBCSolution(tsk.eval(pozicija));
			
			if (tsk.isFirstBetter(vmesni, Pbest)) {
				vmesni.Pbest = vmesni;
			} else{
				vmesni.Pbest = Pbest;
			}
			
			vmesni.hitrost = this.hitrost;
			
			return vmesni;
	}
	
	public PSOPBCSolution update(Task tsk, double hitrosti[]) throws StopCriteriaException {
		double pozicija[] = getNewVariables();
		
		for (int i=0; i<pozicija.length; i++) {
			pozicija[i]=tsk.setFeasible(pozicija[i]+hitrosti[i],i);
		}
		
		PSOPBCSolution vmesni = new PSOPBCSolution(tsk.eval(pozicija));
		if (tsk.isFirstBetter(vmesni, Pbest)) {
			vmesni.Pbest = vmesni;
		} else{
			vmesni.Pbest = Pbest;
		}
		
		vmesni.hitrost = hitrosti;
		
		return vmesni;
		
	}
	
	@Override
	public String toString() {
		return super.toString()+" hitrost:"+(Arrays.toString(hitrost)+" Pbest:"+Pbest.getEval());
	}
	
}
