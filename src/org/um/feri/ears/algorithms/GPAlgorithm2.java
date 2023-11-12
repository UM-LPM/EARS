package org.um.feri.ears.algorithms;

import org.um.feri.ears.algorithms.gp.DefaultGPAlgorithm2;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class GPAlgorithm2 extends Algorithm<ProgramSolution2, ProgramSolution2, ProgramProblem2> {

    public static final String TASK_NAME_PREFIX = "task_";
    public static boolean CAN_RUN = true;
    public static boolean INTERRUPT_LISTENER_RUNNING = false;

    protected ArrayList<Double> bestGenFitness;
    protected ArrayList<Double> avgGenFitness;
    protected ArrayList<Double> avgGenTreeHeight;
    protected ArrayList<Double> avgGenTreeSize;
    public abstract ProgramSolution2 executeStep() throws StopCriterionException;

    public abstract ProgramSolution2 executeGeneration() throws StopCriterionException;

    public abstract List<ProgramSolution2> getPopulation();

    public abstract ProgramSolution2 getBest();

    public ArrayList<Double> getBestGenFitness() {
        return bestGenFitness;
    }

    public void setBestGenFitness(ArrayList<Double> bestGenFitness) {
        this.bestGenFitness = bestGenFitness;
    }

    public ArrayList<Double> getAvgGenFitness() {
        return avgGenFitness;
    }

    public void setAvgGenFitness(ArrayList<Double> avgGenFitness) {
        this.avgGenFitness = avgGenFitness;
    }

    public ArrayList<Double> getAvgGenTreeHeight() {
        return avgGenTreeHeight;
    }

    public ArrayList<Double> getAvgGenTreeSize() {
        return avgGenTreeSize;
    }

    public static void addInterruptKeyListener(){
        // Create a new Thread that will handle user input
        Thread userInputThread = new Thread(() -> {
            GPAlgorithm2.INTERRUPT_LISTENER_RUNNING = true;
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Press E to pause the algorithm:");
                String input = scanner.nextLine();

                if(input.equals("e") || input.equals("E")){
                    GPAlgorithm2.CAN_RUN = false;
                    break;
                }
            }
            GPAlgorithm2.INTERRUPT_LISTENER_RUNNING = false;
        });

        if(!GPAlgorithm2.INTERRUPT_LISTENER_RUNNING){
            // Start the userInputThread
            userInputThread.start();
        }

    }

    public static void serializeAlgorithmState(List<ProgramSolution2> population, Task<ProgramSolution2, ProgramProblem2> task, String filename) {
        System.out.println("Serializing current task and population state");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(population);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GPAlgorithm2.TASK_NAME_PREFIX + filename))) {
            oos.writeObject(task);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GPAlgorithm2.CAN_RUN = true;
    }

    public static DefaultGPAlgorithm2 deserializeAlgorithmState(String filename){
        DefaultGPAlgorithm2 alg = new DefaultGPAlgorithm2();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GPAlgorithm2.TASK_NAME_PREFIX + filename))) {
            alg.task = (Task<ProgramSolution2, ProgramProblem2>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<ProgramSolution2> population = (List<ProgramSolution2>) ois.readObject();
            alg.setPopulation(population);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return alg;
    }
}
