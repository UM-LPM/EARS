package org.um.feri.ears.algorithms;

import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.io.*;
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

    public abstract List<ProgramSolution> getPopulation();

    public abstract void setPopulation(List<ProgramSolution> population);

    public abstract ProgramSolution getBest();

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
}
