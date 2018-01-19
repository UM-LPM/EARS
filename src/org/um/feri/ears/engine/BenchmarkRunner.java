package org.um.feri.ears.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class BenchmarkRunner {

	public static void main(String args[]) {
		Util.rnd.setSeed(System.currentTimeMillis());

		String destFolder = "D:\\Benchmark results\\test_rating_files2\\";
		String fileName;
		
		//RandomWalkAlgorithm algorithm = new RandomWalkAlgorithm();
		TLBOAlgorithm algorithm = new TLBOAlgorithm();
		RatingRPUOed2 benchmark = new RatingRPUOed2(); // Create banchmark
		
		algorithm.addCustomInfo("submissionAuthor", "author");
		algorithm.addCustomInfo("submissionId", "id");

		ArrayList<Task> tasks = benchmark.getAllTasks();
		int numberOfRuns = benchmark.getNumberOfRuns();
		
		for (Task t: tasks) {
			StringBuilder sb = new StringBuilder();
			sb.append(algorithm.getAlgorithmInfoCSV()+";"+t.getTaskInfoCSV());
			sb.append("\n");
			for (int i = 0; i < numberOfRuns; i++) {
				DoubleSolution result;
				try {
					result = algorithm.execute(t);
				} catch (StopCriteriaException e) {
					e.printStackTrace();
					return;
				}
				sb.append(result.getEval());
				if(i+1 < numberOfRuns)
					sb.append("\n");
				t.resetCounter();
			}
			
			fileName = algorithm.getID().replace("_", " ")+"_"+t.getProblemName()+".txt";
			
			try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFolder+File.separator+fileName)))) {
				bw.write(sb.toString());
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
