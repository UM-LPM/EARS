package org.um.feri.ears.problems.results;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.ProblemBase;
import org.um.feri.ears.util.MeanStDev;

public class BankOfResults {
	private Hashtable<AlgorithmBase,Hashtable<TaskBase,ArrayList<Double>>> all;
	
	public BankOfResults() {
		all = new Hashtable<AlgorithmBase,Hashtable<TaskBase,ArrayList<Double>>>();
	}
	public void add(TaskBase problem, SolutionBase individual, AlgorithmBase al) {
		Hashtable<TaskBase,ArrayList<Double>> tmpA;
		MeanStDev tmpBB;
		ArrayList<Double> tmpB;
		tmpA = all.get(al);
		if (tmpA==null) {
			tmpA = new Hashtable<TaskBase, ArrayList<Double>>();
			all.put(al, tmpA);
		}
		tmpB = tmpA.get(problem);
		if (tmpB==null) {
			tmpB = new ArrayList<Double>();
			tmpA.put(problem, tmpB);
		}
		tmpB.add(individual.getEval());
	}

	public FriedmanTransport calc4Friedman() {
		//FriedmanTransport
		double[][] mean;
		Vector<String> algorithms = new Vector<String>();
		Vector<String> datasets = new Vector<String>();
		StringBuffer sb = new StringBuffer();
		ArrayList<AlgorithmBase> tmp = new ArrayList<>(all.keySet());
		ArrayList<TaskBase> tmpProblem; // = new ArrayList(all.keySet());
		Hashtable<TaskBase,ArrayList<Double>> tmpA;
		ArrayList<Double> tmpB;
		DoubleSolution tmpC;
		MeanStDev std;
		mean=null;

		int i=-1;
		int j=-1;
		for (AlgorithmBase a: tmp) {
			i++;//
			algorithms.add(a.getID());
			tmpA =all.get(a);
			tmpProblem = new ArrayList<>(tmpA.keySet());
			//mean[i] = new double[tmpProblem.size()];
			j=-1;
			for (TaskBase  p: tmpProblem) {
				j++;
				if (i==0) {
					if (j==0) {
						mean = new double[tmpProblem.size()][];
						for (int g=0; g<tmpProblem.size();g++) {
							mean[g] = new double[tmp.size()];
						}
					}
					
					datasets.add(p.getProblemName());
				}
				tmpB = tmpA.get(p);
				std = new MeanStDev(tmpB);
					sb.append(a.getID()).append('\t').append(p.getProblemName());
					sb.append('\t').append(std.getMean());
					sb.append("\n");
				mean[j][i] = std.mean;
				}
			}
		System.out.println(sb);
		FriedmanTransport result = new FriedmanTransport(mean, algorithms,datasets);
		return result;
	}		
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		ArrayList<AlgorithmBase> tmp = new ArrayList<>(all.keySet());
		ArrayList<TaskBase> tmpProblem; // = new ArrayList(all.keySet());
		Hashtable<TaskBase,ArrayList<Double>> tmpA;
		ArrayList<Double> tmpB;
		DoubleSolution tmpC;
		for (AlgorithmBase  a: tmp) {
			tmpA =all.get(a);
			tmpProblem = new ArrayList<>(tmpA.keySet());
			for (TaskBase  p: tmpProblem) {
				tmpB = tmpA.get(p);
				for (Double in:tmpB) {
					sb.append(a.getID()).append('\t').append(p.getProblemName());
					sb.append('\t').append(in);
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	public void removeAlgorithm(AlgorithmBase al) {
		all.remove(al);
		
	}

}
