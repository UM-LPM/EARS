package org.um.feri.ears.algorithms.so.gsa;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.Arrays;

/**
 * @author crepinsek
 */
public class GSASolution extends DoubleSolution {
    private double[] v; //velocity
    private double[] a; //Acceleration
    private double[] E;
    private double mass; //mass attraction normalized


    public double[] getE() {
        return E;
    }

    public void setE(double[] e) {
        E = e;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double[] getV() {
        return v;
    }

    public double[] getA() {
        return v;
    }

    public GSASolution(Task t) throws StopCriterionException {
        super(t.getRandomEvaluatedSolution());
        v = new double[t.getNumberOfDimensions()]; //init V=zeros(N,dim);
        a = new double[t.getNumberOfDimensions()]; //init
        E = new double[t.getNumberOfDimensions()]; //init
        mass = 0;
    }

    public void resetE() {
        Arrays.fill(E, 0);
    }

    public GSASolution(DoubleSolution eval) {
        super(eval);
        int D = eval.getVariables().size();
        v = new double[D]; //init V=zeros(N,dim);
        a = new double[D]; //init
        E = new double[D]; //init
        mass = 0;
    }

    @Override
    public String toString() {
        return super.toString() + " v:" + (Arrays.toString(v) + " a:" + (Arrays.toString(a)));
    }

    public GSASolution move(Task t) throws StopCriterionException {
		/*
		[N,dim]=size(X);
		V=rand(N,dim).*V+a; %eq. 11.
		X=X+V; %eq. 12.
		*/
        double[] x = getDoubleVariables();

        boolean feasable = true;
        for (int i = 0; i < x.length; i++) {
            v[i] = Util.rnd.nextDouble() * v[i] + a[i];
            if (!t.isFeasible(x[i] + v[i], i)) { //not moved on edge
                feasable = false;
                break;
            }
            x[i] = t.setFeasible(x[i] + v[i], i); //if not feasable generate random?
            //%     %%Agents that go out of the search space, are reinitialized randomly .
            //Tp=X(i,:)>up;Tm=X(i,:)<low;X(i,:)=(X(i,:).*(~(Tp+Tm)))+((rand(1,dim).*(up-low)+low).*(Tp+Tm));
        }
        if (!feasable) { //if not feasable generate random
            x = t.getRandomVariables();
        }
        GSASolution tmp = new GSASolution(t.eval(x));
        for (int d = 0; d < t.getNumberOfDimensions(); d++) {
            tmp.a[d] = a[d];
            tmp.E[d] = E[d];
            tmp.v[d] = v[d];
            tmp.mass = mass;
        }
        return tmp;

    }


}
