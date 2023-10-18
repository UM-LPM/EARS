package org.um.feri.ears.engine;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.benchmark.DummyBenchmark;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

/**
 * Reads list.json then executes the benchmark on all submitted algorithms and performs rating.
 */
public class ExecuteTournaments {

    static String tournamentDir;
    static String earsPath;
    static String earsFolder;

    static final String DEST_FOLDER = "@DEST_FOLDER";
    static final String ALGORITHM_NAME = "@ALGORITHM_NAME";
    static final String ALGORITHM_CONSTRUCTOR = "@ALGORITHM_CONSTRUCTOR";
    static final String BENCHMARK_NAME = "@BENCHMARK_NAME";
    static final String SUBMISSION_AUTHOR = "@SUBMISSION_AUTHOR";
    static final String SUBMISSION_ID = "@SUBMISSION_ID";
    static final String TEMPLATE_FILENAME = "BenchmarkRunnerTemplate"; //TODO read from resource?
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
    static boolean runOneTournament = false; //if id of the tournament is provided only run that tournament and skip the rest
    static boolean override = false; //run tournament even if results already exist

    static long initTime;

    static Logger logger = Logger.getLogger(ExecuteTournaments.class.getName());

    public static void main(String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("override")) {
                override = true;
                runOneTournament = false;
            } else {
                inputBenchmarkId = args[0];
                runOneTournament = true;
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("override"))
                override = true;
            inputBenchmarkId = args[1];
            runOneTournament = true;
        }

        initTime = System.currentTimeMillis();
        String benchmarkId, tournamentName;
        boolean benchmarkFilesChanged = false;

        final File f = new File(ExecuteTournaments.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        earsPath = f.getPath();
        earsFolder = f.getParent();
        tournamentDir = new File(earsFolder).getParent() + File.separator + "tournaments";

        FileHandler fileTxt;
        SimpleFormatter formatterTxt;
        logger.setLevel(Level.INFO);
        formatterTxt = new SimpleFormatter();

        try {
            fileTxt = new FileHandler(earsFolder + File.separator + LOG_FILE, true);
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }

        String tournamentFileLocation = tournamentDir + File.separator + TOURNAMENT_FILE;

        try {
            logger.log(Level.INFO, "Reading " + TOURNAMENT_FILE + " at " + tournamentFileLocation);
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(tournamentFileLocation));
            Type type = new TypeToken<List<Tournament>>() {
            }.getType();
            List<Tournament> tournaments = gson.fromJson(br, type);

            for (Tournament t : tournaments) {

                tournamentName = t.name;
                final String benchmarkName = t.benchmarks;
                benchmarkId = t.id;
                benchmarkFilesChanged = false;
                //if input benchmark id is provided run only the benchmark with the same id
                if (runOneTournament && !benchmarkId.equals(inputBenchmarkId))
                    continue;

                String submissionFileLocation = tournamentDir + File.separator + benchmarkId + File.separator + "submissions" + File.separator + SUBMISSION_FILE;
                logger.log(Level.INFO, "Reading " + SUBMISSION_FILE + " at " + submissionFileLocation);
                File submissionFile = new File(submissionFileLocation);

                if (!submissionFile.exists()) {
                    logger.log(Level.WARNING, "Submission.json file missing for " + tournamentName);
                    continue;
                }

                HashMap<String, Submission> newestSubmission = getNewestSubmissions(benchmarkId);

                int threads = Runtime.getRuntime().availableProcessors();
                ExecutorService service = Executors.newFixedThreadPool(threads);

                final String benchmarkResultsDir = tournamentDir + File.separator + benchmarkId + File.separator + RESULTS_FOLDER;
                //for each submission copy BenchmarkRunnerTemplate, replace tags, compile and run
                for (Submission sub : newestSubmission.values()) {
                    String algorithmFolder = sub.author.replace(' ', '_') + "_" + sub.timestamp;
                    String algorithmName = sub.algorithm;
                    String author = sub.author;
                    String id = sub.id;
                    String algorithmDir = tournamentDir + File.separator + benchmarkId + File.separator + "submissions" + File.separator + algorithmFolder;

                    if (canExecuteBenchmark(algorithmDir) || override) {
                        benchmarkFilesChanged = true;
                        Future<Void> future = service.submit(createRunnable(algorithmDir, algorithmName, benchmarkName, benchmarkResultsDir, author, id));

				        /*try {
				        	future.get(5, TimeUnit.HOURS);
				        } catch (TimeoutException e) {
				        	future.cancel(true);
				        	logger.log(Level.SEVERE,"Algorithm "+algorithmName+" exceeded execution time", e);
				        	writeErrorToJson(algorithmDir, "Algorithm exceeded execution time");
				        }*/
                    }
                }

                service.shutdown();
                service.awaitTermination(newestSubmission.size() * 5, TimeUnit.HOURS);

                System.out.println("Shutdown");

                if (benchmarkFilesChanged) {
                    logger.log(Level.INFO, "Performing rating for tournament " + tournamentName + " with benchmark " + benchmarkName);
                    performRating(benchmarkResultsDir, benchmarkName, benchmarkId);
                } else
                    logger.log(Level.INFO, "No file has changed for tournament " + tournamentName);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception while running tournaments", e);
        }
    }

    /**
     * Go through all of the submissions and return only the newest submission for each author
     * @param benchmarkId ID of the benchmark
     * @return list of newest submissions for each author
     */
    private static HashMap<String, Submission> getNewestSubmissions(String benchmarkId) {

        String submissionFileLocation = tournamentDir + File.separator + benchmarkId + File.separator + "submissions" + File.separator + SUBMISSION_FILE;
        logger.log(Level.INFO, "Reading " + SUBMISSION_FILE + " at " + submissionFileLocation);
        File submissionFile = new File(submissionFileLocation);

        Gson gson = new Gson();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(submissionFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Submission.json file missing for benchamrk wit ID: " + benchmarkId);
            return null;
        }
        Type type = new TypeToken<List<Submission>>() {
        }.getType();
        List<Submission> submissions = gson.fromJson(br, type);

        HashMap<String, Submission> newestSubmission = new HashMap<>();

        /* get through all of the submissions and check only the newest submission for each author */
        for (Submission sub : submissions) {
            String author = sub.author;
            Timestamp ts = new Timestamp(sub.timestamp);
            Submission current = newestSubmission.get(author);
            if (current == null) {
                newestSubmission.put(author, sub);
            } else if (ts.after(new Timestamp(current.timestamp))) {
                newestSubmission.put(author, sub);
            }
        }
        return newestSubmission;
    }

    //check if the benchmark needs to be executed
    private static boolean canExecuteBenchmark(String algorithmDir) {

        JsonParser parser = new JsonParser();

        try {
            File f = new File(algorithmDir + File.separator + COMPILE_REPORT_FILE);
            if (f.exists()) {
                JsonElement jsonElement = parser.parse((new FileReader(f)));
                // Get the root JsonObject
                JsonObject je = jsonElement.getAsJsonObject();

                //if error_list is not empty don't execute benchmark
                if (je.has("error_list")) {
                    if (je.get("error_list").isJsonArray()) {
                        JsonArray ja = je.getAsJsonArray("error_list");
                        if (ja.size() > 0)
                            return false;
                    } else if (je.get("error_list").getAsString().length() > 0) {
                        return false;
                    }
                }

                if (je.has("benchmarkSuccessful")) {
                    boolean benchmarkSuccessful = je.get("benchmarkSuccessful").getAsBoolean();

                    //if benchmark was already successful don't run it again
                    if (benchmarkSuccessful)
                        return false;
                    else
                        return true;
                } else
                    return true;

            } else {
                logger.log(Level.SEVERE, "File " + COMPILE_REPORT_FILE + " not found in " + algorithmDir);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in canExecuteBenchmark", e);
        }
        return false;
    }

    private static void writeStatusToJson(boolean successful, String algorithmDir) {

        try {
            File f = new File(algorithmDir + File.separator + COMPILE_REPORT_FILE);
            if (f.exists()) {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse((new FileReader(f)));
                // Get the root JsonObject
                JsonObject je = jsonElement.getAsJsonObject();
                je.addProperty("benchmarkSuccessful", successful);

                Gson gson = new Gson();
                String resultingJson = gson.toJson(je);
                FileUtils.writeStringToFile(f, resultingJson);

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in writeStatusToJson", e);
        }
    }

    private static void writeErrorToJson(String algorithmDir, String error) {

        try {
            File f = new File(algorithmDir + File.separator + COMPILE_REPORT_FILE);
            if (f.exists()) {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse((new FileReader(f)));
                // Get the root JsonObject
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if(error.length() > 0)
                    jsonObject.addProperty("benchmarkSuccessful", false);

                //append error if already exists
                if (jsonObject.has("error_list")) {
                    String currentError = jsonObject.get("error_list").getAsString();
                    if (currentError != null) {
                        currentError += "\n" + error;
                        jsonObject.addProperty("error_list", currentError);
                    }

                } else {
                    jsonObject.addProperty("error_list", error);
                }

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String resultingJson = gson.toJson(jsonObject);
                FileUtils.writeStringToFile(f, resultingJson);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in writeErrorToJson", e);
        }
    }

    private static boolean hasClassFiles(String algorithmDir) {

        String fileName;
        File folder = new File(algorithmDir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();
                if (getFileExtension(fileName).equals("class")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Callable<Void> createRunnable(String algorithmDir, String algorithmName, String benchmarkName, String benchmarkResultsDir, String author, String id) {

        return () -> {

            if (!removePackageFromFiles(algorithmDir)) {
                writeErrorToJson(algorithmDir, "Unable to remove package from file");
                return null;
            }

            logger.log(Level.INFO, "Compiling files for algorithm: " + algorithmName);
            String templateFile = earsFolder + File.separator + TEMPLATE_FILENAME + ".txt";
            String fileData = "", line;
            try (BufferedReader br = new BufferedReader(new FileReader(templateFile))) {

                while ((line = br.readLine()) != null) {
                    fileData += line;
                    fileData += "\n";
                }
                //replace all tags in template file
                if (SystemUtils.IS_OS_WINDOWS)
                    fileData = fileData.replaceAll(DEST_FOLDER, benchmarkResultsDir.replaceAll("\\\\", "\\\\\\\\\\\\\\\\"));
                else
                    fileData = fileData.replaceAll(DEST_FOLDER, benchmarkResultsDir);

                fileData = fileData.replaceAll(ALGORITHM_NAME, algorithmName);
                fileData = fileData.replaceAll(ALGORITHM_CONSTRUCTOR, algorithmName + "()");
                fileData = fileData.replaceAll(BENCHMARK_NAME, benchmarkName);

                fileData = fileData.replaceAll(SUBMISSION_AUTHOR, author);
                fileData = fileData.replaceAll(SUBMISSION_ID, id);

                //save template file to algorithm destination
                FileWriter fw = new FileWriter(algorithmDir + File.separator + BENCHMARK_RUNNER_FILENAME + ".java");
                fw.write(fileData);
                fw.close();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while creating template file for algorithm: " + algorithmName, e);
                writeErrorToJson(algorithmDir, e.toString());
                return null;
            }

            //compile benchmark runner file
            logger.log(Level.INFO, "Running compile command for algorithm: " + algorithmName);
            try {
                String command = "javac " + "-sourcepath " + algorithmDir + " -cp " + earsPath + " " + algorithmDir + File.separator + BENCHMARK_RUNNER_FILENAME + ".java";
                //ProcessBuilder pb = new ProcessBuilder("javac","-sourcepath","\""+algorithmDir+"\"","-cp","\""+earsPath+"\"","\""+algorithmDir+File.separator+BENCHMARK_RUNNER_FILENAME+".java\"");
                //ProcessBuilder pb = new ProcessBuilder("javac","-sourcepath",algorithmDir,"-cp",earsPath, BENCHMARK_RUNNER_FILENAME+".java");

                File folder = new File(algorithmDir);

                if (!folder.exists()) {
                    logger.log(Level.SEVERE, "Folder does not exists: " + algorithmDir);
                    return null;
                }

                File[] listOfFiles = folder.listFiles();
                String fileName;
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        fileName = file.getName();
                        if (FilenameUtils.getExtension(fileName).equals("java")) {
                            ProcessBuilder pb = new ProcessBuilder("javac", "-sourcepath", algorithmDir, "-cp", earsPath, fileName);
                            pb.directory(new File(algorithmDir));
                            startProcess(pb, algorithmDir, algorithmName, benchmarkResultsDir);
                        }
                    }
                }

            } catch (Exception e) {
                //e.printStackTrace();
                logger.log(Level.SEVERE, "Error while compiling benchmark runner file for algorithm: " + algorithmName, e);
                writeErrorToJson(algorithmDir, e.toString());
                removeGeneratedFiles(benchmarkResultsDir, algorithmName);
                return null;
            }

            logger.log(Level.INFO, "Running benchmark for algorithm: " + algorithmName);
            //run the compiled file
            try {
                ProcessBuilder pb;
                if (SystemUtils.IS_OS_WINDOWS) {
                    pb = new ProcessBuilder("java", "-cp", "\"" + earsPath + File.pathSeparator + algorithmDir + "\\\\\"", BENCHMARK_RUNNER_FILENAME);
                } else {
                    pb = new ProcessBuilder("java", "-cp", earsPath + File.pathSeparator + algorithmDir + "/", BENCHMARK_RUNNER_FILENAME);
                }

                startProcess(pb, algorithmDir, algorithmName, benchmarkResultsDir);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while running benchmark for algorithm: " + algorithmName, e);
                writeErrorToJson(algorithmDir, e.toString());
                removeGeneratedFiles(benchmarkResultsDir, algorithmName);
                return null;
            }
            writeStatusToJson(true, algorithmDir);

            return null;
        };
    }

    private static void removeGeneratedFiles(String benchmarkResultsDir, String algorithmName) {
        logger.log(Level.INFO, "Removing generated benchmark files for algorithm: " + algorithmName);

        File folder = new File(benchmarkResultsDir);

        if (!folder.exists()) {
            logger.log(Level.SEVERE, "Folder does not exists: " + benchmarkResultsDir);
            return;
        }

        File[] listOfFiles = folder.listFiles();
        String fileName;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();

                if(fileName.length() < algorithmName.length() + 1)
                    continue;

                if (fileName.substring(0, algorithmName.length() + 1).equals(algorithmName + "_")) {
                    try {
                        file.delete();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Unable to delete file: " + fileName);
                    }
                }
            }
        }
    }

    private static boolean removePackageFromFiles(String algorithmDir) {

        logger.log(Level.INFO, "Removing packages from files in folder: " + algorithmDir);

        File folder = new File(algorithmDir);

        if (!folder.exists()) {
            logger.log(Level.SEVERE, "Folder does not exists: " + algorithmDir);
            return false;
        }

        File[] listOfFiles = folder.listFiles();
        String fileName;
        String line;
        StringBuilder fileData;
        boolean hasPackage;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();

                if (getFileExtension(fileName).equals("java")) {

                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        hasPackage = false;
                        fileData = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            if (line.contains("package ")) {
                                hasPackage = true;
                                continue;
                            }
                            fileData.append(line);
                            fileData.append("\n");
                        }

                        if (hasPackage) {
                            FileWriter fw = new FileWriter(file, false);
                            fw.write(fileData.toString());
                            fw.close();
                        }

                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error while removing package files in directory: " + algorithmDir, e);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void startProcess(ProcessBuilder pb, String algorithmDir, String algorithmName, String benchmarkResultsDir) throws Exception {

        logger.log(Level.INFO, "Starting process with command: " + String.join(" ",pb.command().toArray(new String[0])));

        pb.redirectErrorStream(true);
        Process p = pb.start();
        StringBuilder sbError = new StringBuilder();

        new Thread(() -> {
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = null;
            try {
                while (p.isAlive() && (line = stdError.readLine()) != null) {
                    sbError.append(line);
                    sbError.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stdError.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            line = sbError.toString();
            if (line.length() > 0) {
                logger.log(Level.SEVERE, "Error while compiling benchmark runner file for algorithm: " + algorithmName + "\n" + line);
                writeErrorToJson(algorithmDir, line);
                removeGeneratedFiles(benchmarkResultsDir, algorithmName);
            }
        }).start();


        if (!p.waitFor(5, TimeUnit.HOURS)) {
            p.destroy();
            if (p.isAlive()) {
                p.destroyForcibly();
            }
            throw new Exception("Execution time exceeded");
        }
    }

    private static void performRating(String benchmarkResultsDir, String benchmarkName, String benchmarkId) {

        List<String> problems = new ArrayList<String>();
        List<String> algorithms = new ArrayList<String>();

        int numberOfRuns = 15; //Default

        //Create benchmark object to get number of runs
        try {
            Class<?> clazz = Class.forName(BENCHMARK_PACKAGE + "." + benchmarkName);
            Object benchmark = clazz.newInstance();
            numberOfRuns = ((Benchmark) benchmark).getNumberOfRuns();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception creating class from name: " + BENCHMARK_PACKAGE + "." + benchmarkName, e);
            e.printStackTrace();
        }

        DummyAlgorithm.readFromJson = false; //engine only works with .txt files
        ArrayList<DummyAlgorithm> players = new ArrayList<DummyAlgorithm>();
        DummyBenchmark dr = new DummyBenchmark(0.000001); //Create benchmark
        dr.setDisplayRatingCharts(false);
        dr.setRatingCalculation(Benchmark.RatingCalculation.NORMAL);
        Benchmark.printInfo = false;

        //parse algorithm and problem names
        String algorithmName, problemName, fileName;

        File folder = new File(benchmarkResultsDir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();
                int index = fileName.lastIndexOf("_");
                if (index > 0) {
                    algorithmName = fileName.substring(0, index);
                    problemName = fileName.substring(algorithmName.length() + 1, fileName.lastIndexOf('.'));
                    problems.add(problemName);
                    algorithms.add(algorithmName);
                }
            }
        }

        //get distinct problem names
        problems = problems.stream().distinct().collect(Collectors.toList());
        //get distinct algorithm names
        algorithms = algorithms.stream().distinct().collect(Collectors.toList());

        //add problems to benchmark
        for (String name : problems) {
            dr.addDummyTask(name);
        }
        //TODO check if all algorithms have all problem files (get of list all problems from benchmark object)
        //add problems to benchmark
        for (String name : algorithms) {
            players.add(new DummyAlgorithm(name, benchmarkResultsDir));
        }

        for (DummyAlgorithm al : players) {
            dr.addAlgorithm(al);
        }

        dr.run(numberOfRuns);
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        logger.log(Level.INFO, "Total execution time: " + estimatedTime + "s");
        ArrayList<Player> list = dr.getTournamentResults().getPlayers();
        Player.JsonPlayer[] jsonPlayers = dr.getTournamentResults().getPlayersJson();
        StringBuilder sb = new StringBuilder();
        for (Player p : list) {
            //System.out.println(p); //print ranks
            sb.append(p.toString());
            sb.append("\n");
        }

        HashMap<String, Submission> newestSubmissions = getNewestSubmissions(benchmarkId);

        if(newestSubmissions != null) {

            for(Player.JsonPlayer player : jsonPlayers) {
                for(Submission sub : newestSubmissions.values()) {
                    if(sub.algorithm.equals(player.playerId)) {
                        player.submissionId = sub.id;
                        player.submissionAuthor =  sub.author;
                        break;
                    }
                }
            }
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        Util.writeToFile(benchmarkResultsDir + File.separator + RESULTS_FILE, gson.toJson(jsonPlayers));
        Util.writeToFile(benchmarkResultsDir + File.separator + REPORT_FILE, sb.toString());
    }

    private static String getFileExtension(String fileName) {

        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0)
            extension = fileName.substring(i + 1);
        return extension;
    }

    public static class Tournament {
        public String id;
        public String name;
        public Long timestamp;
        public Long ends;
        public String benchmarks;
        public String path;
        public String description;
        public String password;
    }

    public static class Submission {
        public String id;
        public String author;
        public Long timestamp;
        public String algorithm;
        public String submissionUrl;
    }
}