package org.um.feri.analyse.correlationdependence;

import java.util.Random;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.Griewank;
import org.um.feri.ears.problems.unconstrained.cec2014.F1;
import org.um.feri.ears.problems.unconstrained.cec2014.F10;
import org.um.feri.ears.problems.unconstrained.cec2014.F11;
import org.um.feri.ears.problems.unconstrained.cec2014.F12;
import org.um.feri.ears.problems.unconstrained.cec2014.F13;
import org.um.feri.ears.problems.unconstrained.cec2014.F14;
import org.um.feri.ears.problems.unconstrained.cec2014.F15;
import org.um.feri.ears.problems.unconstrained.cec2014.F16;
import org.um.feri.ears.problems.unconstrained.cec2014.F2;
import org.um.feri.ears.problems.unconstrained.cec2014.F3;
import org.um.feri.ears.problems.unconstrained.cec2014.F4;
import org.um.feri.ears.problems.unconstrained.cec2014.F5;
import org.um.feri.ears.problems.unconstrained.cec2014.F6;
import org.um.feri.ears.problems.unconstrained.cec2014.F7;
import org.um.feri.ears.problems.unconstrained.cec2014.F8;
import org.um.feri.ears.problems.unconstrained.cec2014.F9;

public class CalculateEps {
	
	public static Double calculateDistanceBetweenMin(Problem prob)
	{
		Double distanceAvg=0.0;
		int numOfCand = 10;
		double distances[] = new double[numOfCand];
		int dim = prob.getNumberOfDimensions();
		
		for(int i = 0; i < numOfCand; ++i)
		{
			double[] distancePerDim = new double[dim];
			double[] candidate = new double[dim];
			
			if(i ==0 || i == 2 || i == 8)
			{
				candidate = prob.getOptimalVector()[0];
			}
			else
			{
				candidate = new Random()
						.doubles(dim, (double) prob.lowerLimit.get(0), (double) prob.upperLimit.get(0)).toArray();
			}
			for (int curDim = 0; curDim < dim; ++curDim) {
				Double[] leftX = calculateLeft(prob, candidate, curDim);
				Double[] rightX = calcucalteRight(prob, candidate, curDim);
				Double dist = leftX[curDim] - rightX[curDim];

				distancePerDim[curDim] = Math.abs(dist) / 2;
			}
			distances[i] = DoubleStream.of(distancePerDim).sum() / distancePerDim.length;
		}
		
		distanceAvg = DoubleStream.of(distances).sum() / distances.length;
		return distanceAvg;
	}

	private static Double[] calcucalteRight(Problem prob, double[] candidate, int dim) {

		boolean doCalc = true;
		boolean goDown = true;
		Double[] cand = ArrayUtils.toObject(candidate);
		
		do{
			cand[dim] = cand[dim] - 0.1;
			double prevEval = prob.eval(cand);
			
			cand[dim] = cand[dim] + 0.1; // to previous value;
			double eval = prob.eval(cand);
			
			cand[dim] = cand[dim] + 0.1;
			double nextEval = prob.eval(cand);
			
			if(cand[dim] >= prob.upperLimit.get(0))
			{
				doCalc = false;
			}
			
			if (prevEval < eval && eval < nextEval)
			{
				goDown = false;
			}
			
			if (prevEval > eval && eval > nextEval)
			{
				if(goDown == false)
				{
					doCalc = false;
					cand[dim] -= 0.1;
				}
			}
			
		}while(doCalc);
		
		return cand;
	}

	private static Double[] calculateLeft(Problem prob, double[] candidate, int dim) {
		boolean doCalc = true;
		boolean goDown = true;
		Double[] cand = ArrayUtils.toObject(candidate);
		
		do{
			cand[dim] = cand[dim] + 0.1;
			double prevEval = prob.eval(cand);
			
			cand[dim] = cand[dim] - 0.1; // to previous value;
			double eval = prob.eval(cand);
			
			cand[dim] = cand[dim] - 0.1;
			double nextEval = prob.eval(cand);
			
			if(cand[dim] >= prob.lowerLimit.get(0))
			{
				doCalc = false;
			}
			
			if (prevEval < eval && eval < nextEval)
			{
				goDown = false;
			}
			
			if (prevEval > eval && eval > nextEval)
			{
				if(goDown == false)
				{
					doCalc = false;
					cand[dim] += 0.1;
				}
			}
			
		}while(doCalc);
		
		
		return cand;
	}

	public static void main(String[] args) 
	{
		int dimm = 2;
		int part = Integer.parseInt(args[0]);
		int numOfProblems = 16;
		Problem problems[] = new Problem[numOfProblems];
		Double dist = 0.0;

		
		switch(part){
			case 1:
			{
				problems[0] = new F1(dimm);
				dist = calculateDistanceBetweenMin(problems[0]);
				System.out.println("if(args[0].contains(\""+problems[0].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[1] = new F2(dimm);
				dist = calculateDistanceBetweenMin(problems[1]);
				System.out.println("else if(args[0].contains(\""+problems[1].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[2] = new F3(dimm);
				dist = calculateDistanceBetweenMin(problems[2]);
				System.out.println("else if(args[0].contains(\""+problems[2].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[3] = new F4(dimm);
				dist = calculateDistanceBetweenMin(problems[3]);
				System.out.println("else if(args[0].contains(\""+problems[3].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
			//	break;
			}
			case 2:
			{
				problems[4] = new F5(dimm);
				dist = calculateDistanceBetweenMin(problems[4]);
				System.out.println("else if(args[0].contains(\""+problems[4].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[5] = new F6(dimm);
				dist = calculateDistanceBetweenMin(problems[5]);
				System.out.println("else if(args[0].contains(\""+problems[5].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[6] = new F7(dimm);
				dist = calculateDistanceBetweenMin(problems[6]);
				System.out.println("else if(args[0].contains(\""+problems[6].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[7] = new F8(dimm);
				dist = calculateDistanceBetweenMin(problems[7]);
				System.out.println("else if(args[0].contains(\""+problems[7].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[8] = new F9(dimm);
				dist = calculateDistanceBetweenMin(problems[8]);
				System.out.println("else if(args[0].contains(\""+problems[8].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
		//		break;
			}
			case 3:
			{
				problems[9] = new F10(dimm);
				dist = calculateDistanceBetweenMin(problems[9]);
				System.out.println("else if(args[0].contains(\""+problems[9].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[10] = new F11(dimm);
				dist = calculateDistanceBetweenMin(problems[10]);
				System.out.println("else if(args[0].contains(\""+problems[10].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[11] = new F12(dimm);
				dist = calculateDistanceBetweenMin(problems[11]);
				System.out.println("else if(args[0].contains(\""+problems[11].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[12] = new F13(dimm);
				dist = calculateDistanceBetweenMin(problems[12]);
				System.out.println("else if(args[0].contains(\""+problems[12].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
			//	break;
			}
			case 4:
			{
				problems[13] = new F14(dimm);
				dist = calculateDistanceBetweenMin(problems[13]);
				System.out.println("if(args[0].contains(\""+problems[13].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[14] = new F15(dimm);
				dist = calculateDistanceBetweenMin(problems[14]);
				System.out.println("if(args[0].contains(\""+problems[14].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				problems[15] = new F16(dimm);
				dist = calculateDistanceBetweenMin(problems[15]);
				System.out.println("if(args[0].contains(\""+problems[15].getName()+"\")) ");
				System.out.println("{");
				System.out.println("\tepsilon="+dist+";");
				System.out.println("}");
				break;
			}
			default:
			{
				Problem problem = new Griewank(dimm);
				dist = calculateDistanceBetweenMin(problem);
				System.out.println(problem.getName()+" distance is "+dist);
				break;
			}
		}

		
		


	}

}
