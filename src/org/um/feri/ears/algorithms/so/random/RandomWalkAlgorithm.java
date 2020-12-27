package org.um.feri.ears.algorithms.so.random;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class RandomWalkAlgorithm extends Algorithm { // needs to extend Algorithm
    private DoubleSolution best; // used to save global best solution

    public RandomWalkAlgorithm() {
        ai = new AlgorithmInfo("RWSi", "", "RWSi", "Random Walk Simple"); // add algorithm name
        au = new Author("Matej", "matej.crepinsek at um.si"); // add author information
    }

    @Override
    public DoubleSolution execute(Task task) throws StopCriterionException { // method which executes the algorithm
        // the task object holds information about the stopping criterion
        // and information about the problem (number of dimensions, number of constraints, upper and lower bounds...)
        DoubleSolution newSolution;
        best = task.getRandomEvaluatedSolution();  // generate new random solution (number of evaluations is incremented automatically)
        // to evaluate a custom solution or an array use task.eval(mySolution)
        while (!task.isStopCriterion()) { // run until the stopping criterion is not reached

            newSolution = task.getRandomEvaluatedSolution();
            if (task.isFirstBetter(newSolution, best)) { // use method isFirstBetter to check which solution is better (it checks constraints and considers the type of the problem (minimization or maximization))
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
