package org.um.feri.ears.experiments;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.de.DEAlgorithm;
import org.um.feri.ears.algorithms.tlbo.TLBOAlgorithm;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.misc.SoilModelProblem;
import org.um.feri.ears.util.Util;

public class SoilModelExperiment {
	
	public static void main(String[] args) throws StopCriteriaException
	{
		Util.rnd.setSeed(System.currentTimeMillis());

		List<Algorithm> players = new ArrayList<Algorithm>();
		players.add(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 50));
		players.add(new TLBOAlgorithm());
		
		String[] meritve = {"meritev4", "meritevA", "meritev_gui"};
		int run_num = 100;
		
		double[][] i2_res;
		double[][] i3_res;
		
		
		StandardDeviation sd = new StandardDeviation();
		Mean mean = new Mean();
		
		StringBuilder sb = new StringBuilder();
		//DecimalFormat df0 = new DecimalFormat("####.##");
		
		sb.append(";;");
		for(String m : meritve)
		{
			sb.append(m+";;");
		}
		sb.append("\n");
		sb.append("metoda;;"+ StringUtils.repeat("i2;i3;", meritve.length));
		sb.append("\n");
		
		for (Algorithm alg : players) {
			i2_res = new double[meritve.length][run_num];
			i3_res = new double[meritve.length][run_num];
			
			sb.append(alg.getAlgorithmInfo().getVersionAcronym());
			System.out.println(alg.getAlgorithmInfo().getVersionAcronym());
			for(int k=0;k < meritve.length; k++)
			{
				String m = meritve[k];
				System.out.println("meritev: "+m);
				SoilModelProblem i2 = new SoilModelProblem(3,0,2,m); // numberOfDimensions, numberOfConstraints, layers, filename
				SoilModelProblem i3 = new SoilModelProblem(5,0,3,m);

				Task ti2,ti3;
				for(int i = 0; i < run_num; i++)
				{
					System.out.println(i);
					ti2 = new Task(EnumStopCriteria.EVALUATIONS, 20000, 0.0001, i2);
					ti3 = new Task(EnumStopCriteria.EVALUATIONS, 20000, 0.0001, i3);
					i2_res[k][i] = alg.run(ti2).getEval();
					i3_res[k][i] = alg.run(ti3).getEval();
				}
			}

			sb.append(";MIN;");
			for(int k=0;k < meritve.length; k++)
			{
				sb.append(NumberUtils.min(i2_res[k])+";");
				sb.append(NumberUtils.min(i3_res[k])+";");
			}
			sb.append("\n");
			sb.append(";MAX;");
			for(int k=0;k < meritve.length; k++)
			{
				sb.append(NumberUtils.max(i2_res[k])+";");
				sb.append(NumberUtils.max(i3_res[k])+";");
			}
			sb.append("\n");
			sb.append(";AVG;");
			for(int k=0;k < meritve.length; k++)
			{
				sb.append(mean.evaluate(i2_res[k])+";");
				sb.append(mean.evaluate(i3_res[k])+";");
			}
			sb.append("\n");
			sb.append(";STD;");
			for(int k=0;k < meritve.length; k++)
			{
				sb.append(sd.evaluate(i2_res[k])+";");
				sb.append(sd.evaluate(i3_res[k])+";");
			}
			sb.append("\n");
		}
		
		appendToFile(sb);
	}
	
	private static void appendToFile(StringBuilder sb)
	{
		try {

			String filename= "D:\\Benchmark results\\SoilModel_pop18.csv";
			FileWriter fw = new FileWriter(filename,true); //the true will append the new data
			fw.write(sb.toString());//appends the string to the file .replace(".", ",")
			fw.close();
			sb.setLength(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
