package org.um.feri.ears.algorithms.so.soma;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class SOMA extends Algorithm {

    Strategy strategy;
    DoubleSolution best;
    int leaderId; //index of the best solution
    Task task;

    private int popSize;
    private double step;
    private double pathLength;
    private double prt;

    DoubleSolution[] population;

    public SOMA(Strategy strategy) {
        this(strategy, 50);
    }

    public SOMA(Strategy strategy, int popSize) {
        this(strategy, popSize, 0.3, 3.1, 0.3);
    }

    public SOMA(Strategy strategy, int popSize, double step, double pathLength, double prt) {
        super();
        this.strategy = strategy;
        this.popSize = popSize;
        this.step = step;
        this.pathLength = pathLength;
        this.prt = prt;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("SOMA",
                "@article{zelinka2016soma,"
                        + "title={SOMA—self-organizing migrating algorithm},"
                        + "author={Zelinka, Ivan},"
                        + "booktitle={Self-Organizing Migrating Algorithm},"
                        + "pages={3--49},"
                        + "year={2016},"
                        + "publisher={Springer}}",
                "SOMA_" + strategy.name(), "Self-Organizing Migrating Algorithm");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        task = taskProblem;
        initPopulation();

        while (!task.isStopCriteria()) {

            switch (strategy) {
                case ALL_TO_ALL:
                    population = allToAll();
                    break;
                case ALL_TO_ONE:
                    population = allToOne();
                    break;
                case ALL_TO_ONE_RANDOM:
                    population = allToOneRandom();
                    break;
                case ALL_TO_ALL_ADAPTIVE:
                    population = allToOneAdaptive();
                    break;
            }
            task.incrementNumberOfIterations();
        }
        updateBestSolution();
        return best;
    }

    private DoubleSolution[] allToAll() throws StopCriteriaException {
        DoubleSolution[] newPopulation = new DoubleSolution[popSize];
        for (int i = 0; i < popSize; i++) {
            ArrayList<DoubleSolution> solutions = new ArrayList<>();
            for (int j = 0; j < popSize; j++) {
                if (i != j) {
                    solutions.addAll(getSoluitionsOnJumpingPositions(population[i], population[j]));
                }
            }
            newPopulation[i] = getBestSolution(solutions);
        }
        return newPopulation;
    }

    private DoubleSolution[] allToOne() throws StopCriteriaException {
        DoubleSolution[] newPopulation = new DoubleSolution[popSize];
        updateBestSolution();
        for (int i = 0; i < popSize; i++) {
            if (i == leaderId) {
                newPopulation[i] = new DoubleSolution(best);
            } else {
                ArrayList<DoubleSolution> soluitions = getSoluitionsOnJumpingPositions(population[i], best);
                newPopulation[i] = getBestSolution(soluitions);
            }
        }
        return newPopulation;
    }

    private DoubleSolution[] allToOneRandom() throws StopCriteriaException {
        DoubleSolution[] newPopulation = new DoubleSolution[popSize];
        int leaderId = Util.nextInt(popSize);
        DoubleSolution leader = population[leaderId];
        for (int i = 0; i < popSize; i++) {
            if (i == leaderId) {
                newPopulation[i] = new DoubleSolution(leader);
            } else {
                ArrayList<DoubleSolution> solutions = getSoluitionsOnJumpingPositions(population[i], leader);
                newPopulation[i] = getBestSolution(solutions);
            }
        }
        return newPopulation;
    }

    private DoubleSolution[] allToOneAdaptive() throws StopCriteriaException {
        DoubleSolution[] newPopulation = new DoubleSolution[popSize];
        for (int i = 0; i < popSize; i++) {
            DoubleSolution jumpingSolution = population[i];
            for (int j = 0; j < popSize; j++) {
                if (i != j) {
                    ArrayList<DoubleSolution> soluitions = getSoluitionsOnJumpingPositions(jumpingSolution, population[j]);
                    jumpingSolution = getBestSolution(soluitions);
                }
            }
            newPopulation[i] = jumpingSolution;
        }
        return newPopulation;
    }

    private void updateBestSolution() {
        for (int i = 0; i < popSize; i++) {
            if (task.isFirstBetter(population[i], best)) {
                best = new DoubleSolution(population[i]);
                leaderId = i;
            }
        }
    }

    private void initPopulation() throws StopCriteriaException {
        population = new DoubleSolution[popSize];
        best = task.getRandomSolution();
        leaderId = 0;
        population[0] = new DoubleSolution(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriteria())
                break;
            population[i] = task.getRandomSolution();
            if (task.isFirstBetter(population[i], best)) {
                best = new DoubleSolution(population[i]);
                leaderId = i;
            }
        }
    }

    protected DoubleSolution getBestSolution(ArrayList<DoubleSolution> solutions) {
        DoubleSolution bestSolution = solutions.get(0);
        for (int i = 1; i < solutions.size(); i++) {
            if (solutions.get(i) == null)
                continue;
            if (task.isFirstBetter(solutions.get(i), bestSolution)) {
                bestSolution = solutions.get(i);
            }
        }
        return bestSolution;
    }

    private ArrayList<DoubleSolution> getSoluitionsOnJumpingPositions(DoubleSolution jumpingSolution, DoubleSolution towardsSolution) throws StopCriteriaException {
        ArrayList<DoubleSolution> solutions = new ArrayList<>();
        solutions.add(jumpingSolution);
        for (int i = 1; i * step <= pathLength; i++) {
            if (task.isStopCriteria())
                return solutions;
            DoubleSolution addSolution = getSolutionOnStep(jumpingSolution, towardsSolution, i * step);
            solutions.add(addSolution);
        }
        return solutions;
    }

    private DoubleSolution getSolutionOnStep(DoubleSolution jumpingSolution, DoubleSolution towardsSolution, double jump) throws StopCriteriaException {
        double[] newSolution = new double[task.getNumberOfDimensions()];
        boolean[] prtVector = createPrtVector();
        for (int i = 0; i < task.getNumberOfDimensions(); i++) {
            if (prtVector[i]) {
                newSolution[i] = jumpingSolution.getValue(i) + (towardsSolution.getValue(i) - jumpingSolution.getValue(i)) * jump;
                while (newSolution[i] < task.getLowerLimit(i)) {
                    newSolution[i] += task.getUpperLimit(i) - task.getLowerLimit()[i];
                }
                while (newSolution[i] > task.getUpperLimit(i)) {
                    newSolution[i] -= task.getUpperLimit(i) - task.getLowerLimit()[i];
                }
            } else {
                newSolution[i] = jumpingSolution.getValue(i);
            }
        }
        return task.eval(newSolution);
    }

    private boolean[] createPrtVector() {
        boolean[] prtVector = new boolean[task.getNumberOfDimensions()];
        boolean ok = false;
        do {
            for (int i = 0; i < prtVector.length; i++) {
                prtVector[i] = Util.nextDouble() < prt;
                if (prtVector[i]) {
                    ok = true;
                }
            }
        } while (!ok);
        return prtVector;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}