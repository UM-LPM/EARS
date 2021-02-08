package org.um.feri.ears.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;

public class DummyAlgorithm extends Algorithm {

    HashMap<String, double[]> results;
    HashMap<String, EvaluationStorage> problemEvaluations;
    HashMap<String, Integer> positions; //stores the position of the current result of the current problem
    String algorithmName;
    String filesDir;
    public static boolean readFromJson = true;

    public DummyAlgorithm(String name) {
        this(name, "D:/Results/");
    }

    public DummyAlgorithm(String algorithmName, String filesDir) {
        this.algorithmName = algorithmName;
        this.filesDir = filesDir;
        ai = new AlgorithmInfo(algorithmName, algorithmName, "");
        results = new HashMap<>();
        positions = new HashMap<>();
        problemEvaluations = new HashMap<>();
        if (!readFromJson)
            fillResults(algorithmName); //TODO lazy load
    }

    private void fillResults(String name) {

        File folder = new File(filesDir);
        File[] listOfFiles = folder.listFiles();

        String problemName, fileName, value;

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();
                if (fileName.toLowerCase().indexOf(name.toLowerCase() + "_") == 0) {
                    problemName = fileName.substring(name.length() + 1, fileName.length() - 4);
                    double[] resultArray = new double[10000];
                    int index = 0;
                    try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                        String line = br.readLine();

                        while (line != null) {
                            //First line may contain metadata
                            if (index == 0 && line.indexOf(';') > 0) {
                                readAlgorithmInfo(line);
                                line = br.readLine();
                                continue;
                            }
                            resultArray[index] = Double.parseDouble(line);
                            line = br.readLine();
                            index++;
                            if (index >= 10000) {
                                System.err.println("The file " + fileName + " has more than 10000 results. Skipping to end of file.");
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

        if (readFromJson) {
            String fileName = algorithmName + "_" + task.getFileNameString() + ".json";

            if (!problemEvaluations.containsKey(fileName)) {
                String path = filesDir + File.separator + fileName;

                if (new File(path).isFile()) {
                    String json = Util.readFromFile(path);
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        //TODO check json file metadata (versions, dimensions...)
                        EvaluationStorage es = mapper.readValue(json, EvaluationStorage.class);
                        problemEvaluations.put(fileName, es);
                        positions.put(fileName, 0);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("File " + path + " not found!");
                    return null;
                }
            }

            EvaluationStorage es = problemEvaluations.get(fileName);
            int currentRun = positions.get(fileName);
            EvaluationStorage.Evaluation[] evaluations = es.evaluations[currentRun];
            positions.put(fileName, currentRun + 1);

            switch (task.getStopCriterion()) {
                case CPU_TIME: {
                    long stopCpuTime = task.getAllowedCPUTime();
                    int index;
                    for (index = 0; index < evaluations.length; index++) {
                        long cpuTime = evaluations[index].time;
                        if (cpuTime == stopCpuTime) { //if evaluation with the same stopping criterion found break
                            break;
                        } else if (index + 1 < evaluations.length) { //if the next evaluation has a higher cpu time return the current evaluation
                            if (evaluations[index + 1].time > stopCpuTime)
                                break;
                        }
                    }
                    index = Math.min(index, evaluations.length - 1);
                    updateTask(task, evaluations[index]);
                    return new DummySolution(evaluations[index].fitness);
                }
                case ITERATIONS: {
                    int stopIteration = task.getMaxIterations();
                    int index;
                    for (index = 0; index < evaluations.length; index++) {
                        int iteration = evaluations[index].iteration;
                        if (iteration == stopIteration) { //if evaluation with the same stopping criterion found break
                            break;
                        } else if (index + 1 < evaluations.length) { //if the next evaluation has a higher iteration number return the current evaluation
                            if (evaluations[index + 1].iteration > stopIteration)
                                break;
                        }
                    }
                    index = Math.min(index, evaluations.length - 1);
                    updateTask(task, evaluations[index]);
                    return new DummySolution(evaluations[index].fitness);
                }
                case EVALUATIONS: {
                    int stopEvaluation = task.getMaxEvaluations();
                    int index;
                    for (index = 0; index < evaluations.length; index++) {
                        int evaluation = evaluations[index].evalNum;
                        if (evaluation == stopEvaluation) { //if evaluation with the same stopping criterion found break
                            break;
                        } else if (index + 1 < evaluations.length) { //if the next evaluation has a higher evaluation number return the current evaluation
                            if (evaluations[index + 1].evalNum > stopEvaluation)
                                break;
                        }
                    }
                    index = Math.min(index, evaluations.length - 1);
                    updateTask(task, evaluations[index]);
                    return new DummySolution(evaluations[index].fitness);
                }
                case STAGNATION: {
                    int stagnationTrialCounter = 0;
                    int index = 0;
                    double best = evaluations[index++].fitness;
                    for (; index < evaluations.length; index++) {

                        if (task.isFirstBetter(evaluations[index].fitness, best)) {
                            best = evaluations[index].fitness;
                            stagnationTrialCounter = 0;
                        } else {
                            stagnationTrialCounter += evaluations[index].evalNum - evaluations[index - 1].evalNum; // increment by number of evaluations between two indices
                        }

                        if (stagnationTrialCounter >= task.getMaxTrialsBeforeStagnation()) {
                            break;
                        }
                    }
                    index = Math.min(index, evaluations.length - 1);
                    updateTask(task, evaluations[index]);
                    return new DummySolution(evaluations[index].fitness);
                }
                case GLOBAL_OPTIMUM_OR_EVALUATIONS: {
                    int index;
                    for (index = 0; index < evaluations.length; index++) {
                        if (task.isEqualToGlobalOptimum(evaluations[index].fitness))
                            break;
                    }
                    index = Math.min(index, evaluations.length - 1);
                    updateTask(task, evaluations[index]);
                    return new DummySolution(evaluations[index].fitness);
                }
                default:
                    break;
            }
        } else {
            //TODO lazy load results
            String problemName = task.getProblemName();

            if (results.containsKey(problemName.toLowerCase())) {
                double val = getNextValue(problemName.toLowerCase());
                return new DummySolution(val);
            }
        }
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
                task.incrementNumberOfEvaluations();
            }
            while (task.getNumberOfIterations() < evaluation.iteration)
                task.incrementNumberOfIterations();
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
