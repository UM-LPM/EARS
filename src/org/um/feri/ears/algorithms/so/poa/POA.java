package org.um.feri.ears.algorithms.so.poa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class POA extends NumberAlgorithm {

    private int populationSize;
    private double R; // search radius

    private NumberSolution<Double> bestSolution;
    private NumberSolution<Double>[] population;

    public POA() {
        this(30);
    }

    public POA(int popSize) {
        au = new Author("Lea", "lea.roj1@student.um.si");
        ai = new AlgorithmInfo(
                "POA",
                "Pelican Optimization Algorithm",
                "Trojovský & Dehghani (2022)"
        );
        this.populationSize = popSize;
        this.R = 0.2;
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {

        this.task = task;
        int dimensions = task.problem.getNumberOfDimensions();
        initPopulation();

        while (!task.isStopCriterion()) {
            int t = task.getNumberOfIterations() + 1;

            // PREY INCIALIZATION ------------------------------------------------
            int prey = (int) Math.floor(RNG.nextDouble() * populationSize);  // randomly select a pelican as prey
            double[] positionOfPreyInSearchSpace = population[prey].getVariables().stream().mapToDouble(a -> a).toArray(); // select prey p_j
            double preyFitness = population[prey].getEval();  // F_p

            for (int i = 0; i < populationSize && !task.isStopCriterion(); i++) {
                NumberSolution<Double> pelicanPosition = population[i];
                double[] x = pelicanPosition.getVariables().stream().mapToDouble(a -> a).toArray();

                // Checks if prey is better than the current pelican
                boolean preyBetter = preyFitness < pelicanPosition.getEval();
                // If I = 2 --> more displacement for a member, which can lead member to newer areas of the search space. I affects the POA exploration power to accurately scan the search space
                double rI = RNG.nextDouble();
                int I = (int) Math.round(1 + rI); // I is a random number which is equal to one or two

                if (I < 1) {
                    I = 1;
                }
                if (I > 2) {
                    I = 2;
                }

                double r = RNG.nextDouble();

                // PHASE 1 ------------------------------------------------------------------------------------------
                for (int j = 0; j < dimensions; j++) {
                    double xi = x[j];
                    double preyLoc = positionOfPreyInSearchSpace[j];

                    if (preyBetter) // Equation (4) --> F_p < F_i
                        x[j] = xi + r * (preyLoc - I * xi); // x_(i,j) = x_(i,j) + rand*(p_j - I*x_(i,j))
                    else
                        x[j] = xi + r * (xi - preyLoc); // x_(i,j) = x_(i,j) + rand*(x_(i,j) - p_j)
                }

                task.problem.makeFeasible(x);
                List<Double> xList = new ArrayList<>();
                for (double v : x) {
                    xList.add(v);
                }

                NumberSolution<Double> newCandidatePositionAfterPhase1 = new NumberSolution<>(xList);  // In article this is X_(i)^p1

                if (task.isStopCriterion()) break;
                task.eval(newCandidatePositionAfterPhase1);  // Fitness of candidate position after phase 1

                // Update the ith population member using Equation (5) --> new position for a pelican is accepted if the value of the objective function is improved in that position.
                // Effective updating => the algorithm is prevented from moving to non-optimal areas.
                NumberSolution<Double> positionAfterPhase1 = task.problem.isFirstBetter(newCandidatePositionAfterPhase1, pelicanPosition) ? newCandidatePositionAfterPhase1 : pelicanPosition;  // X_i = Xp1 if F_p < F_i else X_i   Formula (5)

                // PHASE 2: Winding on the water surface (exploitation phase) --------------------------------------
                double[] x2 = positionAfterPhase1.getVariables().stream().mapToDouble(a -> a).toArray();

                // radius of the neighborhood of the population members x_(i,j) to search locally near each member to converge to a better solution
                double radius = R * (1 - (double) t / task.getMaxIterations()); // r = R*(1 - t/T)

                for (int j = 0; j < dimensions; j++) {
                    double r3 = RNG.nextDouble();
                    x2[j] += radius * (2 * r3 - 1) * x2[j]; // Formula 6 --> x_(i,j)^P_2 = x_(i,j) + radius * (2*rand - 1)*x_(i,j)
                }

                task.problem.makeFeasible(x2);

                List<Double> x2List = new ArrayList<>();
                for (double v : x2) {
                    x2List.add(v);
                }

                NumberSolution<Double> positionAfterPhase2 = new NumberSolution<>(x2List);
                if (task.isStopCriterion()) break;
                task.eval(positionAfterPhase2);

                // Equation 7 --> X_i = Xp2 if F_p2 < F_i else X_i - effective updating to accept or reject the new pelican position
                // if position2 < postion1 then select position2 else select position1
                NumberSolution<Double> updatedPelicanPosition = task.problem.isFirstBetter(positionAfterPhase2, positionAfterPhase1) ? positionAfterPhase2 : positionAfterPhase1;

                population[i] = updatedPelicanPosition;

                if (task.problem.isFirstBetter(updatedPelicanPosition, bestSolution)) {
                    bestSolution = updatedPelicanPosition;
                }
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new NumberSolution[populationSize];
        population[0] = task.generateRandomEvaluatedSolution();
        bestSolution = population[0];

        for (int i = 1; i < populationSize; i++) {
            if (task.isStopCriterion())
                break;
            population[i] = task.generateRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(population[i], bestSolution)) {
                bestSolution = population[i];
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
