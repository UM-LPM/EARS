package org.um.feri.ears.examples.dynopt.wbo25

import org.um.feri.ears.algorithms.DummyAlgorithm
import org.um.feri.ears.algorithms.NumberAlgorithm
import java.io.File

fun main() {
    val algResultsDir =
        "D:${File.separator}" +
                "EDOLAB-MATLAB${File.separator}" +
                "Results${File.separator}" +
                "Comparison${File.separator}" +
                "WBO25${File.separator}" +
                "MPB_Peaks10_ChangeFrequency5000_D5_ShiftSeverity1_Environments100"

    val players = arrayListOf<NumberAlgorithm>(
        DummyAlgorithm("ACFPSO", algResultsDir),
        DummyAlgorithm("AMPDE", algResultsDir),
        DummyAlgorithm("CPSO", algResultsDir)
    )

    val benchmark = AdaptedMPBBenchmark()
    benchmark.addAlgorithms(players)
    benchmark.run(31)

    val tournamentResults = benchmark.tournamentResults
    tournamentResults.players.forEach { player ->
        println(
            "Player: ${player.id}, Rating: ${player.glicko2Rating.rating}, Rating Deviation: ${player.glicko2Rating.ratingDeviation}," +
                    " Wins: ${player.sumWinLossDraw.win}, Losses: ${player.sumWinLossDraw.lose}, Draws: ${player.sumWinLossDraw.draw}"
        )
    }
}