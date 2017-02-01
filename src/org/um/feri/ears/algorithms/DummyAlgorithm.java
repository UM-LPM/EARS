package org.um.feri.ears.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.um.feri.ears.problems.DummySolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Task;

public class DummyAlgorithm extends Algorithm{
	
	HashMap<String, double[]> results;
	HashMap<String, Integer> positions; //stores the position of the current result of the current problem
	
	public DummyAlgorithm(String name)
	{
		ai = new AlgorithmInfo(name,"",name, name);
		fillResults(name);
		
	}

	private void fillResults(String name) {
		
		results = new HashMap<String, double[]>();
		positions = new HashMap<String, Integer>();
		
		File folder = new File("D:/Results/");
		File[] listOfFiles = folder.listFiles();
		
		String pathName = name+"_";
		String problemName, fileName, value;
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName().toLowerCase();
				if(fileName.toLowerCase().contains(pathName.toLowerCase()))
				{
					problemName = fileName.substring(fileName.indexOf(pathName)+pathName.length()+1,fileName.length()-4);
					double[] resultArray = new double[10000];
					int index = 0;
					try(BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
						String line = br.readLine();

						while (line != null && index < resultArray.length) {
							
							resultArray[index] = Double.parseDouble(line);
							line = br.readLine();
							index++;
						}
						results.put(problemName.toLowerCase(), resultArray);
						positions.put(problemName.toLowerCase(), 0);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}
	}

	@Override
	public DoubleSolution execute(Task taskProblem) {
		String problemName = taskProblem.getProblemName();
		
		if(results.containsKey(problemName.toLowerCase()))
		{
			double val = getNextValue(problemName.toLowerCase());
			DummySolution solution = new DummySolution(val);
			return solution;
		}
		
		return new DummySolution(Double.MAX_VALUE);
	}

	private double getNextValue(String problemName) {
		
		double[] problemReults = results.get(problemName);
		int index = positions.get(problemName);
		positions.put(problemName, positions.get(problemName) + 1);
		return problemReults[index];
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}

}
