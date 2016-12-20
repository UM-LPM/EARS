package org.um.feri.ears.vaje;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.Objective;

public class Schaffer extends DoubleMOProblem{
	
	
	public Schaffer(){
		this(2,0,2);
	}
	
	public Schaffer(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, numberOfConstraints, numberOfObjectives);
		
		name="Schaffer";
		
		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);


		for (int i = 0; i < numberOfDimensions; i++) {
			lowerLimit.add(-50.0);
			upperLimit.add(50.0);
		}
		
		this.addObjective(new SCH_F1());
		this.addObjective(new SCH_F2());

	}
	

	/*@Override
	public double[] evaluate(Double[] ds) {
		
		double obj[] = new double[numberOfObjectives];

		obj[0] = Math.pow(ds[0], 2);
		obj[1] = Math.pow(ds[1] - 2, 2);

		return obj;
	}*/


	public class SCH_F1 extends Objective{

		@Override
		public double eval(double[] ds) {
			
			double sum = 0.0;
			for(int i = 0; i < ds.length; i++)
			{
				sum += Math.pow(ds[i], 2);
			}
			return sum;
		}
	}

	public class SCH_F2 extends Objective{

		@Override
		public double eval(double[] ds) {
			
			double sum = 0.0;
			for(int i = 0; i < ds.length; i++)
			{
				sum += Math.pow(ds[i] - 2, 2);
			}
			return sum;
		}
	}


	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
		// TODO Auto-generated method stub
		
	}
	

}
