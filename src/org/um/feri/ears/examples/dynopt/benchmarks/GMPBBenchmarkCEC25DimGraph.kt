package org.um.feri.ears.examples.dynopt.benchmarks

import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.benchmark.SOBenchmark
import org.um.feri.ears.examples.dynopt.utils.Scenario
import org.um.feri.ears.problems.*

class GMPBBenchmarkCEC25DimGraph(val k: Int, val maximization: Boolean = true, val drawLimit: Double = 1e-7) :
    SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm>() {

    init {
        name = "Generalized Moving Peaks Benchmark CEC 2025"
        shortName = "GMPB"
        maxEvaluations = 3000   // the value doesn't matter
        maxIterations = 0   // the value doesn't matter
    }

    override fun initAllProblems() {
        val settings = Scenario.F2.settings  // dummy - just to get settings.FEs
        for (f in listOf(9, 10)) {
        //for (f in listOf(2, 9, 10)) {
            val problemName = "${shortName.lowercase()}-f${f}k${k}" // Must be the same as in the DummyAlgorithm results key (HashMap)
            val problem = DummyProblem(problemName, maximization)
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