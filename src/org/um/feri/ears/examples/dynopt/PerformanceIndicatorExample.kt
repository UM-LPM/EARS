package org.um.feri.ears.examples.dynopt

import org.um.feri.ears.algorithms.DummyAlgorithm
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.benchmark.Benchmark
import org.um.feri.ears.examples.dynopt.benchmarks.MPBBenchmarkIndicatorExample
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
    val players = algorithms.map { DummyAlgorithm(it, algResultsDir, DummyAlgorithm.FileFormat.CEC_RESULTS_FORMAT) }
        .toCollection(ArrayList<NumberAlgorithm>())

    players[0].id = "Algorithm A"
    players[1].id = "Algorithm B"
    players[2].id = "Algorithm C"

    Benchmark.printInfo = true
    val benchmark = MPBBenchmarkIndicatorExample().apply {
        isDisplayRatingCharts = true
        setDisplayAdvancedStats(false)
        addAlgorithms(players)
        run(currentSettings.numRuns)
    }

    val tournamentResults = benchmark.tournamentResults
    tournamentResults.displayResults(true, true);
}