package org.um.feri.ears.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class DummyMOAlgorithm<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

	HashMap<String, ParetoSolution<Type>[]> results;
	HashMap<String, Integer> positions; //stores the position of the current result of the current problem
	
	public DummyMOAlgorithm(String name)
	{
		ai = new AlgorithmInfo(name,"",name, name);
		fillResults(name);
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		
	}

	@Override
	public ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException {
		String problemName = taskProblem.getProblemFileName();
		
		if(results.containsKey(problemName.toLowerCase()))
		{
			ParetoSolution<Type> val = getNextValue(problemName.toLowerCase());
			return val;
		}
		
		return new ParetoSolution<Type>();
	}

	private ParetoSolution<Type> getNextValue(String problemName) {
		
		ParetoSolution<Type>[] problemReults = results.get(problemName);
		int index = positions.get(problemName);
		positions.put(problemName, positions.get(problemName) + 1);
		return problemReults[index];
	}
	
	private void fillResults(String name) {
		
		results = new HashMap<String, ParetoSolution<Type>[]>();
		positions = new HashMap<String, Integer>();
		
		File folder = new File("D:/Pareto/");
		File[] listOfFiles = folder.listFiles();
		
		String pathName = name+"_";
		String problemName, fileName;
		String[] value;
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName().toLowerCase();
				if(fileName.toLowerCase().contains(pathName.toLowerCase()))
				{
					problemName = fileName.substring(fileName.indexOf(pathName)+pathName.length(),fileName.length()-4);
					ParetoSolution[] resultArray = new ParetoSolution[10000];
					int index = 0;
					try(BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
						String line = br.readLine();
						
						while (line != null)
						{
							if(line.compareTo("#") == 0)
							{
								line = br.readLine();
								if(line != null)
								{
									ParetoSolution<Type> ps = new ParetoSolution<Type>();
									while (line != null && line.compareTo("#") != 0)
									{
										value = line.split(" ");
										MOSolutionBase<Type> mos = new MOSolutionBase<Type>(value.length);
										for(int i=0;i<value.length;i++)
										{
											mos.setObjective(i, Double.parseDouble(value[i]));
										}
										ps.add(mos);
										line = br.readLine();
									}
									ps.setCapacity(ps.size());
									resultArray[index] = ps;
									index++;
								}
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

}
