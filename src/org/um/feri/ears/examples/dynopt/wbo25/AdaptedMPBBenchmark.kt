package org.um.feri.ears.examples.dynopt.wbo25

import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.benchmark.SOBenchmark
import org.um.feri.ears.problems.DoubleProblem
import org.um.feri.ears.problems.DummyProblem
import org.um.feri.ears.problems.NumberSolution
import org.um.feri.ears.problems.StopCriterion
import org.um.feri.ears.problems.Task

class AdaptedMPBBenchmark(val drawLimit: Double = 1e-7) :
    SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm>() {

    init {
        name = "Adapted Moving Peaks Benchmark"
        shortName = "aMPB"
        maxEvaluations = 3000   // the value doesn't matter
        maxIterations = 0   // the value doesn't matter
    }

    override fun initAllProblems() {
        for (eval in 5000..500000 step 5000) {
            val problemName = "MPBEval${eval}"
            val problem = DummyProblem(problemName, true)
            problem.objectiveMaximizationFlags[0] = true
            addTask(problem, stopCriterion, maxEvaluations, 0, maxIterations)
        }
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