package org.um.feri.ears.algorithms.so.random

import org.um.feri.ears.algorithms.AlgorithmInfo
import org.um.feri.ears.algorithms.Author
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.problems.DoubleProblem
import org.um.feri.ears.problems.NumberSolution
import org.um.feri.ears.problems.StopCriterionException
import org.um.feri.ears.problems.Task

class RandomSearch : NumberAlgorithm() {

    // needs to extend NumberAlgorithm
    private var best: NumberSolution<Double?>? = null // used to save global best solution

    fun RandomSearch() {
        ai = AlgorithmInfo("RW", "Random Walk", "")
        au = Author("Matej", "matej.crepinsek@um.si")
    }

    @Throws(StopCriterionException::class)
    override fun execute(task: Task<NumberSolution<Double?>, DoubleProblem>): NumberSolution<Double?>? { // method which executes the algorithm
        // the task object holds information about the stopping criterion
        // and information about the problem (number of dimensions, number of constraints, upper and lower bounds...)
        var newSolution: NumberSolution<Double?>
        best =
            task.randomEvaluatedSolution // generate new random solution (number of evaluations is incremented automatically)
        // to evaluate a custom solution or an array use task.eval(mySolution)
        while (!task.isStopCriterion) { // run until the stopping criterion is not reached

            newSolution = task.randomEvaluatedSolution
            if (task.problem.isFirstBetter(
                    newSolution,
                    best
                )
            ) { // use method isFirstBetter to check which solution is better (it checks constraints and considers the type of the problem (minimization or maximization))
                best = newSolution
            }
            task.incrementNumberOfIterations() // increase the number of generations for each iteration of the main loop
        }
        return best // return the best solution found
    }

    override fun resetToDefaultsBeforeNewRun() {
    }
}