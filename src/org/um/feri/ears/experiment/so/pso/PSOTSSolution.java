package org.um.feri.ears.experiment.so.pso;

import java.util.Arrays;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOTSSolution extends DoubleSolution {
	PSOTSSolution Pbest;
	double v[];

	public PSOTSSolution getPbest() {
		return Pbest;
	}

	public void setPbest(PSOTSSolution Pbest) {
		this.Pbest = Pbest;
	}

	public double[] getV() {
		return v;
	}

	public PSOTSSolution(Task t) throws StopCriteriaException {
		super(t.getRandomSolution());
		v = new double[t.getDimensions()];
		double l;
		double r;
		for (int i = 0; i < t.getDimensions(); i++) {
			l = -Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			r = Math.abs(t.getUpperLimit()[i] - t.getLowerLimit()[i]) / 4;
			v[i] = Util.nextDouble(l,r);
		}
		Pbest = this;
	}

	public PSOTSSolution(DoubleSolution eval) {
		super(eval);

	}
	
	public PSOTSSolution Cross(PSOTSSolution Parent, Task tsk) throws StopCriteriaException {
		double pozicija[] = this.Pbest.getNewVariables();
		double pozicija2[] = Parent.Pbest.getNewVariables();
		
		for (int i=0; i<pozicija.length; i++) {
			pozicija[i] = (pozicija[i]+pozicija2[i])/2;
		}
		
		PSOTSSolution vmesni = new PSOTSSolution(tsk.eval(pozicija));
		
		if (tsk.isFirstBetter(vmesni, Pbest)) {
			vmesni.Pbest = vmesni;
		} else{
			vmesni.Pbest = Pbest;
		}
		
		vmesni.v = this.v;
		
		return vmesni;
	}

	@Override
	public String toString() {
		return super.toString() + " v:" + (Arrays.toString(v) + " p:" + Pbest.getEval());
	}

	public PSOTSSolution update(Task t, double v[]) throws StopCriteriaException {
		double x[] = getNewVariables();
		for (int i = 0; i < x.length; i++) {
			x[i] = t.feasible(x[i] + v[i], i);
		}
		PSOTSSolution tmp = new PSOTSSolution(t.eval(x));
		if (t.isFirstBetter(tmp, Pbest)) {
			tmp.Pbest = tmp;
		} else
			tmp.Pbest = Pbest;
		tmp.v = v;
		return tmp;

	}
}