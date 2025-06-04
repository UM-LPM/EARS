package org.um.feri.ears.algorithms;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.GPAlgorithmExecutor;
import org.um.feri.ears.algorithms.gp.RequiredEvalsCalcMethod;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.GPAlgorithmRunStats;
import org.um.feri.ears.util.RunConfiguration;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.gp_stats.GPAlgorithmMultiConfigurationsProgressData;
import org.yaml.snakeyaml.util.Tuple;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public abstract class GPAlgorithm extends Algorithm<ProgramSolution, ProgramSolution, ProgramProblem> {

    public static final long serialVersionUID = -7440978397322072640L;

    @AlgorithmParameter(name = "required evaluations calculation method")
    protected RequiredEvalsCalcMethod requiredEvalsCalcMethod;

    @AlgorithmParameter(name = "required evaluations calculation method params")
    // Dictionary with key value pairs
    protected HashMap<String, Integer> requiredEvalsCalcMethodParams;

    public static final String TASK_NAME_PREFIX = "task_";
    public static boolean CAN_RUN = true;
    public static boolean INTERRUPT_LISTENER_RUNNING = false;

    protected ArrayList<Double> bestOverallFitnesses;
    protected ArrayList<Double> avgGenFitnesses;
    protected ArrayList<Double> avgGenTreeDepths;
    protected ArrayList<Double> avgGenTreeSizes;
    protected ArrayList<Double> bestGenFitnesses;

    public abstract ProgramSolution executeStep() throws StopCriterionException;

    public abstract ProgramSolution executeGeneration() throws StopCriterionException;

    public abstract ProgramSolution execute(GPAlgorithmExecutor gpAlgorithmExecutor, RunConfiguration runConfiguration, String saveGPAlgorithmStateFilename, GPAlgorithmMultiConfigurationsProgressData multiConfigurationsProgressData) throws StopCriterionException;

    public abstract List<ProgramSolution> getPopulation();

    public abstract void setPopulation(List<ProgramSolution> population);

    public abstract ProgramSolution getBest();

    public abstract List<ImmutablePair<Integer, ProgramSolution>> getBestGenSolutions();

    public abstract List<ProgramSolution> getBestGenSolutionsConvergenceGraph();

    public abstract List<ProgramSolution> getBestGenSolutionsMasterTournament();

    public abstract ProblemComparator<ProgramSolution> getComparator();

    public abstract void setHallOfFameSize(int hallOfFameSize);

    public ArrayList<Double> getBestOverallFitnesses() {
        return bestOverallFitnesses;
    }

    public void setBestOverallFitnesses(ArrayList<Double> bestOverallFitnesses) {
        this.bestOverallFitnesses = bestOverallFitnesses;
    }

    public ArrayList<Double> getAvgGenFitnesses() {
        return avgGenFitnesses;
    }

    public void setAvgGenFitnesses(ArrayList<Double> avgGenFitnesses) {
        this.avgGenFitnesses = avgGenFitnesses;
    }

    public ArrayList<Double> getAvgGenTreeDepths() {
        return avgGenTreeDepths;
    }

    public ArrayList<Double> getAvgGenTreeSizes() {
        return avgGenTreeSizes;
    }

    public ArrayList<Double> getBestGenFitnesses() {
        return bestGenFitnesses;
    }

    public void setBestGenFitnesses(ArrayList<Double> bestGenFitnesses) {
        this.bestGenFitnesses = bestGenFitnesses;
    }


    public static void addInterruptKeyListener(){
        // Create a new Thread that will handle user input
        Thread userInputThread = new Thread(() -> {
            GPAlgorithm.INTERRUPT_LISTENER_RUNNING = true;
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Press E to pause the algorithm:");
                String input = scanner.nextLine();

                if(input.equals("e") || input.equals("E")){
                    GPAlgorithm.CAN_RUN = false;
                    break;
                }
            }
            GPAlgorithm.INTERRUPT_LISTENER_RUNNING = false;
        });

        if(!GPAlgorithm.INTERRUPT_LISTENER_RUNNING){
            // Start the userInputThread
            userInputThread.start();
        }

    }

    public void execute(int numOfGens, String saveGPAlgorithmStateFilename, String executionPhaseName, GPAlgorithmMultiConfigurationsProgressData multiConfigurationsProgressData) {
        try {
            try {
                if (numOfGens <= 0)
                    numOfGens = Integer.MAX_VALUE;

                for (int i = 0; i < numOfGens; i++) {
                    ProgramSolution sol = executeGeneration();

                    // Update GP Algorithm progress data run
                    if(multiConfigurationsProgressData != null){
                        multiConfigurationsProgressData.addGenProgressData(i + 1, executionPhaseName, this);
                    }

                    // Print current gpAlgorithm statistics to console
                    if(isDebug()){
                        System.out.println("Generation: " + getTask().getNumberOfIterations() + ", Best Fitness: " + getBest().getEval() + ", Avg Fitness: " + getAvgGenFitnesses().get(getAvgGenFitnesses().size() - 1) + ", Avg Tree Depth: " + getAvgGenTreeDepths().get(getAvgGenTreeDepths().size() - 1) + ", Avg Tree Size: " + getAvgGenTreeSizes().get(getAvgGenTreeSizes().size() - 1));
                        //System.out.println("Best Individual: " + getBest().getTree().toJsonString());
                    }

                    if (saveGPAlgorithmStateFilename == null || saveGPAlgorithmStateFilename.length() == 0) {
                        // Serialize algorithm state
                        GPAlgorithm.serializeAlgorithmState(this, getDefaultGPAlgorithmStateFilename());
                    } else {
                        // Serialize algorithm state
                        GPAlgorithm.serializeAlgorithmState(this, saveGPAlgorithmStateFilename);
                    }
                    if (sol != null)
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number of generations to run (-1 for run to the end).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (StopCriterionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getDefaultGPAlgorithmStateFilename(){
        return task.problem.getName() + "_" + getPopSize() + "_" + getCrossoverProbability() + "_" + getElitismProbability() + "_" + getMutationProbability() + "_" + getNumberOfTournaments() + "_" + task.problem.getMinTreeDepth() + "_" + task.problem.getMaxTreeStartDepth() + "_" + task.problem.getMaxTreeEndDepth() + "_" + task.problem.getMaxTreeSize() + "_gpAlgorithmState_" + getFormattedDate() + ".ser";
    }

    public String getFormattedDate() {
        // Get current date
        LocalDate date = LocalDate.now();
        // Create a new string builder
        // Append the day
        String formattedDate = String.format("%02d", date.getDayOfMonth()) +
                // Append the month
                String.format("%02d", date.getMonthValue()) +
                // Append the year
                date.getYear();

        return formattedDate;
    }

    public static void serializeAlgorithmState(GPAlgorithm gpAlgorithm, String filename) {
        System.out.println("Serializing current task and population state");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gpAlgorithm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GPAlgorithm.CAN_RUN = true;
    }

    public static GPAlgorithm deserializeAlgorithmState(String filename){
        GPAlgorithm alg = new DefaultGPAlgorithm();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            alg = (GPAlgorithm) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return alg;
    }

    public int calculateRequiredEvals(int popSize){
        if(requiredEvalsCalcMethod != RequiredEvalsCalcMethod.POP_SIZE && (requiredEvalsCalcMethodParams == null || !requiredEvalsCalcMethodParams.containsKey("EvalsPerMatch"))){
            throw new IllegalArgumentException("Required evaluations calculation method params must contain key 'EvalsPerMatch' for the required evaluations calculation method: " + requiredEvalsCalcMethod);
        }

        int evalsPerMatch = 1;
        if(requiredEvalsCalcMethodParams != null && requiredEvalsCalcMethodParams.containsKey("EvalsPerMatch")){
            evalsPerMatch = requiredEvalsCalcMethodParams.get("EvalsPerMatch");
        }

        int requiredEvals = -1;
        switch(requiredEvalsCalcMethod){
            case POP_SIZE:
                return popSize;
            case SET:
                requiredEvals = popSize - 1;
                break;
            case DET:
                requiredEvals = (2 * popSize) - 2;
                break;
            case K_Random_Opponents:
                if(requiredEvalsCalcMethodParams == null || !requiredEvalsCalcMethodParams.containsKey("K")){
                    throw new IllegalArgumentException("Required evaluations calculation method params must contain key 'K' for K Random Opponents method");
                }
                requiredEvals = (int) Math.floor((popSize * requiredEvalsCalcMethodParams.get("K")) / 2.0);
                break;
            case SwissSystem:
            case SSOS:
                requiredEvals = (int) (Math.ceil(popSize / 2.0) * Math.ceil(Math.log(popSize) / Math.log(2)));
                break;
        }

        return requiredEvals * evalsPerMatch;
    }

    public void setPopSize(int popSize) {
        return;
    }

    public int getPopSize() {
        return -1;
    }

    public void setCrossoverProbability(double crossoverProbability) {
        return;
    }

    public double getCrossoverProbability() {
        return 0;
    }

    public void setMutationProbability(double mutationProbability) {
        return;
    }

    public void setElitismProbability(double elitismProbability) {
        return;
    }

    public double getMutationProbability() {
        return 0;
    }

    public void setNumberOfTournaments(int numberOfTournaments) {
        return;
    }

    public int getNumberOfTournaments() {
        return 0;
    }

    public double getElitismProbability() {
        return 0;
    }

    public GPAlgorithmRunStats getStats(){
        return new GPAlgorithmRunStats(getBestGenSolutions(), getBestGenSolutionsConvergenceGraph(), getBestGenSolutionsMasterTournament(), bestOverallFitnesses, avgGenFitnesses, avgGenTreeDepths, avgGenTreeSizes, bestGenFitnesses);
    }

    public RequiredEvalsCalcMethod getRequiredEvalsCalcMethod() {
        return requiredEvalsCalcMethod;
    }

    public void setRequiredEvalsCalcMethod(RequiredEvalsCalcMethod requiredEvalsCalcMethod) {
        this.requiredEvalsCalcMethod = requiredEvalsCalcMethod;
    }

    public Map<String, Integer> getRequiredEvalsCalcMethodParams() {
        return requiredEvalsCalcMethodParams;
    }

    public void setRequiredEvalsCalcMethodParams(HashMap<String, Integer> requiredEvalsCalcMethodParams) {
        this.requiredEvalsCalcMethodParams = requiredEvalsCalcMethodParams;
    }
}
