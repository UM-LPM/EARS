package org.um.feri.ears.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.benchmark.DummyRating;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

/**
 * Create rating leaderboard from multiple file. The name of each file must contain algorithm name and problem name (algorithm_problem.txt). Each line in the file contains a single result.
 *
 */
public class RunBenchmarkFromFiles {

	public static void main(String[] args) {
		
        Util.rnd.setSeed(System.currentTimeMillis());
        		

		String srcFolder = "D:\\Benchmark results\\test_rating_files2";
		String destFolder = "D:\\Benchmark results\\test_rating_files2";
		
		List<String> problems = new ArrayList<String>();
		List<String> algorithms = new ArrayList<String>();
		
		//TODO number of runs?
		int numberOfsolutions = 15;
		
        ArrayList<DummyAlgorithm> players = new ArrayList<DummyAlgorithm>();
        DummyRating dr = new DummyRating(0.000001); //Create banchmark
        dr.setDisplayRatingIntervalChart(false);
        
        //parse algorithm and problem names
        String algorithmName, problemName, fileName;
        
        File folder = new File(srcFolder);
		File[] listOfFiles = folder.listFiles();
        
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName();
				int index = fileName.indexOf("_");
				if(index > 0)
				{
					algorithmName = fileName.substring(0, index);
					problemName = fileName.substring(algorithmName.length()+1,fileName.lastIndexOf('.'));
					problems.add(problemName);
					algorithms.add(algorithmName);
				}
			}
		}
        
        ResultArena ra = new ResultArena(100);

        //get distinct problem names
		problems = problems.stream().distinct().collect(Collectors.toList());
		//get distinct algorithm names
		algorithms = algorithms.stream().distinct().collect(Collectors.toList());
		
		//add problems to benchmark
		for(String name : problems){
			dr.addDummyTask(name);
		}
		//add problems to benchmark
		for(String name : algorithms){
			players.add(new DummyAlgorithm(name, srcFolder));
		}
        
        for (DummyAlgorithm al:players) {
        	ra.addPlayer(al, al.getID());
        	dr.registerAlgorithm(al);
        }
        BankOfResults ba = new BankOfResults();
        long initTime = System.currentTimeMillis();
        dr.run(ra, ba, numberOfsolutions); //repeat competition 50X
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        System.out.println("Benchmark execution time: "+estimatedTime + "s");
        ArrayList<Player> list = ra.getPlayers();
        StringBuilder sb = new StringBuilder();
        for (Player p: list) {
        	System.out.println(p); //print ranks
        	sb.append(p.toString());
        	sb.append("\n");
        }
        //TODO create JSON file
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFolder+"\\results.txt")))) {
			bw.write(sb.toString());
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
