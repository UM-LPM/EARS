package org.um.feri.ears.experiments;

import org.um.feri.ears.algorithms.es.ES1cNsAlgorithm;
import org.um.feri.ears.algorithms.es.ES1pNsAlgorithm;
import org.um.feri.ears.algorithms.pso.PSO;
import org.um.feri.ears.algorithms.pso.PSOOmega;
import org.um.feri.ears.algorithms.tlbo.TLBOAlgorithm;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.misc.SoilModelProblem;
import org.um.feri.ears.util.Util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.algorithms.de.DEAlgorithm;;

public class SoilModelTest {

	public static void main(String[] args) {
		
		//TODO število dimenzij se lahko spreminja
		// ali je kakšna povezava med število plastmi in število dimenzij (x)
		// število plasti hraniti v x?
		// dimenzija = število plasti * 2 + 1 ?
		// velikost polja xx in y1?
		
		Util.rnd.setSeed(System.currentTimeMillis());
		
		//PSOOmega es = new PSOOmega(10, 0.7, 2, 2);
		DEAlgorithm es = new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin,50);
		TLBOAlgorithm tlbo = new TLBOAlgorithm();
		
		SoilModelProblem emp = new SoilModelProblem(3,0,2,"meritev1");  // numberOfDimensions, numberOfConstraints, layers, filename
		Task task = new Task(EnumStopCriteria.EVALUATIONS, 20000, 0.0001, emp);
		
		
		try {
			long initTime = System.currentTimeMillis();
			DoubleSolution best = es.run(task);
        	long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        	System.out.println("Execution time: "+estimatedTime + "s");
			//System.out.println(Arrays.toString(best.getVariables()));
			System.out.println(best.toString());
		} catch (StopCriteriaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 DecimalFormat df0 = new DecimalFormat("####.##");

		
		double x[] ={
		525.6,
		0.83,
		112.5};
		
		
		double x2[] = {
		525.6,
		0.4,
		525.6,
		0.43,
		112.5};
		
		double x3[] = {
		525.6,
		0.2,
		525.6,
		0.2,
		525.6,
		0.43,
		112.5};
		
		double x4[] = {
		525.6,
		0.1,
		525.6,
		0.1,
		525.6,
		0.1,
		525.6,
		0.53,
		112.5};
		
		double x5[] = {
		525.6,
		0.1,
		525.6,
		0.1,
		525.6,
		0.1,
		525.6,
		0.1,
		525.6,
		0.43,
		112.5};
		
		double x6[] = {
		196.9,
	    1.7,
	    351.7,
	    8.4,
	    185.2};
		
		double optimal[] = {
		29.6,
	    11.4,
	    3.0,
	    10.8,
	    8.0};
		
		double current[] = {
		29.1,
	    10.9,
	    2.5,
	    10.3,
	    7.5};
		
		Double[] inputBoxed = ArrayUtils.toObject(x);
		List<Double> list = Arrays.asList(inputBoxed);
		
		double fit = emp.eval(list);
		
		//System.out.println(fit);
		
		/*
		double step = 0.05;
		double max = 0.5;
		int index = 0;
		boolean run = true;
		double fitness;
		StringBuilder sb = new StringBuilder();
		int count = 0;
		while(run)
		{
			count++;
			if(count > 10000)
			{
				appendToFile(sb);
				count = 0;
			}
			fitness = emp.eval(current);
			sb.append(fitness+";");
			for(int i = 0; i< current.length; i++)
				sb.append(df0.format(current[i]).replace(",", ".")+"\t");
			sb.append("\n");
			//System.out.println(Util.arrayToString(current));
			
			current[index] += step;
			index = 0;

			while(current[index] > (optimal[index] + max))
			{
				current[index] -= max*2;
				index++;
				if(index >= current.length)
				{
					run = false;
					break;
				}
			}
		}
		
		appendToFile(sb);*/

		
	}
	
	private static void appendToFile(StringBuilder sb)
	{
		try {

			String filename= "D:\\Benchmark results\\fitness_combinations.txt";
			FileWriter fw = new FileWriter(filename,true); //the true will append the new data
			fw.write(sb.toString());//appends the string to the file .replace(".", ",")
			fw.close();
			sb.setLength(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
