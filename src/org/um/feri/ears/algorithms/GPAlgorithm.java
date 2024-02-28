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

    public static final String TASK_NAME_PREFIX = "task_";
    public static boolean CAN_RUN = true;
    public static boolean INTERRUPT_LISTENER_RUNNING = false;

    protected ArrayList<Double> bestGenFitness;
    protected ArrayList<Double> avgGenFitness;
    protected ArrayList<Double> avgGenTreeDepth;
    protected ArrayList<Double> avgGenTreeSize;
    public abstract ProgramSolution executeStep() throws StopCriterionException;

    public abstract ProgramSolution executeGeneration() throws StopCriterionException;

    public abstract List<ProgramSolution> getPopulation();

    public abstract void setPopulation(List<ProgramSolution> population);

    public abstract ProgramSolution getBest();

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

    public ArrayList<Double> getAvgGenTreeDepth() {
        return avgGenTreeDepth;
    }

    public ArrayList<Double> getAvgGenTreeSize() {
        return avgGenTreeSize;
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

        /*try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(population);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GPAlgorithm.TASK_NAME_PREFIX + filename))) {
            oos.writeObject(task);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        GPAlgorithm.CAN_RUN = true;
    }

    public static GPAlgorithm deserializeAlgorithmState(String filename){
        GPAlgorithm alg = new DefaultGPAlgorithm();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            alg = (GPAlgorithm) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GPAlgorithm.TASK_NAME_PREFIX + filename))) {
            alg.task = (Task<ProgramSolution, ProgramProblem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<ProgramSolution> population = (List<ProgramSolution>) ois.readObject();
            alg.setPopulation(population);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        return alg;
    }
}
