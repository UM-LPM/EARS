package org.um.feri.ears.algorithms.so.random;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class RandomSearch extends NumberAlgorithm { // needs to extend NumberAlgorithm
    private NumberSolution<Double> best; // used to save global best solution

    public RandomSearch() {
        ai = new AlgorithmInfo("RS", "Random Search", "");
        au = new Author("Matej", "matej.crepinsek@um.si");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException { // method which executes the algorithm
        // the task object holds information about the stopping criterion
        // and information about the problem (number of dimensions, number of constraints, upper and lower bounds...)
        NumberSolution<Double> newSolution;
        best = task.generateRandomEvaluatedSolution();  // generate new random solution (number of evaluations is incremented automatically)
        // to evaluate a custom solution or an array use task.eval(mySolution)
        while (!task.isStopCriterion()) { // run until the stopping criterion is not reached

            newSolution = task.generateRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(newSolution, best)) { // use method isFirstBetter to check which solution is better (it checks constraints and considers the type of the problem (minimization or maximization))
                best = newSolution;
            }
            task.incrementNumberOfIterations(); // increase the number of generations for each iteration of the main loop
        }
        return best; // return the best solution found
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
