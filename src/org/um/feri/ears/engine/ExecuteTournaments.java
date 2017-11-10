package org.um.feri.ears.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SystemUtils;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.benchmark.AlgorithmEvalResult;
import org.um.feri.ears.benchmark.DummyRating;
import org.um.feri.ears.benchmark.RatingBenchmarkBase;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Reads list.json then executes the benchmark on all submitted algorithms and performs rating.
 *
 */
public class ExecuteTournaments {

	//Only for testing
//	static String tournamentDir = "C:"+File.separator+"Users"+File.separator+"Ravby"+File.separator+"Desktop"+File.separator+"platforma"+File.separator+"tournaments";
//	static String earsFolder = "C:"+File.separator+"Users"+File.separator+"Ravby"+File.separator+"Desktop"+File.separator+"platforma"+File.separator+"EARS";
//	static String earsPath = earsFolder+File.separator+"ears.jar";
	
	static String tournamentDir;
	static String earsPath;
	static String earsFolder;

	static final String DEST_FOLDER = "@DEST_FOLDER";
	static final String ALGORITHM_NAME = "@ALGORITHM_NAME";
	static final String ALGORITHM_CONSTRUCTOR = "@ALGORITHM_CONSTRUCTOR";
	static final String BENCHMARK_NAME = "@BENCHMARK_NAME";
	static final String TEMPLATE_FILENAME = "BenchmarkRunnerTemplate";
	static final String BENCHMARK_RUNNER_FILENAME = "BenchmarkRunner";
	static final String TOURNAMENT_FILE = "list.json";
	static final String SUBMISSION_FILE = "submission_list.json";
	static final String RESULTS_FOLDER = "benchmark_result_files";
	static final String BENCHMARK_PACKAGE = "org.um.feri.ears.benchmark";
	
	static boolean override = false;

	static long initTime;

	public static void main(String[] args) {
		
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("override"))
				override = true;
		}

		initTime = System.currentTimeMillis();
		String benchmarkName, algorithmName, benchmarkId, tournamentName;

		final File f = new File(ExecuteTournaments.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		earsPath = f.getPath();
		earsFolder = f.getParent();
		tournamentDir = new File(earsFolder).getParent()+File.separator+"tournaments";
		
		String tournamentFileLocation = tournamentDir+File.separator+TOURNAMENT_FILE;
		
		try {
			System.out.println("Reading "+TOURNAMENT_FILE+" at "+tournamentFileLocation);
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(tournamentFileLocation));
			Type type = new TypeToken<List<Tournament>>(){}.getType();
			List<Tournament> tournaments = gson.fromJson(br, type);

			for(Tournament t : tournaments){
				tournamentName = t.name;
				benchmarkName = t.benchmarks;
				benchmarkId = t.id;
				String submissionFileLocation = tournamentDir +File.separator+ benchmarkId +File.separator+"submissions"+File.separator+SUBMISSION_FILE;
				System.out.println("Reading "+SUBMISSION_FILE+" at "+submissionFileLocation);
				br = new BufferedReader(new FileReader(submissionFileLocation));
				type = new TypeToken<List<Submission>>(){}.getType();
				List<Submission> submissions = gson.fromJson(br, type);

				HashMap<String,Submission> newestSubmission = new HashMap<String,Submission>();

				//get only the newest submission for each author
				for(Submission sub : submissions) {
					String author = sub.author;
					Timestamp ts = new Timestamp(sub.timestamp);
					Submission current = newestSubmission.get(author);
					if(current == null) {
						newestSubmission.put(author, sub);
					}
					else if(ts.after(new Timestamp(current.timestamp))) {
						newestSubmission.put(author, sub);
					}
				}

				String benchmarkResultsDir = tournamentDir +File.separator+ benchmarkId+File.separator+RESULTS_FOLDER; 
				//for each submission copy BenchmarkRunnerTemplate, replace tags, compile and run
				for (Submission sub : newestSubmission.values()) {
					String algorithmFolder = sub.author.replace(' ', '_')+"_"+sub.timestamp;
					algorithmName = sub.algorithm;
					String algorithmDir = tournamentDir +File.separator+ benchmarkId +File.separator+"submissions"+File.separator+algorithmFolder;
					if(!hasClassFiles(algorithmDir) || override)
						runBenchmark(algorithmDir,algorithmName, benchmarkName, benchmarkResultsDir);
				}
				System.out.println("Performing rating for tournament "+tournamentName+" with benchmark "+benchmarkName);
				performRating(benchmarkResultsDir, benchmarkName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean hasClassFiles(String algorithmDir) {
		
		String fileName;
		File folder = new File(algorithmDir);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName();
				if(getFileExtension(fileName).equals("class")) {
					return true;
				}
			}
		}
		return false;
	}

	private static void runBenchmark(String algorithmDir, String algorithmName, String benchmarkName, String benchmarkResultsDir) {

		removePackageFromFiles(algorithmDir);

		System.out.println("Compiling files for algorithm: "+algorithmName);
		String templateFile = earsFolder+File.separator+TEMPLATE_FILENAME+".txt";
		String fileData="", line;
		try(BufferedReader br = new BufferedReader(new FileReader(templateFile))) {

			while ((line = br.readLine()) != null) {
				fileData += line;
				fileData +="\n";
			}
			//replace all tags in template file
			if(SystemUtils.IS_OS_WINDOWS)
				fileData = fileData.replaceAll(DEST_FOLDER, benchmarkResultsDir.replaceAll("\\\\", "\\\\\\\\\\\\\\\\"));
			else
				fileData = fileData.replaceAll(DEST_FOLDER, benchmarkResultsDir);
			
			fileData = fileData.replaceAll(ALGORITHM_NAME, algorithmName);
			fileData = fileData.replaceAll(ALGORITHM_CONSTRUCTOR, algorithmName+"()");
			fileData = fileData.replaceAll(BENCHMARK_NAME, benchmarkName);

			//save template file to algorithm destination
			FileWriter fw = new FileWriter(algorithmDir+File.separator+BENCHMARK_RUNNER_FILENAME+".java");
			fw.write(fileData);
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		//compile benchmark runner file
		try {
			String command = "javac "+"-sourcepath "+algorithmDir+" -cp "+earsPath+" "+algorithmDir+File.separator+BENCHMARK_RUNNER_FILENAME+".java";
			//ProcessBuilder pb = new ProcessBuilder("javac","-sourcepath","\""+algorithmDir+"\"","-cp","\""+earsPath+"\"","\""+algorithmDir+File.separator+BENCHMARK_RUNNER_FILENAME+".java\"");
			ProcessBuilder pb = new ProcessBuilder("javac","-sourcepath",algorithmDir,"-cp",earsPath,BENCHMARK_RUNNER_FILENAME+".java");
			pb.directory(new File(algorithmDir));
			//System.out.println(command);

			//System.out.println(pb.command());
			
			startProcess(pb);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Runing benchmark for algorithm: "+algorithmName);
		//run the compiled file
		try {
			ProcessBuilder pb;
			if(SystemUtils.IS_OS_WINDOWS){
				pb = new ProcessBuilder("java","-cp","\""+earsPath+File.pathSeparator+algorithmDir+"\\\\\"",BENCHMARK_RUNNER_FILENAME);
			}
			else {
				pb = new ProcessBuilder("java","-cp",earsPath+File.pathSeparator+algorithmDir+"/",BENCHMARK_RUNNER_FILENAME);
			}

			startProcess(pb);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private static void removePackageFromFiles(String algorithmDir) {

		File folder = new File(algorithmDir);
		File[] listOfFiles = folder.listFiles();
		String fileName, line, fileData;
		boolean hasPackage = false;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName();

				if(getFileExtension(fileName).equals("java")) {

					try(BufferedReader br = new BufferedReader(new FileReader(file))) {
						hasPackage = false;
						fileData = "";
						while ((line = br.readLine()) != null) {
							if(line.contains("package ")) {
								hasPackage = true;
								continue;
							}
							fileData += line;
							fileData +="\n";
						}

						if(hasPackage) {
							FileWriter fw = new FileWriter(file, false);
							fw.write(fileData);
							fw.close();
						}

					} catch (Exception e) {
						e.printStackTrace();
						return;
					}

				}
			}
		}
	}

	private static void startProcess(ProcessBuilder pb) throws IOException {
		Process p = pb.start();
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		String s = null;
		// read any errors from the attempted command
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}
	}

	private static void performRating(String benchmarkResultsDir, String benchmarkName) {

		Util.rnd.setSeed(System.currentTimeMillis());

		List<String> problems = new ArrayList<String>();
		List<String> algorithms = new ArrayList<String>();

		int numberOfRuns = 15; //Default
		
		//Create benchmark object to get number of runs
		try {
			Class<?> clazz = Class.forName(BENCHMARK_PACKAGE+"."+benchmarkName);
			Object benchmark = clazz.newInstance();
			numberOfRuns = ((RatingBenchmarkBase) benchmark).getNumberOfRuns();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		ArrayList<DummyAlgorithm> players = new ArrayList<DummyAlgorithm>();
		DummyRating dr = new DummyRating(0.000001); //Create banchmark
		dr.setDisplayRatingIntervalChart(false);
		RatingBenchmarkBase.printSingleRunDuration = false;

		//parse algorithm and problem names
		String algorithmName, problemName, fileName;

		File folder = new File(benchmarkResultsDir);
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
			players.add(new DummyAlgorithm(name, benchmarkResultsDir));
		}

		for (DummyAlgorithm al:players) {
			ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
			dr.registerAlgorithm(al);
		}
		BankOfResults ba = new BankOfResults();

		dr.run(ra, ba, numberOfRuns); //repeat competition 50X
		long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
		System.out.println("Total execution time: "+estimatedTime + "s");
		ArrayList<Player> list = new ArrayList<Player>();
		list.addAll(ra.calculteRatings()); //new ranks
		String results = ra.getPlayersJson();
		for (Player p: list) {
			System.out.println(p); //print ranks
		}        
		Util.writeToFile(benchmarkResultsDir+File.separator+"results.json", results);
	}

	private static String getFileExtension(String fileName) {

		String exstentsion = "";
		int i = fileName.lastIndexOf('.');
		if (i >= 0) 
			exstentsion = fileName.substring(i+1); 
		return exstentsion;
	}

	public class Tournament {
		public String id;
		public String name;
		public Long timestamp;
		public Long ends;
		public String benchmarks;
		public String path;
		public String description;
		public String password;
	}

	public class Submission {
		public String id;
		public String author;
		public Long timestamp;
		public String algorithm;
		public String submissionUrl;
	}

}