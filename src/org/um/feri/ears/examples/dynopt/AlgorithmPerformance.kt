package org.um.feri.ears.examples.dynopt

class AlgorithmPerformance(val name: String) {
    val evalData: LinkedHashMap<String, EvalMetrics> = LinkedHashMap()

    fun addRating(evalNumber: String, rating: Double, ratingDeviation: Double) {
        evalData[evalNumber] = EvalMetrics(evalNumber, rating, ratingDeviation)
    }

    fun setAvgFitness(evalNumber: String, avgFitness: Double) {
        evalData[evalNumber]?.avgFitness = avgFitness
    }

    fun setFitnessDeviation(evalNumber: String, stdDeviation: Double) {
        evalData[evalNumber]?.fitnessDeviation = stdDeviation
    }

    data class EvalMetrics(
        val evalNumber: String,
        val rating: Double,
        val ratingDeviation: Double,
        var avgFitness: Double = 0.0, // average over all runs
        var fitnessDeviation: Double = 0.0
    )
}