package org.um.feri.ears.problems.real_world

import org.um.feri.ears.problems.DoubleProblem
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Protein Folding Optimization (PFO) problem
 *
 * A complex benchmark problem simulating protein folding on a
 * three-dimensional AB off-lattice model.
 *
 *
 * STILLINGER, Frank H.; HEAD-GORDON, Teresa; HIRSHFELD, Catherine L.
 * Toy model for protein folding
 * Physical review E, 1993, 48.2: 1469
 *
 * BOŠKOVIĆ, Borko; BREST, Janez
 * Two-phase protein folding optimization on a three-dimensional AB off-lattice model
 * Swarm and Evolutionary Computation, 2020, 57: 100708.
 */
class PFO(
    protein: Protein
) : DoubleProblem(
    protein.name,
    2 * INSTANCES[protein]!!.sequence.length - 5,
    1,
    1,
    0
) {
    enum class Protein {
        P_1BXP,
        P_1CB3,
        P_1BXL,
        P_1EDP,
        P_2ZNF,
        P_1EDN,
        P_2H3S,
        P_1ARE,
        P_2KGU,
        P_1TZ4,
        P_1TZ5,
        P_1AGT,
        P_1CRN,
        P_2KAP,
        P_1HVV,
        P_1GK4,
        P_F13,
        P_F21,
        P_F34,
        P_F55
    }

    private data class PFOSequence(
        val sequence: String,
        val bestEnergy: Double,
    )

    private companion object {
        private val INSTANCES = mapOf(
            Protein.P_1BXP to PFOSequence(
                sequence = "ABBBBBBABBBAB",
                bestEnergy = -5.6104
            ),
            Protein.P_1CB3 to PFOSequence(
                sequence = "BABBBAABBAAAB",
                bestEnergy = -8.4589
            ),
            Protein.P_1BXL to PFOSequence(
                sequence = "ABAABBAAAAABBABB",
                bestEnergy = -17.3962
            ),
            Protein.P_1EDP to PFOSequence(
                sequence = "ABABBAABBBAABBABA",
                bestEnergy = -15.0092
            ),
            Protein.P_2ZNF to PFOSequence(
                sequence = "ABABBAABBABAABBABA",
                bestEnergy = -18.3402
            ),
            Protein.P_1EDN to PFOSequence(
                sequence = "ABABBAABBBAABBABABAAB",
                bestEnergy = -21.4703
            ),
            Protein.P_2H3S to PFOSequence(
                sequence = "AABBAABBBBBABBBABAABBBBBB",
                bestEnergy = -21.1519
            ),
            Protein.P_1ARE to PFOSequence(
                sequence = "BBBAABAABBABABBBAABBBBBBBBBBB",
                bestEnergy = -25.2800
            ),
            Protein.P_2KGU to PFOSequence(
                sequence = "ABAABBAABABBABAABAABABABABABAAABBB",
                bestEnergy = -52.7165
            ),
            Protein.P_1TZ4 to PFOSequence(
                sequence = "BABBABBAABBAAABBAABBAABABBBABAABBBBBB",
                bestEnergy = -43.0229
            ),
            Protein.P_1TZ5 to PFOSequence(
                sequence = "AAABAABAABBABABBAABBBBAABBBABAABBABBB",
                bestEnergy = -49.3868
            ),
            Protein.P_1AGT to PFOSequence(
                sequence = "AAAABABABABABAABAABBAAABBABAABBBABABAB",
                bestEnergy = -65.1990
            ),
            Protein.P_1CRN to PFOSequence(
                sequence = "BBAAABAAABBBBBAABAAABABAAAABBBAAAAAAAABAAABBAB",
                bestEnergy = -92.9853
            ),
            Protein.P_2KAP to PFOSequence(
                sequence = "BBAABBABABABABBABABBBBABAABABAABBBBBBABBBAABAAABBABBABBAAAAB",
                bestEnergy = -85.5099
            ),
            Protein.P_1HVV to PFOSequence(
                sequence = "BAABBABBBBBBAABABBBABBABBABABAAAAABBBABAABBABBBABBAABBABBAABBBBBAABBBBBABBB",
                bestEnergy = -95.4475
            ),
            Protein.P_1GK4 to PFOSequence(
                sequence = "ABABAABABBBBABBBABBABBBBAABAABBBBBAABABBBABBABBBAABBABBBBBAABABAAABABAABBBBAABABBBBA",
                bestEnergy = -106.4190
            ),
            Protein.P_F13 to PFOSequence(
                sequence = "ABBABBABABBAB",
                bestEnergy = -6.9961
            ),
            Protein.P_F21 to PFOSequence(
                sequence = "BABABBABABBABBABABBAB",
                bestEnergy = -16.5544
            ),
            Protein.P_F34 to PFOSequence(
                sequence = "ABBABBABABBABBABABBABABBABBABABBAB",
                bestEnergy = -31.3455
            ),
            Protein.P_F55 to PFOSequence(
                sequence = "BABABBABABBABBABABBABABBABBABABBABBABABBABABBABBABABBAB",
                bestEnergy = -51.9030
            ),
        )
    }

    private data class PFOPoint(var x: Double, var y: Double, var z: Double)

    private val sequence = INSTANCES[protein]!!.sequence
    private val l = sequence.length

    init {
        lowerLimit = ArrayList(List(numberOfDimensions) { -PI })
        upperLimit = ArrayList(List(numberOfDimensions) { PI })
        objectiveSpaceOptima[0] = INSTANCES[protein]!!.bestEnergy
    }

    override fun eval(x: DoubleArray): Double {
        // Eval must be thread-safe, so we must reallocate every time
        val points: Array<PFOPoint> = Array(l) { PFOPoint(0.0, 0.0, 0.0) }
        val cosTheta = DoubleArray(l - 2) { 0.0 }

        for (i in 0 until l - 2) {
            cosTheta[i] = cos(x[i])
        }

        points[1].y = 1.0
        points[2].x = cosTheta[0]
        points[2].y = 1.0 + sin(x[0])

        for (i in 3 until l) {
            points[i].x = points[i - 1].x + cosTheta[i - 2] * cos(x[l - 2 + i - 3])
            points[i].y = points[i - 1].y + sin(x[i - 2]) * cos(x[l - 2 + i - 3])
            points[i].z = points[i - 1].z + sin(x[l - 2 + i - 3])
        }

        var e1 = 0.0
        var e2 = 0.0

        for (i in 0 until l - 2) {
            e1 += 1 - cosTheta[i]

            for (j in i + 2 until l) {
                val c = when (sequence[i].code + sequence[j].code) {
                    2 * 'A'.code -> 1.0
                    'A'.code + 'B'.code -> -0.5
                    2 * 'B'.code -> 0.5
                    else -> throw IllegalStateException("Invalid amino acid pair")
                }

                val xDiff = points[i].x - points[j].x
                val yDiff = points[i].y - points[j].y
                val zDiff = points[i].z - points[j].z

                val d = (xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff)
                val p = 1.0 / (d * d * d)

                e2 += (p * p) - (c * p)
            }
        }

        return (e1 / 4.0) + (4.0 * e2)
    }
}