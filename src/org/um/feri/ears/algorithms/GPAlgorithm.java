package org.um.feri.ears.algorithms;

import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.algorithms.gp.GPAlgorithmExecutor;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.GPAlgorithmRunStats;
import org.um.feri.ears.util.RunConfiguration;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.gp_stats.GPAlgorithmMultiConfigurationsProgressData;
import org.um.feri.ears.util.gp_stats.GPAlgorithmRunProgressData;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class GPAlgorithm extends Algorithm<ProgramSolution, ProgramSolution, ProgramProblem> {

    public static final long serialVersionUID = -7440978397322072640L;

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

    public abstract ProblemComparator<ProgramSolution> getComparator();

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
                        multiConfigurationsProgressData.saveProgressData();
                    }

                    // Print current gpAlgorithm statistics to console
                    if(isDebug()){
                        System.out.println("Generation: " + getTask().getNumberOfIterations() + ", Best Fitness: " + getBest().getEval() + ", Avg Fitness: " + getAvgGenFitnesses().get(getAvgGenFitnesses().size() - 1) + ", Avg Tree Depth: " + getAvgGenTreeDepths().get(getAvgGenTreeDepths().size() - 1) + ", Avg Tree Size: " + getAvgGenTreeSizes().get(getAvgGenTreeSizes().size() - 1));
                        System.out.println("Best Individual: " + getBest().getTree().toJsonString());
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
        return new GPAlgorithmRunStats(bestOverallFitnesses, avgGenFitnesses, avgGenTreeDepths, avgGenTreeSizes, bestGenFitnesses);
    }
}
