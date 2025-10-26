package org.um.feri.ears.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;

public class DummyAlgorithm extends NumberAlgorithm {

    public enum FileFormat {
        RESULT_PER_LINE, JSON, CEC_RESULTS_FORMAT
    }

    HashMap<String, double[]> results;
    HashMap<String, EvaluationStorage.Evaluation[]> problemEvaluations;
    HashMap<String, ArrayList<EvaluationStorage.Evaluation>[]> problemEvaluationHistory; //stores the evaluation history for each problem and each run
    HashMap<String, Integer> positions; //stores the position of the current result of the current problem
    String algorithmName;
    String filesDir;
    String nameInFile;
    FileFormat fileFormat;

    static final int MAX_RUNS_PER_FILE = 100; //this represents the number of results (lines) in a file. It should be set to the lowest possible number to improve memory management

    public DummyAlgorithm(String algorithmName) {
        this(algorithmName, algorithmName, "D:/Results/", FileFormat.RESULT_PER_LINE);
    }

    public DummyAlgorithm(String algorithmName, String filesDir) {
        this(algorithmName, algorithmName, filesDir, FileFormat.RESULT_PER_LINE);
    }

    public DummyAlgorithm(String algorithmName, String filesDir, FileFormat fileFormat) {
        this(algorithmName, algorithmName, filesDir, fileFormat);
    }

    /**
     * @param algorithmName name of the algorithm used in the results
     * @param nameInFile name of the algorithm in the file
     * @param filesDir directory where the results are stored
     * @param fileFormat format of the results file
     */
    public DummyAlgorithm(String algorithmName, String nameInFile, String filesDir, FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        this.algorithmName = algorithmName;
        this.nameInFile = nameInFile;
        this.filesDir = filesDir;
        ai = new AlgorithmInfo(algorithmName, algorithmName, "");
        results = new HashMap<>();
        positions = new HashMap<>();
        problemEvaluations = new HashMap<>();
        problemEvaluationHistory = new HashMap<>();

        if (fileFormat != FileFormat.JSON)
            fillResults(nameInFile);
    }

    private void fillResults(String name) {

        File folder = new File(filesDir);
        File[] listOfFiles = folder.listFiles();

        String problemName, fileName;

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();
                if (fileName.toLowerCase().indexOf(name.toLowerCase() + "_") == 0) {
                    problemName = fileName.substring(name.length() + 1, fileName.length() - 4);
                    double[] resultArray;
                    int index = 0;
                    try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                        String line = br.readLine();

                        if(fileFormat == FileFormat.CEC_RESULTS_FORMAT) {
                            int k = 0;

                            while (line != null) {
                                if(line.isBlank())
                                    break;

                                String[] splitLine;
                                if (line.contains(",")) {
                                    splitLine = line.split(",");
                                } else {
                                    // Split by one or more whitespace characters
                                    splitLine = line.split("\\s+");
                                }

                                index = 0;
                                resultArray = new double[MAX_RUNS_PER_FILE];

                                for (String s : splitLine) {

                                    if(s.isBlank())
                                        continue;

                                    if (index >= MAX_RUNS_PER_FILE) {
                                        System.err.println("The file " + fileName + " has more than " + MAX_RUNS_PER_FILE + " results. Skipping to end of file.");
                                        break;
                                    }

                                    if (s.equalsIgnoreCase("inf")) {
                                        resultArray[index++] = Double.POSITIVE_INFINITY;
                                    } else if (s.equalsIgnoreCase("-inf")) {
                                        resultArray[index++] = Double.NEGATIVE_INFINITY;
                                    } else {
                                        resultArray[index++] = Double.parseDouble(s);
                                    }
                                }

                                String problemKey = problemName.toLowerCase() + "k" + k++;
                                results.put(problemKey, resultArray);
                                positions.put(problemKey, 0);

                                line = br.readLine();
                            }

                        } else if (fileFormat == FileFormat.RESULT_PER_LINE) {
                            resultArray = new double[MAX_RUNS_PER_FILE];
                            while (line != null) {
                                if (index >= MAX_RUNS_PER_FILE) {
                                    System.err.println("The file " + fileName + " has more than " + MAX_RUNS_PER_FILE + " results. Skipping to end of file.");
                                    break;
                                }
                                //First line may contain metadata
                                if (index == 0 && line.indexOf(';') > 0) {
                                    readAlgorithmInfo(line);
                                    line = br.readLine();
                                    continue;
                                }
                                if(line.isBlank())
                                    break;

                                //line = line.replace(",", ".");
                                resultArray[index] = Double.parseDouble(line);
                                line = br.readLine();
                                index++;
                            }
                            results.put(problemName.toLowerCase(), resultArray);
                            positions.put(problemName.toLowerCase(), 0);
                        }

                    } catch (Exception e) {
                        System.out.println("Error in file: " + file.getAbsolutePath() + " " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readAlgorithmInfo(String metadata) {

        String[] allInfo = metadata.split(";");
        for (String inf : allInfo) {
            if (inf.contains("algorithm name")) {
                String[] subInfo = inf.split(",");
                for (String info : subInfo) {
                    String[] split = info.split(":");
                    if (split.length != 2)
                        continue;
                    addCustomInfo(split[0], split[1]);
                }
            }
        }
    }

    public void addProblemResults(String problemName, double[] resultArray) {
        results.put(problemName.toLowerCase(), resultArray);
        positions.put(problemName.toLowerCase(), 0);
    }

    @Override
    public DummySolution execute(Task task) {

        if (fileFormat == FileFormat.JSON) {
            String key = task.getFileNameString() + task.getStopCriterionString();

            //load evaluations from file
            if (!problemEvaluations.containsKey(key)) {
                String fileName = nameInFile + "_" + task.getFileNameString() + ".json";
                String path = filesDir + File.separator + fileName;

                if (new File(path).isFile()) {
                    String json = Util.readFromFile(path);
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        //TODO check json file metadata (versions, dimensions...)
                        EvaluationStorage es = mapper.readValue(json, EvaluationStorage.class);

                        EvaluationStorage.Evaluation[] evaluations = new EvaluationStorage.Evaluation[es.numberOfRuns];

                        ArrayList<EvaluationStorage.Evaluation>[] evaluationHistory = new ArrayList[es.numberOfRuns];

                        //find the evaluation corresponding to the stopping criterion for each run
                        for (int i = 0; i < es.numberOfRuns; i++) {

                            EvaluationStorage.Evaluation[] evaluationsPerRun = es.evaluations[i];

                            ArrayList<EvaluationStorage.Evaluation> filteredEvaluations = new ArrayList<>();
                            for (EvaluationStorage.Evaluation value : evaluationsPerRun) {

                                if (value.evalNum % task.getStoreEveryNthEvaluation() == 0) {
                                    filteredEvaluations.add(value);
                                }
                            }
                            evaluationHistory[i] = filteredEvaluations;

                            switch (task.getStopCriterion()) {
                                case CPU_TIME: {
                                    long stopCpuTime = task.getAllowedCPUTimeNs();
                                    int index;
                                    for (index = 0; index < evaluationsPerRun.length; index++) {
                                        long cpuTime = evaluationsPerRun[index].time;
                                        if (cpuTime == stopCpuTime) { //if evaluation with the same stopping criterion found break
                                            break;
                                        } else if (index + 1 < evaluationsPerRun.length) { //if the next evaluation has a higher cpu time return the current evaluation
                                            if (evaluationsPerRun[index + 1].time > stopCpuTime)
                                                break;
                                        }
                                    }
                                    index = Math.min(index, evaluationsPerRun.length - 1);
                                    evaluations[i] = evaluationsPerRun[index];
                                    break;
                                }
                                case ITERATIONS: {
                                    int stopIteration = task.getMaxIterations();
                                    int index;
                                    for (index = 0; index < evaluationsPerRun.length; index++) {
                                        int iteration = evaluationsPerRun[index].iteration;
                                        if (iteration == stopIteration) { //if evaluation with the same stopping criterion found break
                                            break;
                                        } else if (index + 1 < evaluationsPerRun.length) { //if the next evaluation has a higher iteration number return the current evaluation
                                            if (evaluationsPerRun[index + 1].iteration > stopIteration)
                                                break;
                                        }
                                    }
                                    index = Math.min(index, evaluationsPerRun.length - 1);
                                    evaluations[i] = evaluationsPerRun[index];
                                    break;
                                }
                                case EVALUATIONS: {
                                    int stopEvaluation = task.getMaxEvaluations();
                                    int index;
                                    for (index = 0; index < evaluationsPerRun.length; index++) {
                                        int evaluation = evaluationsPerRun[index].evalNum;
                                        if (evaluation == stopEvaluation) { //if evaluation with the same stopping criterion found break
                                            break;
                                        } else if (index + 1 < evaluationsPerRun.length) { //if the next evaluation has a higher evaluation number return the current evaluation
                                            if (evaluationsPerRun[index + 1].evalNum > stopEvaluation)
                                                break;
                                        }
                                    }
                                    index = Math.min(index, evaluationsPerRun.length - 1);
                                    evaluations[i] = evaluationsPerRun[index];
                                    break;
                                }
                                case STAGNATION: {
                                    int stagnationTrialCounter = 0;
                                    int index = 0;
                                    double best = evaluationsPerRun[index++].fitness;
                                    for (; index < evaluationsPerRun.length; index++) {

                                        if (task.problem.isFirstBetter(evaluationsPerRun[index].fitness, best, 0)) {
                                            best = evaluationsPerRun[index].fitness;
                                            stagnationTrialCounter = 0;
                                        } else {
                                            stagnationTrialCounter += evaluationsPerRun[index].evalNum - evaluationsPerRun[index - 1].evalNum; // increment by number of evaluations between two indices
                                        }

                                        if (stagnationTrialCounter >= task.getMaxTrialsBeforeStagnation()) {
                                            break;
                                        }
                                    }
                                    index = Math.min(index, evaluationsPerRun.length - 1);
                                    evaluations[i] = evaluationsPerRun[index];
                                    break;
                                }
                                case GLOBAL_OPTIMUM_OR_EVALUATIONS: {
                                    int index;
                                    for (index = 0; index < evaluationsPerRun.length; index++) {
                                        if (task.problem.isEqualToGlobalOptimum(evaluationsPerRun[index].fitness, 0))
                                            break;
                                    }
                                    index = Math.min(index, evaluationsPerRun.length - 1);
                                    evaluations[i] = evaluationsPerRun[index];
                                    break;
                                }
                                default:
                                    break;
                            }
                        }
                        problemEvaluationHistory.put(key, evaluationHistory);
                        problemEvaluations.put(key, evaluations);
                        positions.put(key, 0);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("File " + path + " not found!");
                    return null;
                }
            }

            if (!positions.containsKey(key)) {
                System.err.println("No evaluation with key: " + key + " found!");
                return null;
            }
            int currentRun = positions.get(key);
            if (currentRun >= problemEvaluations.get(key).length) {
                System.err.println("No evaluation for run: " + (currentRun + 1) + "!");
                return null;
            }
            EvaluationStorage.Evaluation evaluation = problemEvaluations.get(key)[currentRun];
            task.setEvaluationHistory(problemEvaluationHistory.get(key)[currentRun]);
            positions.put(key, currentRun + 1);
            updateTask(task, evaluation);
            return new DummySolution(evaluation.fitness);

        } else {
            //TODO lazy load results
            String problemName = task.getProblemName();

            if (results.containsKey(problemName.toLowerCase())) {
                double val = getNextValue(problemName.toLowerCase());
                return new DummySolution(val);
            }
        }
        System.err.println(task.getProblemName() + " not found for algorithm " + algorithmName);
        return new DummySolution(Double.MAX_VALUE);
    }

    private double getNextValue(String problemName) {

        double[] problemResults = results.get(problemName);
        int index = positions.get(problemName);
        positions.put(problemName, positions.get(problemName) + 1);
        return problemResults[index];
    }

    private void updateTask(Task task, EvaluationStorage.Evaluation evaluation) {
        try {
            while (task.getNumberOfEvaluations() < evaluation.evalNum) {
                task.incrementNumberOfEvaluations(1);
            }
            while (task.getNumberOfIterations() < evaluation.iteration)
                task.incrementNumberOfIterations();

            task.startTimer();
            task.setEvaluationTimeNs(evaluation.time);

        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of evaluations for the given problem and run
     */
    public void resetRunNumbers() {
        positions.replaceAll((k, v) -> 0);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
