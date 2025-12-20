package org.um.feri.ears.examples.dynopt

import org.um.feri.ears.algorithms.DummyAlgorithm
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmark
import org.um.feri.ears.examples.dynopt.benchmarks.MPBBenchmarkExample
import org.um.feri.ears.examples.dynopt.utils.AlgorithmPerformance
import org.um.feri.ears.examples.dynopt.utils.ComparisonSettings
import org.um.feri.ears.examples.dynopt.utils.EvalMetrics
import org.um.feri.ears.examples.dynopt.utils.LetsPlotUtils
import org.um.feri.ears.examples.dynopt.utils.Scenario
import java.io.File

fun main() {
    val currentSettings = ComparisonSettings(31, 1000, 50, 10, 5000, 5, 3)

    val algResultsDir = "D:${File.separator}EDOLAB-MATLAB${File.separator}Results${File.separator}Moravec${File.separator}MethodologyExplanation${File.separator}" +
            "SampleInterval${currentSettings.sampleInterval}${File.separator}" +
            "MPB_Peaks${currentSettings.peaks}_" +
            "ChangeFrequency${currentSettings.changeFrequency}_" +
            "ShiftSeverity${currentSettings.shiftSeverity}_" +
            "Environments${currentSettings.environments}_" +
            "Dim${currentSettings.dimensions}"

    // Available algorithms in the results folder:
    // "ACFPSO", "AMPDE", "AMPPSO", "AmQSO", "AMSO", "APCPSO", "AMPPSO_BC", "AMPPSO_GI", "CDE",
    // "CESO", "CPSO", "CPSOR", "DCPSO", "DSPSO", "DynDE", "DynPopDE", "FTMPSO", "HmSO", "IDSPSO",
    // "ImQSO", "mCMAES", "mDE","mjDE", "mPSO", "mQSO", "psfNBC", "RPSO", "SPSO_AP_AD", "TMIPSO"

    val algorithms = listOf("AMPDE", "DynPopDE", "mQSO")
    val algorithmsPerformances = algorithms.map { AlgorithmPerformance(name = it) }
    val players = algorithms.map { DummyAlgorithm(it, algResultsDir, DummyAlgorithm.FileFormat.CEC_RESULTS_FORMAT) }
        .toCollection(ArrayList<NumberAlgorithm>())

    for (k in 0 until currentSettings.FEs.size) {
        val benchmark = MPBBenchmarkExample(k).apply {
            isDisplayRatingCharts = false
            setDisplayAdvancedStats(false)
            addAlgorithms(players)
            run(currentSettings.numRuns)
        }
        val tournamentResults = benchmark.tournamentResults
        tournamentResults.players.forEach { player ->
            algorithmsPerformances.firstOrNull { it.name == player.id }?.apply {
                evalMetrics.add(EvalMetrics(player.glicko2Rating.rating, player.glicko2Rating.ratingDeviation))
            }
        }
    }

    // == generate plot ==
    val envFrom = 1
    val envTo = 3

    LetsPlotUtils.generatePlot(
        comparisonSettings = currentSettings,
        algorithms = algorithmsPerformances,
        width = 1500,
        height = 750,
        minX = if (envFrom == 1) 0 else currentSettings.envIndexes[envFrom - 2],
        maxX = currentSettings.envIndexes[envTo - 1],   // indexes (1st evaluation index = 0, 51st evaluation index (change) = 51, 102nd evaluation index (change) = 102, etc.)
        showVerticalLines = true,
        filename = "RCG_Example_SampleInterval${currentSettings.sampleInterval}.png"
    )
}