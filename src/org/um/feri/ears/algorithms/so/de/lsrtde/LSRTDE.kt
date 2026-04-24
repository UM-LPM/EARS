package org.um.feri.ears.algorithms.so.lsrtde

import org.um.feri.ears.algorithms.AlgorithmInfo
import org.um.feri.ears.algorithms.Author
import org.um.feri.ears.algorithms.NumberAlgorithm
import org.um.feri.ears.problems.DoubleProblem
import org.um.feri.ears.problems.NumberSolution
import org.um.feri.ears.problems.Task
import org.um.feri.ears.util.Util
import org.um.feri.ears.util.annotation.AlgorithmParameter
import org.um.feri.ears.util.random.RNG
import kotlin.math.*

class LSRTDE @JvmOverloads constructor(
    @AlgorithmParameter(name = "population size")
    private val popSize: Int = 30
) : NumberAlgorithm() {

    private var memorySize = 5
    private var memoryIter = 0
    private var successFilled = 0
    private var memoryCurrentIndex = 0

    private var nVars = 0
    private var nIndsCurrent = 0
    private var nIndsFront = 0
    private var nIndsFrontMax = 0
    private var newNIndsFront = 0
    private var populSize = 0

    private var pfIndex = 0

    private var bestfit = Double.POSITIVE_INFINITY
    private var successRate = 0.5
    private lateinit var globalBestVec: DoubleArray
    private var globalBestFit = Double.POSITIVE_INFINITY

    private var F = 0.0
    private var Cr = 0.0

    private lateinit var upperBound: DoubleArray
    private lateinit var lowerBound: DoubleArray

    private lateinit var population: Array<DoubleArray>
    private lateinit var populationFront: Array<DoubleArray>
    private lateinit var populationTemp: Array<DoubleArray>
    private lateinit var fitArr: DoubleArray
    private lateinit var fitArrCopy: DoubleArray
    private lateinit var fitArrFront: DoubleArray
    private lateinit var tempSuccessCr: DoubleArray
    private lateinit var memoryCr: DoubleArray
    private lateinit var fitDelta: DoubleArray
    private lateinit var weights: DoubleArray
    private lateinit var trial: DoubleArray
    private lateinit var indices: IntArray
    private lateinit var indices2: IntArray

    private var theChosenOne = 0

    private lateinit var task: Task<NumberSolution<Double>, DoubleProblem>

    init {
        au = Author("Liam Mesarec", "liam.mesarec@student.um.si")
        ai = AlgorithmInfo(
            "L-SRTDE", "Linear population size reduction Success Rate-based adaptive Differential Evolution",
            "@inproceedings{author2024lsrtde," +
                    "  title={Success Rate-based Adaptive Differential Evolution (L-SRTDE) for CEC 2024 Competition}," +
                    "  author={Author, Firstname and Author, Secondname}," +
                    "  booktitle={Proceedings of the IEEE Congress on Evolutionary Computation (CEC 2024)}," +
                    "  pages={1--8}," +
                    "  year={2024}," +
                    "  organization={IEEE}" +
                    "}"
        )
    }

    override fun execute(task: Task<NumberSolution<Double>, DoubleProblem>): NumberSolution<Double> {
        this.task = task
        lowerBound = task.problem.lowerLimit.toDoubleArray()
        upperBound = task.problem.upperLimit.toDoubleArray()

        initialize()
        mainLoop()

        println(globalBestFit)
        val out = NumberSolution(Util.toDoubleArrayList(globalBestVec))
        out.setObjective(0, globalBestFit)
        return out
    }

    private fun initialize() {
        val dims = task.problem.numberOfDimensions
        nVars = dims
        val newNInds = popSize * dims

        populSize = newNInds * 2
        nIndsCurrent = newNInds
        nIndsFront = newNInds
        nIndsFrontMax = newNInds

        population = Array(populSize) { DoubleArray(nVars) }
        populationFront = Array(nIndsFront) { DoubleArray(nVars) }
        populationTemp = Array(populSize) { DoubleArray(nVars) }

        fitArr = DoubleArray(populSize)
        fitArrCopy = DoubleArray(populSize)
        fitArrFront = DoubleArray(nIndsFront)

        weights = DoubleArray(populSize)
        tempSuccessCr = DoubleArray(populSize) { 0.0 }
        fitDelta = DoubleArray(populSize)
        memoryCr = DoubleArray(memorySize) { 1.0 }

        trial = DoubleArray(nVars)
        indices = IntArray(populSize)
        indices2 = IntArray(populSize)

        for (i in 0 until populSize)
            for (j in 0 until nVars)
                population[i][j] = RNG.nextDouble(lowerBound[j], upperBound[j])
    }

    private fun weightedRandomPick(weights: List<Double>): Int {
        val total = weights.sum()
        val r = RNG.nextDouble(0.0, total)
        var acc = 0.0
        for (i in weights.indices) {
            acc += weights[i]
            if (r <= acc) {
                return i
            }
        }
        return weights.size - 1
    }

    private fun qSort2int(mass: DoubleArray, mass2: IntArray, low: Int, high: Int) {
        var i = low
        var j = high
        val x = mass[(low + high) / 2]
        do {
            while (mass[i] < x) i++
            while (mass[j] > x) j--
            if (i <= j) {
                val t = mass[i]; mass[i] = mass[j]; mass[j] = t
                val t2 = mass2[i]; mass2[i] = mass2[j]; mass2[j] = t2
                i++; j--
            }
        } while (i <= j)
        if (low < j) qSort2int(mass, mass2, low, j)
        if (i < high) qSort2int(mass, mass2, i, high)
    }

    private fun evalVector(x: DoubleArray): Double {
        val sol = NumberSolution(Util.toDoubleArrayList(x))
        task.eval(sol)
        return sol.eval
    }

    private fun mainLoop() {

        for (i in 0 until nIndsFront) {
            fitArr[i] = evalVector(population[i])
            if (i == 0) {
                bestfit = fitArr[i]
                globalBestFit = bestfit
            }
            if (task.problem.isFirstBetter(fitArr[i], bestfit, 0)) bestfit = fitArr[i]

            if (task.problem.isFirstBetter(fitArr[i], globalBestFit, 0)) {
                globalBestFit = fitArr[i]
                globalBestVec = population[i].clone()
            }
        }

        for (i in 0 until nIndsFront) {
            fitArrCopy[i] = fitArr[i]
            indices[i] = i
        }
        qSort2int(fitArrCopy, indices, 0, nIndsFront - 1)
        for (i in 0 until nIndsFront) {
            populationFront[i] = population[indices[i]].clone()
            fitArrFront[i] = fitArrCopy[i]
        }

        pfIndex = 0

        while (!task.isStopCriterion) {

            val meanF = 0.4 + tanh(successRate * 5) * 0.25
            val sigmaF = 0.02

            for (i in 0 until nIndsFront) {
                fitArrCopy[i] = fitArrFront[i]
                indices2[i] = i
            }
            qSort2int(fitArrCopy, indices2, 0, nIndsFront - 1)
            val FitTemp2 = List(nIndsFront) { i -> exp(-i.toDouble() / nIndsFront * 3) }
            val psizeval = max(2, (nIndsFront * 0.7 * exp(-successRate * 7)).roundToInt())

            for (i in 0 until nIndsFront) {
                theChosenOne = RNG.nextInt(nIndsFront)
                memoryCurrentIndex = RNG.nextInt(memorySize)

                var prand: Int
                do prand = indices[RNG.nextInt(psizeval)]
                while (prand == theChosenOne)

                var r1: Int
                do {
                    val w = weightedRandomPick(FitTemp2)
                    r1 = w
                } while (r1 == prand)

                var r2: Int
                do r2 = indices[RNG.nextInt(nIndsFront)]
                while (r2 == prand || r2 == r1)

                do F = RNG.nextGaussian(meanF, sigmaF)
                while (F !in 0.0..1.0)

                Cr = min(max(RNG.nextGaussian(memoryCr[memoryCurrentIndex], 0.05), 0.0), 1.0)

                val willCross = RNG.nextInt(nVars)
                var actualCr = 0.0

                for (j in 0 until nVars) {
                    if (RNG.nextDouble() < Cr || j == willCross) {
                        trial[j] =
                            populationFront[theChosenOne][j] +
                                    F * (population[prand][j] - populationFront[theChosenOne][j]) +
                                    F * (populationFront[r1][j] - population[r2][j])

                        if (trial[j] < lowerBound[j] || trial[j] > upperBound[j])
                            trial[j] = RNG.nextDouble(lowerBound[j], upperBound[j])

                        actualCr++
                    } else {
                        trial[j] = populationFront[theChosenOne][j]
                    }
                }

                actualCr /= nVars.toDouble()

                if (task.isStopCriterion)
                    break

                val tempFit = evalVector(trial)

                if (task.problem.isFirstBetter(tempFit, globalBestFit, 0)) {
                    globalBestFit = tempFit
                    globalBestVec = trial.clone()
                }
                if (task.problem.isFirstBetter(tempFit, fitArrFront[theChosenOne], 0)) {
                    population[nIndsCurrent + successFilled] = trial.clone()
                    populationFront[pfIndex] = trial.clone()

                    fitArr[nIndsCurrent + successFilled] = tempFit
                    fitArrFront[pfIndex] = tempFit

                    tempSuccessCr[successFilled] = actualCr
                    fitDelta[successFilled] = abs(fitArrFront[theChosenOne] - tempFit)

                    successFilled++
                    pfIndex = (pfIndex + 1) % nIndsFront
                }

            }
            successRate = successFilled.toDouble() / nIndsFront.toDouble()

            newNIndsFront =
                ((4 - nIndsFrontMax).toDouble() / task.maxEvaluations * task.numberOfEvaluations + nIndsFrontMax).roundToInt()

            removeWorst(nIndsFront, newNIndsFront)
            nIndsFront = newNIndsFront

            updateMemoryCr()

            nIndsCurrent = nIndsFront + successFilled
            if (nIndsCurrent > nIndsFront) {
                for (i in 0 until nIndsCurrent) {
                    fitArrCopy[i] = fitArr[i]
                    indices[i] = i
                }
                qSort2int(fitArrCopy, indices, 0, nIndsCurrent - 1)

                nIndsCurrent = nIndsFront
                for (i in 0 until nIndsCurrent) {
                    for (j in 0 until nVars) {
                        populationTemp[i][j] = population[indices[i]][j]
                    }
                }
                for (i in 0 until nIndsCurrent) {
                    for (j in 0 until nVars) {
                        population[i][j] = populationTemp[i][j]
                    }
                    fitArr[i] = fitArrCopy[i]
                }
            }
            successFilled = 0
        }
    }

    private fun removeWorst(oldN: Int, newN: Int) {
        var n = oldN
        while (n > newN) {
            var worst = 0
            var worstFit = fitArrFront[0]
            for (i in 1 until n) {
                if (!task.problem.isFirstBetter(fitArrFront[i], worstFit, 0)) {
                    worst = i
                    worstFit = fitArrFront[i]
                }
            }
            for (i in worst until n - 1) {
                populationFront[i] = populationFront[i + 1]
                fitArrFront[i] = fitArrFront[i + 1]
            }
            n--
        }
    }

    private fun updateMemoryCr() {
        if (successFilled != 0) {
            val meanWL = meanWL(tempSuccessCr, fitDelta)
            memoryCr[memoryIter] = 0.5 * (meanWL + memoryCr[memoryIter])
            memoryIter = (memoryIter + 1) % memorySize
        }
    }

    private fun meanWL(vector: DoubleArray, tempWeights: DoubleArray): Double {
        var wsum = 0.0
        for (i in 0 until successFilled) wsum += tempWeights[i]

        for (i in 0 until successFilled)
            weights[i] = tempWeights[i] / wsum

        var sumSq = 0.0
        var sum = 0.0
        for (i in 0 until successFilled) {
            sumSq += weights[i] * vector[i] * vector[i]
            sum += weights[i] * vector[i]
        }

        return if (abs(sum) > 1e-8) sumSq / sum else 1.0
    }

    override fun resetToDefaultsBeforeNewRun() {

    }
}