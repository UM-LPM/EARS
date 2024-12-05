package org.um.feri.ears.algorithms.so.rfoa

import org.um.feri.ears.algorithms.AlgorithmInfo
import org.um.feri.ears.algorithms.Author
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.problems.DoubleProblem
import org.um.feri.ears.problems.NumberSolution
import org.um.feri.ears.problems.StopCriterionException
import org.um.feri.ears.problems.Task
import org.um.feri.ears.util.SolutionUtils
import org.um.feri.ears.util.annotation.AlgorithmParameter
import org.um.feri.ears.util.comparator.ProblemComparator
import org.um.feri.ears.util.random.RNG
import kotlin.math.cos
import kotlin.math.sin

class RFOA(popSize: Int = 10) : NumberAlgorithm() {
    @AlgorithmParameter(name = "population size")
    private var popSize = 0

    private lateinit var foxes: ArrayList<NumberSolution<Double>> // population
    private lateinit var best: NumberSolution<Double> // best global solution

    init {
        this.popSize = popSize
        ai = AlgorithmInfo("RFOA", "Red Fox Optimization Algorithm", "Dawid Połap, Marcin Woźniak")
        au = Author("Zan", "zan.bedrac@student.um.si")
    }

    @Throws(StopCriterionException::class)
    override fun execute(task: Task<NumberSolution<Double?>, DoubleProblem>): NumberSolution<Double> {
        this.task = task
        initPopulation()
        foxes.sortWith(ProblemComparator<NumberSolution<Double>>(task.problem))

        while (!task.isStopCriterion()) {
            reproduction()
            foodSearching()
            localHabitat()
            task.incrementNumberOfIterations()
        }

        return best
    }

    /**
     * Generates an initial population of foxes with random positions within bounds.
     */
    private fun initPopulation() {
        foxes = ArrayList(popSize)

        for (i in 0 until popSize) {
            val fox = task.getRandomEvaluatedSolution()
            foxes.add(fox)
            updateBestFox(fox)
            if (task.isStopCriterion()) return
        }
    }

    /**
     * Last 5% of the population leaves the herd and reproduces with
     * the best two foxes in the population with factor κ (less than 1).
     */
    private fun reproduction() {
        val fromIndex = (popSize - 0.05 * popSize).toInt()

        for (i in fromIndex until popSize) {
            val center = calculateHabitatCenter()
            val κ = RNG.nextDouble()

            for (d in 0 until task.problem.numberOfDimensions) {
                if (task.isStopCriterion()) return

                val (lower, upper) = task.problem.getLowerLimit()[d] to task.problem.getUpperLimit()[d]
                val newValue = if (κ >= 0.45) RNG.nextDouble(lower, upper) else κ * center[d]

                val solution = task.problem.setFeasible(newValue, d)
                foxes[i].setValue(d, solution)
            }
            if(task.isStopCriterion()) return
            task.eval(foxes[i])
            updateBestFox(foxes[i])
        }
    }

    /**
     * Foxes search for food in the habitat by moving towards the best fox.
     */
    private fun foodSearching() {
        val bestFox = best.copy()

        for (i in 0 until popSize) {
            val α = RNG.nextDouble(0.0, SolutionUtils.calculateEuclideanDistance(foxes[i], bestFox))

            for (d in 0 until task.problem.numberOfDimensions) {
                if (task.isStopCriterion()) return

                val direction = if (bestFox.getValue(d) - foxes[i].getValue(d) < 0) -1.0 else 1.0
                val newValue = foxes[i].getValue(d) + α * direction

                val solution = task.problem.setFeasible(newValue, d)
                foxes[i].setValue(d, solution)
            }

            if(task.isStopCriterion()) return

            task.eval(foxes[i])
            updateBestFox(foxes[i])
        }
    }

    /**
     * Foxes traverse through the local habitat by moving in a random direction.
     *
     * For each fox:
     * - Randomly select a fox from the population with a probability of 25 %.
     * - Generate a random angle φ (max. 360 ° -> 2π) for each dimension.
     * - Calculate the radius r using the formula: r = a * sin(φ) / φ.
     */
    private fun localHabitat() {
        var a = RNG.nextDouble(0.0, 0.2)

        for (i in foxes.indices) {
            if (RNG.nextDouble() <= 0.75) continue

            val φ = DoubleArray(task.problem.numberOfDimensions) { RNG.nextDouble(0.0, 2 * Math.PI) }
            val r = if (φ[0] != 0.0) a * sin(φ[0]) / φ[0] else RNG.nextDouble()

            for (d in 0 until task.problem.numberOfDimensions) {
                if (task.isStopCriterion()) return

                val (lower, upper) = task.problem.getLowerLimit()[d] to task.problem.getUpperLimit()[d]
                val angle = if (d == 0) cos(φ[d]) else sin(φ[d])

                for (k in 0 until d + 1) {
                    a = foxes[i].getValue(d) + a * r * angle
                    val b = foxes[i].getValue(d) - a * r * angle

                    val newValue = if (a in lower..upper) a else b
                    val solution = task.problem.setFeasible(newValue, d)
                    foxes[i].setValue(d, solution)
                }
            }
            if(task.isStopCriterion()) return

            task.eval(foxes[i])
            updateBestFox(foxes[i])
        }
    }

    /**
     * Calculates the habitat center by averaging the two best foxes in each dimension.
     */
    private fun calculateHabitatCenter(): DoubleArray {
        val center = DoubleArray(task.problem.numberOfDimensions)
        for (d in 0 until task.problem.numberOfDimensions)
            center[d] = (foxes[0].getValue(d) + foxes[1].getValue(d)) / 2.0

        return center
    }

    /**
     * Updates the best fox if the new provided fox is better.
     */
    private fun updateBestFox(fox: NumberSolution<Double>) {
        if (!::best.isInitialized || task.problem.isFirstBetter(fox, best)) best = fox
    }

    override fun resetToDefaultsBeforeNewRun() {
    }
}
