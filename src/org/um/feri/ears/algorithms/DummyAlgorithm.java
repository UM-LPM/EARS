package org.um.feri.ears.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.DummySolution;
import org.um.feri.ears.problems.Task;

public class DummyAlgorithm extends Algorithm{
	
	HashMap<String, double[]> results;
	HashMap<String, Integer> positions; //stores the position of the current result of the current problem
	String filesDir = "D:/Results/";
	
	public DummyAlgorithm(String name, String filesDir)
	{
		ai = new AlgorithmInfo(name);
		
		this.filesDir = filesDir;
		ai = new AlgorithmInfo(name,"",name, name);
		results = new HashMap<String, double[]>();
		positions = new HashMap<String, Integer>();
		fillResults(name);
		
	}

	public DummyAlgorithm(String name) {
		ai = new AlgorithmInfo(name,"",name, name);
		results = new HashMap<String, double[]>();
		positions = new HashMap<String, Integer>();
	}

	private void fillResults(String name) {
		
		File folder = new File(filesDir);
		File[] listOfFiles = folder.listFiles();
		
		String problemName, fileName, value;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName();
				if(fileName.toLowerCase().indexOf(name.toLowerCase()+"_") == 0)
				{
					problemName = fileName.substring(name.length()+1,fileName.length()-4);
					double[] resultArray = new double[10000];
					int index = 0;
					try(BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
						String line = br.readLine();

						while (line != null && index < resultArray.length) {
							//First line may contain metadata
							if(index == 0 && line.indexOf(';') > 0) {
								readAlgorithmInfo(line);
								line = br.readLine();
								continue;
							}
							resultArray[index] = Double.parseDouble(line);
							line = br.readLine();
							index++;
							if(index >= 10000) {
								System.err.println("The file "+fileName+" has more than 10000 results. Skipping to end of file.");
								break;
							}
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
	
	private void readAlgorithmInfo(String metadata) {
		
		String[] allInfo = metadata.split(";");
		for(String inf: allInfo) {
			if(inf.contains("algorithm name")) {
				String[] subInfo = inf.split(",");
				for(String info : subInfo) {
					String[] split = info.split(":");
					if(split.length != 2)
						continue;
					addCustomInfo(split[0], split[1]);	
				}
			}
		}
	}

	public void addProblemresults(String problemName, double[] resultArray){
		results.put(problemName.toLowerCase(), resultArray);
		positions.put(problemName.toLowerCase(), 0);
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
