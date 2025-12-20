package org.um.feri.ears.examples.dynopt.benchmarks

import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.benchmark.SOBenchmark
import org.um.feri.ears.examples.dynopt.utils.Scenario
import org.um.feri.ears.problems.*
import kotlin.math.max

class GMPBBenchmarkCEC25Frequency(val maximization: Boolean = true, val drawLimit: Double = 1e-7) :
    SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm>() {

    init {
        name = "Generalized Moving Peaks Benchmark CEC 2025"
        shortName = "GMPB"
        maxEvaluations = 3000   // the value doesn't matter
        maxIterations = 0   // the value doesn't matter
    }

    override fun initAllProblems() {
        val settingsF2 = Scenario.F2.settings  // dummy - just to get settings.FEs
        for (k in 0 until settingsF2.FEs.size) {
            if (settingsF2.FEs[k] % settingsF2.changeFrequency == 0) {
                val problemName = "${shortName.lowercase()}-f2k${k}" // Must be the same as in the DummyAlgorithm results key (HashMap)
                val problem = DummyProblem(problemName, maximization)
                addTask(problem, stopCriterion, maxEvaluations, 0, maxIterations)
            }
        }
        val settingsF6 = Scenario.F6.settings  // dummy - just to get settings.FEs
        for (k in 0 until settingsF6.FEs.size) {
            if (settingsF6.FEs[k] % settingsF6.changeFrequency == 0) {
                val problemName = "${shortName.lowercase()}-f6k${k}" // Must be the same as in the DummyAlgorithm results key (HashMap)
                val problem = DummyProblem(problemName, maximization)
                addTask(problem, stopCriterion, maxEvaluations, 0, maxIterations)
            }
        }
        val settingsF7 = Scenario.F7.settings  // dummy - just to get settings.FEs
        for (k in 0 until settingsF7.FEs.size) {
            if (settingsF7.FEs[k] % settingsF7.changeFrequency == 0) {
                val problemName = "${shortName.lowercase()}-f7k${k}" // Must be the same as in the DummyAlgorithm results key (HashMap)
                val problem = DummyProblem(problemName, maximization)
                addTask(problem, stopCriterion, maxEvaluations, 0, maxIterations)
            }
        }
        val settingsF8 = Scenario.F8.settings  // dummy - just to get settings.FEs
        for (k in 0 until settingsF8.FEs.size) {
            if (settingsF8.FEs[k] % settingsF8.changeFrequency == 0) {
                val problemName = "${shortName.lowercase()}-f8k${k}" // Must be the same as in the DummyAlgorithm results key (HashMap)
                val problem = DummyProblem(problemName, maximization)
                addTask(problem, stopCriterion, maxEvaluations, 0, maxIterations)
            }
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