package org.um.feri.ears.examples.dynopt

import org.um.feri.ears.algorithms.DummyAlgorithm
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25
import org.um.feri.ears.examples.dynopt.utils.AlgorithmPerformance
import org.um.feri.ears.examples.dynopt.utils.EvalMetrics
import org.um.feri.ears.examples.dynopt.utils.LetsPlotUtils
import org.um.feri.ears.examples.dynopt.utils.Scenario
import java.io.File

fun main() {
    val currentSettings = Scenario.F10.settings

    val algResultsDir = "D:${File.separator}EDOLAB-MATLAB${File.separator}Utility${File.separator}CEC2025${File.separator}Results${File.separator}" +
            "GMPB_Peaks${currentSettings.peaks}_" +
            "ChangeFrequency${currentSettings.changeFrequency}_" +
            "ShiftSeverity${currentSettings.shiftSeverity}_" +
            "Environments${currentSettings.environments}_" +
            "Dim${currentSettings.dimensions}"

    // Available algorithms in the results folder:
    // "ACFPSO", "AMPPSO_BC", "AMPPSO_GI", "DPPSO", "DUS", "PSPSO", "SPSO_AP_AD"

    val algorithms = listOf("ACFPSO", "AMPPSO_BC", "AMPPSO_GI", "DPPSO", "DUS", "PSPSO", "SPSO_AP_AD")
    val players = algorithms.map { DummyAlgorithm(it, algResultsDir, DummyAlgorithm.FileFormat.CEC_RESULTS_FORMAT) }
        .toCollection(ArrayList<NumberAlgorithm>())

    val benchmark = GMPBBenchmarkCEC25(currentSettings, maximization = false).apply {
        isDisplayRatingCharts = false
        setDisplayAdvancedStats(false)
        addAlgorithms(players)
        run(currentSettings.numRuns)
    }

    val tournamentResults = benchmark.tournamentResults
    tournamentResults.displayResults(true, true);
}