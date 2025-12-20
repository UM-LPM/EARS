package org.um.feri.ears.examples.dynopt.benchmarks

import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.benchmark.SOBenchmark
import org.um.feri.ears.problems.DoubleProblem
import org.um.feri.ears.problems.DummyProblem
import org.um.feri.ears.problems.NumberSolution
import org.um.feri.ears.problems.StopCriterion
import org.um.feri.ears.problems.Task

class MPBBenchmarkExample(val k: Int, val drawLimit: Double = 1e-7) :
    SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm>() {

    init {
        name = "Moving Peaks Benchmark"
        shortName = "MPB"
        maxEvaluations = 3000   // the value doesn't matter
        maxIterations = 0       // the value doesn't matter
    }

    override fun initAllProblems() {
        val problemName = "${shortName.lowercase()}k${k}" // Must be the same as in the DummyAlgorithm results key (HashMap)
        val problem = DummyProblem(problemName, true)
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
                time, maxIterations
            )
        )
    }
}