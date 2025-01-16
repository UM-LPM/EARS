package org.um.feri.ears.algorithms.so.gaoa;

import org.apache.commons.math3.special.Gamma;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class GAOA extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(name = "predator success rate",
            description = "determines the ability of the agents to escape local minimum")
    private double PSR;
    @AlgorithmParameter(name = "speed",
            description = "defines the step size of the agents during exploration and translates to speed of the gazelles in km/h when multiplied by 100")
    private double S;
    @AlgorithmParameter(name = "beta",
            description = "the distribution index that controls the Levy flight motion",
            min = "0.3",
            max = "1.99")
    private double beta;
    private int dim;
    private NumberSolution<Double> best;
    private ArrayList<NumberSolution<Double>> population;
    private ArrayList<NumberSolution<Double>> oldPopulation;

    public GAOA() {
        this(50, 0.88, 0.34, 1.5);
    }

    public GAOA(int popSize, double S, double PSR, double beta) {
        super();
        this.popSize = popSize;
        this.S = S;
        this.PSR = PSR;
        this.beta = beta;

        au = new Author("zan", "zan.hozjan1@student.um.si");
        ai = new AlgorithmInfo("GAOA", "Gazelle Optimisation Algorithm",
                "@article{https://doi.org/10.1007/s00521-022-07854-6"
                        + "  title={Gazelle optimization algorithm: a novel nature-inspired metaheuristic optimizer},"
                        + "  author={Jeffrey O. Agushaka, Absalom E. Ezugwu, Laith Abualigah},"
                        + "  journal={Neural Computing and Applications},"
                        + "  volume={35},"
                        + "  pages={4099–4131},"
                        + "  year={2023}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        dim = task.problem.getNumberOfDimensions();
        List<Double> ub = task.problem.getUpperLimit();
        List<Double> lb = task.problem.getLowerLimit();

        int maxIter = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIter = task.getMaxIterations();
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIter = (task.getMaxEvaluations() - popSize) / popSize;
        }

        List<List<Double>> stepSize = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < dim; j++) {
                row.add(0.0);
            }
            stepSize.add(row);
        }

        // denotes grazing speed of the gazelles
        double s = RNG.nextDouble();
        // determines the direction of movement of the gazelles and alternates between -1 and 1
        int mu;
        int iter = 0;

        initPopulation();

        while (!task.isStopCriterion()) {
            // cumulative effect of the predator
            double CF = pow((1.0 - (double) iter / (double) maxIter), (2.0 * (double) iter / (double) maxIter));

            // levy random number vector
            List<List<Double>> RL = levy(popSize, dim, beta);
            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < dim; j++) {
                    RL.get(i).set(j, RL.get(i).get(j) * 0.05);
                }
            }

            // brownian random number vector
            List<List<Double>> RB = new ArrayList<>();
            for (int i = 0; i < popSize; i++) {
                List<Double> row = new ArrayList<>();
                for (int j = 0; j < dim; j++) {
                    row.add(RNG.nextGaussian());
                }
                RB.add(row);
            }

            // update position
            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < dim; j++) {
                    double R = RNG.nextDouble();
                    double r = RNG.nextDouble();

                    if (r > 0.5) { // exploitation (grazing)
                        stepSize.get(i).set(j, RB.get(i).get(j) * (best.getVariables().get(j) - RB.get(i).get(j) * population.get(i).getVariables().get(j)));
                        population.get(i).getVariables().set(j, population.get(i).getVariables().get(j) + s * R * stepSize.get(i).get(j));
                    } else { // exploration (predator spotted)
                        if (iter % 2 == 0)
                            mu = -1;
                        else
                            mu = 1;

                        if (i < popSize / 2) {
                            // gazelle reacts first and follows levy motion
                            stepSize.get(i).set(j, RL.get(i).get(j) * (best.getVariables().get(j) - RL.get(i).get(j) * population.get(i).getVariables().get(j)));
                            population.get(i).getVariables().set(j, population.get(i).getVariables().get(j) + S * mu * R * stepSize.get(i).get(j));
                        } else {
                            // predator reacts later and first follows brownian motion
                            stepSize.get(i).set(j, RB.get(i).get(j) * (best.getVariables().get(j) - RL.get(i).get(j) * population.get(i).getVariables().get(j)));
                            population.get(i).getVariables().set(j, population.get(i).getVariables().get(j) + S * mu * CF * stepSize.get(i).get(j));
                        }
                    }

                }
            }

            // evaluate current population and update old population
            if (evaluationAndSelection(task)) return best;

            // applying predator effect
            if (RNG.nextDouble() < PSR) {
                List<List<Integer>> U = new ArrayList<>();
                List<List<Double>> R = new ArrayList<>();

                for (int i = 0; i < popSize; i++) {
                    List<Integer> row = new ArrayList<>();
                    for (int j = 0; j < dim; j++) {
                        if (RNG.nextDouble() < PSR)
                            row.add(1);
                        else
                            row.add(0);
                    }
                    U.add(row);
                }
                for (int i = 0; i < popSize; i++) {
                    List<Double> row = new ArrayList<>();
                    for (int j = 0; j < dim; j++) {
                        row.add(RNG.nextDouble());
                    }
                    R.add(row);
                }
                for (int i = 0; i < popSize; i++) {
                    for (int j = 0; j < dim; j++) {
                        Double x = population.get(i).getVariables().get(j);
                        population.get(i).getVariables().set(j, x + CF * ((lb.get(j) + R.get(i).get(j) * (ub.get(j) - lb.get(j))) * (double) U.get(i).get(j)));
                    }
                }
            } else {
                double r = RNG.nextDouble();
                List<Integer> randomIndex1 = new ArrayList<>();
                List<Integer> randomIndex2 = new ArrayList<>();

                for (int i = 0; i < popSize; i++) {
                    randomIndex1.add(i);
                    randomIndex2.add(i);
                }
                RNG.shuffle(randomIndex1);
                RNG.shuffle(randomIndex2);

                for (int i = 0; i < popSize; i++) {
                    for (int j = 0; j < dim; j++) {
                        stepSize.get(i).set(j, (PSR * (1 - r) + r) * (population.get(randomIndex1.get(i)).getVariables().get(j) - population.get(randomIndex2.get(i)).getVariables().get(j)));
                    }
                }
                for (int i = 0; i < popSize; i++) {
                    for (int j = 0; j < dim; j++) {
                        population.get(i).getVariables().set(j, population.get(i).getVariables().get(j) + stepSize.get(i).get(j));
                    }
                }
            }

            // NOTE: located at the beginning of the main loop in the referenced article
            // evaluate current population and update old population
            if (evaluationAndSelection(task)) return best;

            task.incrementNumberOfIterations();
            iter += 1;
        }

        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        oldPopulation = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> newSolution = task.generateRandomEvaluatedSolution();
            population.add(newSolution);

            if (i > 0 && task.problem.isFirstBetter(newSolution, best))
                best = new NumberSolution<>(newSolution);
            else
                best = new NumberSolution<>(population.get(0));
        }
    }

    private boolean evaluationAndSelection(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        for (NumberSolution<Double> g : population) {
            task.problem.makeFeasible(g);
            if (task.isStopCriterion())
                return true;
            task.eval(g);
            if (task.problem.isFirstBetter(g, best)) {
                best = new NumberSolution<>(g);
            }
        }

        if (!oldPopulation.isEmpty()) {
            // keep the better agents comparing new and old generation
            for (int i = 0; i < popSize; i++) {
                if (task.problem.isFirstBetter(oldPopulation.get(i), population.get(i)))
                    population.set(i, oldPopulation.get(i));
            }
        }

        // save current population as old for next iteration
        oldPopulation = new ArrayList<>();
        for (NumberSolution<Double> g : population) {
            oldPopulation.add(new NumberSolution<>(g));
        }

        return false;
    }

    private List<List<Double>> levy(int n, int m, double beta) {
        List<List<Double>> u = new ArrayList<>();
        List<List<Double>> v = new ArrayList<>();
        List<List<Double>> z = new ArrayList<>();

        // used for numerator
        double num = Gamma.gamma(1 + beta) * sin(Math.PI * beta / 2);

        // used for denominator
        double den = Gamma.gamma((1 + beta) / 2) * beta * pow(2, (beta - 1) / 2);

        double sigma_u = pow((num / den), (1 / beta));

        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(RNG.nextGaussian(0, sigma_u));
            }
            u.add(row);
        }

        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(RNG.nextGaussian(0, 1));
            }
            v.add(row);
        }

        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(u.get(i).get(j) / ((pow(abs(v.get(i).get(j)), 1 / beta))));
            }
            z.add(row);
        }

        return z;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
