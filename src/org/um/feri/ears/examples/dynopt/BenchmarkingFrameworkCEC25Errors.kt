package org.um.feri.ears.examples.dynopt

import org.um.feri.ears.algorithms.DummyAlgorithm
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25All
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25Dim
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25Frequency
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25Peaks
import org.um.feri.ears.examples.dynopt.benchmarks.GMPBBenchmarkCEC25Severity
import org.um.feri.ears.examples.dynopt.utils.Scenario
import java.io.File

fun main() {
    val algResultsDir = "D:${File.separator}EDOLAB-MATLAB${File.separator}Utility${File.separator}CEC2025${File.separator}Results${File.separator}" +
            "Benchmarking"

    // Available algorithms in the results folder:
    // "ACFPSO", "AMPPSO_BC", "AMPPSO_GI", "DPPSO", "DUS", "PSPSO", "SPSO_AP_AD"

    val algorithms = listOf("ACFPSO", "AMPPSO_BC", "AMPPSO_GI", "DPPSO", "DUS", "PSPSO", "SPSO_AP_AD")
    val players = algorithms.map { DummyAlgorithm(it, algResultsDir, DummyAlgorithm.FileFormat.CEC_RESULTS_FORMAT) }
        .toCollection(ArrayList<NumberAlgorithm>())

    val benchmark = GMPBBenchmarkCEC25Severity(maximization = false).apply {
        isDisplayRatingCharts = false
        setDisplayAdvancedStats(false)
        addAlgorithms(players)
        run(31)
    }

    val tournamentResults = benchmark.tournamentResults
    tournamentResults.displayResults(true, true);

    /*val playersResults = tournamentResults.players
    for (player in playersResults) {
        println(player.id)
        for (n in 1..12) {
            var counter = 0;
            for (problem in player.wldProblems) {
                if (problem.key.startsWith("gmpb-f${n}k")) {
                    counter += problem.value.lose
                }
            }
            println("F${n}: $counter")
        }
    }*/}