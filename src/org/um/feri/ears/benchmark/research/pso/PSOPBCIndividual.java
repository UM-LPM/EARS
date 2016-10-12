package org.um.feri.ears.benchmark.research.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOPBCIndividual extends DoubleSolution {
	
	PSOPBCIndividual Pbest;
	double hitrost[];

	public double[] getV(){
		return hitrost;
	}
	
	public PSOPBCIndividual getPbest(){
		return Pbest;
	}
	
	public PSOPBCIndividual(DoubleSolution i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	
	public PSOPBCIndividual(Task tsk) throws StopCriteriaException {
		super(tsk.getRandomSolution());
		hitrost = new double[tsk.getDimensions()];//Hitrost za vsako dimenzijo
		double l; double r;
		for (int i=0; i<tsk.getDimensions(); i++) {
			l = -Math.abs(tsk.getUpperLimit()[i]-tsk.getLowerLimit()[i])/4; 
			r = Math.abs(tsk.getUpperLimit()[i]-tsk.getLowerLimit()[i])/4; 
		    hitrost[i] = Util.rnd.nextDouble()*(r-l)+l;
		}
		Pbest = this;	
	}
	
	public PSOPBCIndividual Cross(PSOPBCIndividual Parent, Task tsk) throws StopCriteriaException {
			double pozicija[] = this.Pbest.getNewVariables();
			double pozicija2[] = Parent.Pbest.getNewVariables();
			
			for (int i=0; i<pozicija.length; i++) {
				pozicija[i] = (pozicija[i]+pozicija2[i])/2;
			}
			
			PSOPBCIndividual vmesni = new PSOPBCIndividual(tsk.eval(pozicija));
			
			if (tsk.isFirstBetter(vmesni, Pbest)) {
				vmesni.Pbest = vmesni;
			} else{
				vmesni.Pbest = Pbest;
			}
			
			vmesni.hitrost = this.hitrost;
			
			return vmesni;
	}
	
	public PSOPBCIndividual update(Task tsk, double hitrosti[]) throws StopCriteriaException {
		double pozicija[] = getNewVariables();
		
		for (int i=0; i<pozicija.length; i++) {
			pozicija[i]=tsk.feasible(pozicija[i]+hitrosti[i],i);
		}
		
		PSOPBCIndividual vmesni = new PSOPBCIndividual(tsk.eval(pozicija));
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
