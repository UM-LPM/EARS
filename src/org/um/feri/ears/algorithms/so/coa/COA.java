package org.um.feri.ears.algorithms.so.coa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class COA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private NumberSolution<Double>[] population;
    private NumberSolution<Double> bestSolution;

    public COA() {
        this(30);
    }


    public COA(int popSize) {
        this.popSize = popSize;
        au = new Author("david", "david.dugar@student.um.si");
        ai = new AlgorithmInfo("COA", "Coati Optimization Algorithm", "@article{dehghani2023coati,\n"
                + "  title={Coati Optimization Algorithm: A new bio-inspired metaheuristic algorithm for solving optimization problems},\n"
                + "  author={Mohammad Dehghani, Zeinab Montazeri, Eva Trojovská, Pavel Trojovský},\n"
                + "  journal={Knowledge-Based Systems},\n"
                + "  article={110011},\n" + "  year={2023},\n" + "  publisher={Elsevier}}");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task)
            throws StopCriterionException {
        this.task = task;

        initPopulation();

        while (!task.isStopCriterion()) {
            updateBestSolution();

            //PHASE 1: hunting and attacking strategy on iguana (Exploration Phase)
            NumberSolution<Double> iguana = bestSolution;

            //part 1: First half of population climbs towards Best Solution (iguana)
            for (int i = 0; i < popSize / 2; i++) {
                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> xI = population[i]; // X(i,:)
                NumberSolution<Double> xP1 = updatePositionPhase1(xI, iguana); // xP1

                if (task.problem.isFirstBetter(xP1, xI)) {
                    population[i] = xP1;
                }
            }

            //part 2: second half of population interacts with a random iguana
            for (int i = popSize / 2; i < popSize; i++) {
                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> xI = population[i];

                //random iguana generated within bounds (Eq. 5)
                NumberSolution<Double> randomIguana = task.generateRandomEvaluatedSolution();

                NumberSolution<Double> xP1 = updatePositionPhase1Part2(xI, randomIguana);

                /* 
                Eq. 7 (eval in updatePositionPgase1Partz2): selection (matches if(F_P1 < fit)
                here xP1 already has its fitness calculated
                */
                if (task.problem.isFirstBetter(xP1, xI)) {
                    population[i] = xP1;
                }
            }

            //PHASE 2 start: escaping from predators (Exploitation Phase)
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> xI = population[i];
                NumberSolution<Double> xP2 = updatePositionPhase2(xI);

                //Eq. 11 (eval in updatePositionPhase2)
                if (task.problem.isFirstBetter(xP2, xI)) {
                    population[i] = xP2;
                }
            }

            updateBestSolution();
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new NumberSolution[popSize];
        for (int i = 0; i < popSize; i++) {
            population[i] = task.generateRandomEvaluatedSolution();
            if (task.isStopCriterion()) {
                bestSolution = population[0];
                for (int j = 0; j <= i; j++) {
                    if (population[j] != null && task.problem.isFirstBetter(population[j], bestSolution)) {
                        bestSolution = population[j];
                    }
                }
                return;
            }
        }
        updateBestSolution();
    }

    private void updateBestSolution() {
        NumberSolution<Double> currentBest = population[0];
        for (int i = 1; i < popSize; i++) {
            if (task.problem.isFirstBetter(population[i], currentBest)) {
                currentBest = population[i];
            }
        }
        if (bestSolution == null || task.problem.isFirstBetter(currentBest, bestSolution)) {
            bestSolution = currentBest;
        }
    }

    private NumberSolution<Double> updatePositionPhase1(NumberSolution<Double> xI, NumberSolution<Double> iguana)
            throws StopCriterionException {
        ArrayList<Double> xP1Vals = new ArrayList<>();
        double I = RNG.nextInt(2) + 1;

        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
            double r = RNG.nextDouble();
            double val = xI.getValue(d);
            double bestVal = iguana.getValue(d);

            //equation: X_P1 = X(i,:) + r * (iguana - I * X(i,:)) (Eq. 4)
            double newVal = val + r * (bestVal - I * val);
            newVal = task.problem.makeFeasible(newVal, d);
            xP1Vals.add(newVal);
        }

        NumberSolution<Double> X_P1 = new NumberSolution<>(xP1Vals);
        task.eval(X_P1);
        return X_P1;
    }

    private NumberSolution<Double> updatePositionPhase1Part2(NumberSolution<Double> xI,
            NumberSolution<Double> randomIguana)
            throws StopCriterionException {
        ArrayList<Double> xP1Vals = new ArrayList<>();

        //I = round(1+rand) -> 1 or 2
        double I = RNG.nextInt(1,2);

        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
            double r = RNG.nextDouble();

            double val = xI.getValue(d);
            double iguanaVal = randomIguana.getValue(d);
            double newVal;

            if (task.problem.isFirstBetter(xI, randomIguana)) {
                //xI is better, move away
                newVal = val + r * (val - iguanaVal);
            } else {
                //xI is worse, move towards random iguana (Eq 6)
                newVal = val + r * (iguanaVal - I * val);
            }

            newVal = task.problem.makeFeasible(newVal, d);

            xP1Vals.add(newVal);
        }

        NumberSolution<Double> xP1 = new NumberSolution<>(xP1Vals);
        task.eval(xP1);
        return xP1;
    }

    private NumberSolution<Double> updatePositionPhase2(NumberSolution<Double> xI)
            throws StopCriterionException {
        ArrayList<Double> xP2Vals = new ArrayList<>();

        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {

            double val = xI.getValue(d);
            double lb = task.problem.getLowerLimit().get(d);
            double ub = task.problem.getUpperLimit().get(d);

            //MATLAB: loLocal=lowerbound/t; hiLocal=upperbound/t;
            double loLocal = lb / (task.getNumberOfIterations() + 1); // Eq. 9
            double hiLocal = ub / (task.getNumberOfIterations() + 1); // Eq. 10

            double newVal = val + (1 - 2 * RNG.nextDouble()) * (loLocal + RNG.nextDouble() * (hiLocal - loLocal));

            //explicitly clamp to [loLocal, hiLocal]
            newVal = Math.max(loLocal, Math.min(newVal, hiLocal));

            newVal = task.problem.makeFeasible(newVal, d);
            xP2Vals.add(newVal);
        }

        NumberSolution<Double> X_P2 = new NumberSolution<>(xP2Vals);
        task.eval(X_P2);
        return X_P2;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
