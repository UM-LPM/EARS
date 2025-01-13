package org.um.feri.ears.examples.dynopt

import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.benchmark.SOBenchmark
import org.um.feri.ears.problems.*

class GMPBBenchmark(val evaluationNumber: Int, val drawLimit: Double = 1e-7) :
    SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm>() {

    init {
        name = "Generalized Moving Peaks Benchmark"
        shortName = "GMPB"
        maxEvaluations = 3000   // the value doesn't matter
        maxIterations = 0   // the value doesn't matter
    }

    override fun initAllProblems() {
        val problemName = shortName + "Eval" + evaluationNumber
        val problem = DummyProblem(problemName, true)
        problem.objectiveMaximizationFlags[0] = true
        addTask(problem, stopCriterion, maxEvaluations, 0, maxIterations)
    }

    override fun addTask(
        problem: DoubleProblem?,
        stopCriterion: StopCriterion?,
        maxEvaluations: Int,
        time: Long,
        maxIterations: Int
    ) {
        tasks.add(
            Task<NumberSolution<Double>, DoubleProblem>(
                problem,
                stopCriterion,
                maxEvaluations,
                time,
                maxIterations
            )
        )
    }
}