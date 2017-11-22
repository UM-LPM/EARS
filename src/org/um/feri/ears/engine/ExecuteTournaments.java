package org.um.feri.ears.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.benchmark.DummyRating;
import org.um.feri.ears.benchmark.RatingBenchmarkBase;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.FutureResult;
import org.um.feri.ears.util.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

/**
 * Reads list.json then executes the benchmark on all submitted algorithms and performs rating.
 *
 */
public class ExecuteTournaments {

	//Only for testing
	static String tournamentDir = "C:"+File.separator+"Users"+File.separator+"Ravby"+File.separator+"Desktop"+File.separator+"platforma"+File.separator+"tournaments";
	static String earsFolder = "C:"+File.separator+"Users"+File.separator+"Ravby"+File.separator+"Desktop"+File.separator+"platforma"+File.separator+"EARS";
	static String earsPath = earsFolder+File.separator+"ears.jar";
	
//	static String tournamentDir;
//	static String earsPath;
//	static String earsFolder;

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
	static final String COMPILE_REPORT_FILE = "compile_report.json";
	static final String LOG_FILE = "EARS_LOG.txt";
	static final String REPORT_FILE = "Report.txt";
	static final String RESULTS_FILE = "results.json";
	
	static String inputBenchmarkId;
	static boolean runOneTournament = false;
	static boolean override = false;

	static long initTime;
	
	static Logger logger = Logger.getLogger(ExecuteTournaments.class.getName());

	public static void main(String[] args) {
			
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("override")) {
				override = true;
				runOneTournament = false;
			}
			else {
				inputBenchmarkId = args[0];
				runOneTournament = true;
			}
			
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("override"))
				override = true;
			inputBenchmarkId = args[1];
			runOneTournament = true;
		}

		initTime = System.currentTimeMillis();
		String benchmarkId, tournamentName;
		boolean benchmarkFilesChanged = false;

		final File f = new File(ExecuteTournaments.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//		earsPath = f.getPath();
//		earsFolder = f.getParent();
//		tournamentDir = new File(earsFolder).getParent()+File.separator+"tournaments";
		
		FileHandler fileTxt;
		SimpleFormatter formatterTxt;
		logger.setLevel(Level.INFO);
		formatterTxt = new SimpleFormatter();
		
		try {
			fileTxt = new FileHandler(earsFolder+File.separator+LOG_FILE,true);
	        fileTxt.setFormatter(formatterTxt);
	        logger.addHandler(fileTxt);
		} catch (SecurityException | IOException e1) {
			e1.printStackTrace();
		}
		
		String tournamentFileLocation = tournamentDir+File.separator+TOURNAMENT_FILE;
		
		try {
			logger.log(Level.INFO,"Reading "+TOURNAMENT_FILE+" at "+tournamentFileLocation);
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(tournamentFileLocation));
			Type type = new TypeToken<List<Tournament>>(){}.getType();
			List<Tournament> tournaments = gson.fromJson(br, type);

			for(Tournament t : tournaments){
							
				tournamentName = t.name;
				final String benchmarkName = t.benchmarks;
				benchmarkId = t.id;
				benchmarkFilesChanged = false;
				//if input benchmark id is provided run only the benchmark with the same id
				if(runOneTournament && !benchmarkId.equals(inputBenchmarkId))
					continue;
				
				String submissionFileLocation = tournamentDir +File.separator+ benchmarkId +File.separator+"submissions"+File.separator+SUBMISSION_FILE;
				logger.log(Level.INFO,"Reading "+SUBMISSION_FILE+" at "+submissionFileLocation);
				File submissionFile = new File(submissionFileLocation);
				
				if(!submissionFile.exists()) {
					logger.log(Level.WARNING,"Submission.json file missing for "+tournamentName);
					continue;
				}
				
				br = new BufferedReader(new FileReader(submissionFile));
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
				
				int threads = Runtime.getRuntime().availableProcessors();
				ExecutorService service = Executors.newFixedThreadPool(threads);

				final String benchmarkResultsDir = tournamentDir +File.separator+ benchmarkId+File.separator+RESULTS_FOLDER; 
				//for each submission copy BenchmarkRunnerTemplate, replace tags, compile and run
				for (Submission sub : newestSubmission.values()) {
					String algorithmFolder = sub.author.replace(' ', '_')+"_"+sub.timestamp;
					String algorithmName = sub.algorithm;
					String algorithmDir = tournamentDir +File.separator+ benchmarkId +File.separator+"submissions"+File.separator+algorithmFolder;		
					
					if(canExecuteBenchmark(algorithmDir) || override) {
						
						Future<Void> future = service.submit(createRunnable(algorithmDir,algorithmName, benchmarkName, benchmarkResultsDir));

				        try {
				        	future.get(5, TimeUnit.HOURS);
				        } catch (TimeoutException e) {
				        	future.cancel(true);
				        	logger.log(Level.SEVERE,"Algorithm "+algorithmName+" exceeded execution time", e);
				        	writeErrorToJson("Algorithm exceeded execution time", algorithmDir);
				        }
						
						benchmarkFilesChanged = true;
					}
				}
				
				service.shutdown();
				service.awaitTermination(newestSubmission.size() * 5, TimeUnit.HOURS);
				
				System.out.println("Shutdown");
				
				if(benchmarkFilesChanged) {
					logger.log(Level.INFO,"Performing rating for tournament "+tournamentName+" with benchmark "+benchmarkName);
					performRating(benchmarkResultsDir, benchmarkName);
				}
				else
					logger.log(Level.INFO,"No file has changed for tournament "+tournamentName);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Exception while running tournaments",e);
		}
	}
	
	//check if the benchmark needs to be executed 
	private static boolean canExecuteBenchmark(String algorithmDir) {
		
		JsonParser parser = new JsonParser();

		try {
			File f = new File(algorithmDir+File.separator+COMPILE_REPORT_FILE);
			if(f.exists()){
				JsonElement jsontree = parser.parse((new FileReader(f)));
				// Get the root JsonObject
				JsonObject je = jsontree.getAsJsonObject();
				
				//if error_list is not empty don't execute benchmark
				if(je.has("error_list")) {
					if(je.get("error_list").isJsonArray()) {
						JsonArray ja = je.getAsJsonArray("error_list");
						if(ja.size() > 0)
							return false;
					}
					else if(je.get("error_list").getAsString().length() > 0) {
						return false;
					}
				}

				if(je.has("benchmarkSuccessful")) {
					boolean benchmarkSuccessful = je.get("benchmarkSuccessful").getAsBoolean();

					//if benchmark was already successful don't run it again
					if(benchmarkSuccessful)
						return false;
					else
						return true;
				}
				else
					return true;

			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Exception in canExecuteBenchmark",e);
		}
		return false;
	}

	private static void writeStatusToJson(boolean successful, String algorithmDir) {

		try {
			File f = new File(algorithmDir+File.separator+COMPILE_REPORT_FILE);
			if(f.exists()){
				JsonParser parser = new JsonParser();
				JsonElement jsontree = parser.parse((new FileReader(f)));
				// Get the root JsonObject
				JsonObject je = jsontree.getAsJsonObject();
				je.addProperty("benchmarkSuccessful", successful);
				
				Gson gson = new Gson();
				String resultingJson = gson.toJson(je);
				FileUtils.writeStringToFile(f, resultingJson);
				
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"Exception in writeStatusToJson",e);
		}
	}
	
	private static void writeErrorToJson(String error, String algorithmDir) {

		try {
			File f = new File(algorithmDir+File.separator+COMPILE_REPORT_FILE);
			if(f.exists()){
				JsonParser parser = new JsonParser();
				JsonElement jsontree = parser.parse((new FileReader(f)));
				// Get the root JsonObject
				JsonObject je = jsontree.getAsJsonObject();
				je.addProperty("benchmarkSuccessful", false);
				
				//append effor if already exists
				if(je.has("error_list")) {
					String currentError = je.get("error_list").getAsString();
					if(currentError != null) {
						currentError += "\n" + error;
						je.addProperty("error_list", currentError);
					}

				} else {

					je.addProperty("error_list", error);
				}
				
				/*if(je.has("error_list")) {
					JsonArray ja = je.getAsJsonArray("error_list");
					ja.add(new JsonPrimitive(error));
				}
				else {
					JsonArray array = new JsonArray();
					array.add(new JsonPrimitive(error));
					je.add("error_list", array);
				}*/
				
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				String resultingJson = gson.toJson(je);
				FileUtils.writeStringToFile(f, resultingJson);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"Exception in writeErrorToJson",e);
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
	
	public static Callable<Void> createRunnable(String algorithmDir, String algorithmName, String benchmarkName, String benchmarkResultsDir) {

		Callable<Void> aRunnable = new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				
				if(!removePackageFromFiles(algorithmDir)) {
					writeErrorToJson("Unable to remove package from file", algorithmDir);
					return null;
				}

				logger.log(Level.INFO,"Compiling files for algorithm: "+algorithmName);
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
					logger.log(Level.SEVERE,"Error while creating template file for algorithm: "+algorithmName, e);
					writeErrorToJson(e.toString(), algorithmDir);
					return null;
				}

				//compile benchmark runner file
				logger.log(Level.INFO,"Running compile command for algorithm: "+algorithmName);
				try {
					String command = "javac "+"-sourcepath "+algorithmDir+" -cp "+earsPath+" "+algorithmDir+File.separator+BENCHMARK_RUNNER_FILENAME+".java";
					//ProcessBuilder pb = new ProcessBuilder("javac","-sourcepath","\""+algorithmDir+"\"","-cp","\""+earsPath+"\"","\""+algorithmDir+File.separator+BENCHMARK_RUNNER_FILENAME+".java\"");
					ProcessBuilder pb = new ProcessBuilder("javac","-sourcepath",algorithmDir,"-cp",earsPath,BENCHMARK_RUNNER_FILENAME+".java");
					pb.directory(new File(algorithmDir));
					//System.out.println(command);

					//System.out.println(pb.command());
					
					startProcess(pb, algorithmDir);

				} catch (Exception e) {
					//e.printStackTrace();
					logger.log(Level.SEVERE,"Error while compiling benchmark runner file for algorithm: "+algorithmName, e);
					writeErrorToJson(e.toString(), algorithmDir);
					return null;
				}

				logger.log(Level.INFO,"Runing benchmark for algorithm: "+algorithmName);
				//run the compiled file
				try {
					ProcessBuilder pb;
					if(SystemUtils.IS_OS_WINDOWS){
						pb = new ProcessBuilder("java","-cp","\""+earsPath+File.pathSeparator+algorithmDir+"\\\\\"",BENCHMARK_RUNNER_FILENAME);
					}
					else {
						pb = new ProcessBuilder("java","-cp",earsPath+File.pathSeparator+algorithmDir+"/",BENCHMARK_RUNNER_FILENAME);
					}

					startProcess(pb, algorithmDir);
				} catch (Exception e) {
					logger.log(Level.SEVERE,"Error while runing benchmark for algorithm: "+algorithmName, e);
					writeErrorToJson(e.toString(), algorithmDir);
					return null;
				}
				writeStatusToJson(true, algorithmDir);
		    	
		    	return null;
			}

	    };
	    return aRunnable;
	}

	private static boolean removePackageFromFiles(String algorithmDir) {

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
						logger.log(Level.SEVERE,"Error while removing package files in directory: "+algorithmDir, e);
						return false;
					}

				}
			}
		}
		return true;
	}

	private static void startProcess(ProcessBuilder pb, String algorithmDir) throws Exception {
		Process p = pb.start();
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		String s = null;
		StringBuilder sbError = new StringBuilder();
		// read any errors from the attempted command
		while ((s = stdError.readLine()) != null) {
			sbError.append(s);
			sbError.append("\n");
		}
		s = sbError.toString();
		if(s.length() > 0) {
			throw new Exception(s);
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
			logger.log(Level.SEVERE,"Exception creating class from name: "+BENCHMARK_PACKAGE+"."+benchmarkName,e);
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
				int index = fileName.lastIndexOf("_");
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
		logger.log(Level.INFO,"Total execution time: "+estimatedTime + "s");
		ArrayList<Player> list = new ArrayList<Player>();
		list.addAll(ra.calculteRatings()); //new ranks
		String results = ra.getPlayersJson();
		StringBuilder sb = new StringBuilder();
		for (Player p: list) {
			System.out.println(p); //print ranks
			sb.append(p.toString());
			sb.append("\n");
		}
		Util.writeToFile(benchmarkResultsDir+File.separator+RESULTS_FILE, results);
		Util.writeToFile(benchmarkResultsDir+File.separator+REPORT_FILE, sb.toString());
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