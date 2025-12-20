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

    val algResultsDir = "D:${File.separator}EDOLAB-MATLAB${File.separator}Results${File.separator}Moravec${File.separator}CEC2025${File.separator}" +
            "GMPB_Peaks${currentSettings.peaks}_" +
            "ChangeFrequency${currentSettings.changeFrequency}_" +
            "ShiftSeverity${currentSettings.shiftSeverity}_" +
            "Environments${currentSettings.environments}_" +
            "Dim${currentSettings.dimensions}"

    // Available algorithms in the results folder:
    // "ACFPSO", "AMPDE", "AMPPSO", "AmQSO", "AMSO", "APCPSO", "AMPPSO_BC", "AMPPSO_GI", "CDE",
    // "CESO", "CPSO", "CPSOR", "DCPSO", "DSPSO", "DynDE", "DynPopDE", "FTMPSO", "HmSO", "IDSPSO",
    // "ImQSO", "mCMAES", "mDE","mjDE", "mPSO", "mQSO", "psfNBC", "RPSO", "SPSO_AP_AD", "TMIPSO"

    //val algorithms = listOf("AMPPSO_BC", "AMPPSO_GI", "SPSO_AP_AD")
    val algorithms = listOf("AMPPSO_BC", "AMPPSO_GI", "SPSO_AP_AD", "AMPPSO", "DynPopDE", "DSPSO", "mjDE", "mQSO")
    val algorithmsPerformances = algorithms.map { AlgorithmPerformance(name = it) }
    val players = algorithms.map { DummyAlgorithm(it, algResultsDir, DummyAlgorithm.FileFormat.CEC_RESULTS_FORMAT) }
        .toCollection(ArrayList<NumberAlgorithm>())

    val benchmark = GMPBBenchmarkCEC25(currentSettings).apply {
        isDisplayRatingCharts = false
        setDisplayAdvancedStats(false)
        addAlgorithms(players)
        run(currentSettings.numRuns)
    }

    val tournamentResults = benchmark.tournamentResults
    /*tournamentResults.players.forEach { player ->
        algorithmsPerformances.firstOrNull { it.name == player.id }?.apply {
            evalMetrics.add(EvalMetrics(player.glicko2Rating.rating, player.glicko2Rating.ratingDeviation))
        }
        println(
            "Player: ${player.id}, Rating: ${player.glicko2Rating.rating}, Rating Deviation: ${player.glicko2Rating.ratingDeviation}," +
                    " Wins: ${player.sumWinLossDraw.win}, Losses: ${player.sumWinLossDraw.lose}, Draws: ${player.sumWinLossDraw.draw}"
        )
    }*/
    tournamentResults.displayResults(true, true);
}