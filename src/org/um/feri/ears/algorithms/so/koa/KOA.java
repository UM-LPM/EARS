package org.um.feri.ears.algorithms.so.koa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;

//TODO: verify implementation with the original paper
public class KOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;


    private static final double Tc = 3.0;
    private static final double M0 = 0.1;
    private static final double lambda = 15.0;
    private static final double EPS = 1e-10; // epsilon

    private NumberSolution<Double> sunSolution;
    private ArrayList<NumberSolution<Double>> population;
    private double[] orbital;
    private double[] T;
    private double[] fitness;

    public KOA() {
        this(30);
    }


    public KOA(int popSize) {
        super();
        this.popSize = popSize;


        au = new Author("Luka Lonec", "");
        ai = new AlgorithmInfo("KOA", "Kepler Optimization Algorithm",
                """
                        @article{ABDELBASSET2023110454,
                        title = {Kepler optimization algorithm: A new metaheuristic algorithm inspired by Kepler’s laws of planetary motion},
                        journal = {Knowledge-Based Systems},
                        volume = {268},
                        pages = {110454},
                        year = {2023},
                        issn = {0950-7051},
                        doi = {https://doi.org/10.1016/j.knosys.2023.110454},
                        url = {https://www.sciencedirect.com/science/article/pii/S0950705123002046},
                        author = {Mohamed Abdel-Basset and Reda Mohamed and Shaimaa A. Abdel Azeem and Mohammed Jameel and Mohamed Abouhawwash},
                        keywords = {Kepler’s laws, Metaheuristics, Optimization, Constrained problems, Photovoltaic modules},
                        abstract = {This study presents a novel physics-based metaheuristic algorithm called Kepler optimization algorithm (KOA), inspired by Kepler’s laws of planetary motion to predict the position and velocity of planets at any given time. In KOA, each planet with its position acts as a candidate solution, which is randomly updated through the optimization process with respect to the best-so-far solution (Sun). KOA allows for a more effective exploration and exploitation of the search space because the candidate solutions (planets) exhibit different situations from the Sun at different times. Four challengeable benchmarks, namely CEC 2014, CEC 2017, CEC 2020, and CEC2022, and eight constrained engineering design problems, in addition to the parameter estimation problem of photovoltaic modules, were used to assess the performance of KOA. To observe its effectiveness, it was compared with three classes of stochastic optimization algorithms, including: (i) the latest published algorithms, including Snake Optimizer (SO), Fick’s Law Algorithm (FLA), Coati Optimization Algorithm (COA), Pelican Optimization Algorithm (POA), Dandelion Optimizer (DO), Mountain Gazelle Optimizer (MGO), Artificial Gorilla Troops Optimizer (GTO), and Slime Mold Algorithm (SMA); (ii) well-studied and highly cited algorithms, such as Whale Optimization Algorithm (WOA) and Grey Wolf Optimizer (GWO); and (iii) two highly performing optimizers: LSHADE-cnEpSin and LSHADE-SPACMA. Results of the convergence curve and statistical information indicated that KOA is more promising than all the compared optimizers. The source code of KOA is publicly accessible at https://www.mathworks.com/matlabcentral/fileexchange/126175-kepler-optimization-algorithm-koa}
                        }
                        """
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        int dim = task.problem.getNumberOfDimensions();

        int maxEvaluations = 30000;

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxEvaluations = task.getMaxIterations() / popSize;
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxEvaluations = task.getMaxEvaluations();
        }

        initPopulation();

        orbital = new double[popSize];
        T = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            orbital[i] = RNG.nextDouble();
            T[i] = Math.abs(RNG.nextGaussian());
        }

        fitness = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            fitness[i] = population.get(i).getEval();
            if (sunSolution == null || task.problem.isFirstBetter(population.get(i), sunSolution)) {
                sunSolution = new NumberSolution<>(population.get(i));
            }
        }

        while (!task.isStopCriterion()) {
            double worstFitness = fitness[0];
            for (int i = 1; i < fitness.length; i++) {
                if (fitness[i] < worstFitness) {
                    worstFitness = fitness[i];
                }
            }
            int evalCount = task.getNumberOfEvaluations() - popSize;
            // search accuracy control
            double accuracy_control = M0 * Math.exp(-lambda * (evalCount / (double) maxEvaluations));

            // distance to sun
            double[] R = new double[popSize];
            for (int i = 0; i < popSize; i++) {
                double sum = 0.0;
                for (int j = 0; j < dim; j++) {
                    double diff = sunSolution.getValue(j) - population.get(i).getValue(j);
                    sum += diff * diff;
                }
                R[i] = Math.sqrt(sum);
            }

            // calculate mass
            double[] MS = new double[popSize];
            double[] m = new double[popSize];
            double sumFitness = 0.0;
            for (int k = 0; k < popSize; k++) {
                sumFitness += (fitness[k] - worstFitness);
            }

            for (int i = 0; i < popSize; i++) {
                MS[i] = RNG.nextDouble() * (sunSolution.getEval() - worstFitness) / sumFitness;
                m[i] = (fitness[i] - worstFitness) / sumFitness;
            }

            double[] Rnorm = normalizeArray(R);
            double[] MSnorm = normalizeArray(MS);
            double[] Mnorm = normalizeArray(m);

            // Calculate gravitational force
            double[] Fg = new double[popSize];
            for (int i = 0; i < popSize; i++) {
                Fg[i] = orbital[i] * accuracy_control * ((MSnorm[i] * Mnorm[i]) / (Rnorm[i] * Rnorm[i] + EPS)) + RNG.nextDouble();
            }

            double[] a1 = new double[popSize];
            for (int i = 0; i < popSize; i++) {
                double term = T[i] * T[i] * (accuracy_control * (MS[i] + m[i]) / (4.0 * Math.PI * Math.PI));
                a1[i] = RNG.nextDouble() * Math.pow(term, 1.0 / 3.0);
            }

            // Update each planet
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion())
                    break;

                evalCount = task.getNumberOfEvaluations() - popSize;
                double a2 = -1.0 + (-1.0) * ((evalCount % (maxEvaluations / Tc)) / (maxEvaluations / Tc));

                double n = (a2 - 1.0) * RNG.nextDouble() + 1.0;

                // get 2 random planets
                int a = RNG.nextInt(popSize);
                int b = RNG.nextInt(popSize);

                // Generate rd vector - random vector of dimension size
                double[] rd = new double[dim];
                for (int j = 0; j < dim; j++) {
                    rd[j] = RNG.nextDouble();
                }

                double r = RNG.nextDouble(); // r1

                // Generate U1 - binary vector - Eq.(21)
                boolean[] U1 = new boolean[dim];
                for (int j = 0; j < dim; j++) {
                    U1[j] = rd[j] < r;
                }

                // Store old position
                double[] oldPosition = new double[dim];
                for (int j = 0; j < dim; j++) {
                    oldPosition[j] = population.get(i).getValue(j);
                }

                double[] newPosition = new double[dim];

                // Step 6
                //same as nextDouble() < nextDouble()
                if (RNG.nextDouble() < 0.5) {
                    double h = 1.0 / Math.exp(n * RNG.nextGaussian());

                    double[] Xm = new double[dim];
                    for (int j = 0; j < dim; j++) {
                        Xm[j] = (population.get(b).getValue(j) + sunSolution.getValue(j) + population.get(i).getValue(j)) / 3.0;
                    }

                    for (int j = 0; j < dim; j++) {
                        if (U1[j]) {
                            newPosition[j] = population.get(i).getValue(j);
                        } else {
                            newPosition[j] = Xm[j] + h * (Xm[j] - population.get(a).getValue(j));
                        }
                    }
                } else {
                    // move in orbit
                    int f; // direction flag
                    if (RNG.nextDouble() < 0.5) { // Eq.(18)
                        f = 1;
                    } else {
                        f = -1;
                    }

                    double L = Math.sqrt(accuracy_control * (MS[i] + m[i]) * Math.abs((2.0 / (R[i] + EPS)) - (1.0 / (a1[i] + EPS))));

                    boolean[] U = new boolean[dim];
                    for (int j = 0; j < dim; j++) {
                        U[j] = rd[j] > RNG.nextDouble();
                    }

                    double[] V = new double[dim];

                    if (Rnorm[i] < 0.5) {
                        double M_val = RNG.nextDouble() * (1.0 - r) + r;
                        double[] l = new double[dim];
                        double[] l1 = new double[dim];
                        for (int j = 0; j < dim; j++) {
                            l[j] = L * M_val * (U[j] ? 1.0 : 0.0);
                            double Mv = RNG.nextDouble() * (1.0 - rd[j]) + rd[j];
                            l1[j] = L * Mv * (U[j] ? 0.0 : 1.0);
                        }

                        // Calculate velocity
                        double randScalar = RNG.nextDouble();
                        double[] randVector = new double[dim];
                        for (int j = 0; j < dim; j++) {
                            randVector[j] = RNG.nextDouble();
                        }

                        for (int j = 0; j < dim; j++) {
                            double term1 = l[j] * (2.0 * randScalar * population.get(i).getValue(j) - population.get(a).getValue(j));
                            double term2 = l1[j] * (population.get(b).getValue(j) - population.get(a).getValue(j));
                            double term3 = (1.0 - Rnorm[i]) * f * (U1[j] ? 1.0 : 0.0) * randVector[j] *
                                    (task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j));
                            V[j] = term1 + term2 + term3;
                        }
                    } else {
                        boolean U2 = 0.5 > RNG.nextDouble(); // RNG.nextDouble() > RNG.nextDouble()
                        for (int j = 0; j < dim; j++) {
                            double rand1 = RNG.nextDouble();
                            double rand2 = RNG.nextDouble();
                            double rand3 = RNG.nextDouble();
                            double term1 = rand1 * L * (population.get(a).getValue(j) - population.get(i).getValue(j));
                            double term2 = (1.0 - Rnorm[i]) * f * (U2 ? 1.0 : 0.0) * rand2 *
                                    (rand3 * task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j));
                            V[j] = term1 + term2;
                        }
                    }

                    if (RNG.nextDouble() < 0.5) {
                        f = 1;
                    } else {
                        f = -1;
                    }

                    for (int j = 0; j < dim; j++) {
                        double term1 = population.get(i).getValue(j) + V[j] * f;
                        double term2 = (Fg[i] + Math.abs(RNG.nextGaussian())) * (U[j] ? 1.0 : 0.0) *
                                (sunSolution.getValue(j) - population.get(i).getValue(j));
                        newPosition[j] = term1 + term2;
                    }
                }

                //same as nextDouble() < nextDouble()
                if (RNG.nextDouble() < 0.5) {
                    for (int j = 0; j < dim; j++) {
                        if (!task.problem.isFeasible(newPosition[j], j)) {
                            newPosition[j] = task.problem.getLowerLimit(j) +
                                    RNG.nextDouble() * (task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j));
                        }
                    }
                } else {
                    task.problem.makeFeasible(newPosition);
                }

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSolution);

                if (task.problem.isFirstBetter(newSolution, population.get(i))) {
                    fitness[i] = newSolution.getEval();
                    population.set(i, newSolution);

                    if (task.problem.isFirstBetter(newSolution, sunSolution)) {
                        sunSolution = new NumberSolution<>(newSolution);
                    }
                } else {
                    for (int j = 0; j < dim; j++) {
                        population.get(i).setValue(j, oldPosition[j]);
                    }
                }
            }
        }

        return sunSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }
    }

    private double[] normalizeArray(double[] array) {
        double min = Arrays.stream(array).min().orElse(0.0);
        double max = Arrays.stream(array).max().orElse(1.0);
        double range = max - min;

        double[] normalized = new double[array.length];
        if (range < EPS) {
            Arrays.fill(normalized, 0.0);
        } else {
            for (int i = 0; i < array.length; i++) {
                normalized[i] = (array[i] - min) / range;
            }
        }
        return normalized;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        sunSolution = null;
        population = null;
        orbital = null;
        T = null;
        fitness = null;
    }
}

