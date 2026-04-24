package org.um.feri.ears.algorithms.so.hgoa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class HGOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "c_max")
    private double cMax = 1.0;
    @AlgorithmParameter(name = "c_min")
    private double cMin = 0.00004;
    @AlgorithmParameter(name = "c_boa_init")
    private double sensoryModality = 0.01;
    @AlgorithmParameter(name = "power_exponent")
    private double powerExponent = 0.1;

    @AlgorithmParameter(name = "alpha_ps")
    private double alphaPS = 1.0;
    @AlgorithmParameter(name = "beta_ps")
    private double betaPS = 0.5;
    @AlgorithmParameter(name = "eps_ps")
    private double epsilonPS = 1e-6;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> best;

    public HGOA() {
        this(30);
    }

    public HGOA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("assistant", "generated@codex.com");
        ai = new AlgorithmInfo("HGOA", "Hybrid Grasshopper Optimization Algorithm",
                "Hybrid grasshopper optimizer with butterfly search and pattern search");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        resetToDefaultsBeforeNewRun();
        this.task = task;
        initPopulation();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        int dim = task.problem.getNumberOfDimensions();
        double[] ub = Util.toDoubleArray(task.problem.getUpperLimit());
        double[] lb = Util.toDoubleArray(task.problem.getLowerLimit());

        double pLarge = 1e4;
        int T = 2 * dim;
        double sensory = sensoryModality;

        while (!task.isStopCriterion()) {
            int l = task.getNumberOfIterations() + 1;
            double tr = (double) l / maxIt;
            double c = -pLarge * cMin + (cMax + pLarge * cMin) *
                    Math.exp(Math.pow(tr, 1.5) * Math.log(((pLarge + 1) * cMin) / (cMax + pLarge * cMin)));
            double w;
            if (l <= maxIt / 2)
                w = Math.sin(Math.PI * l / maxIt + Math.PI) + 1.0;
            else
                w = 0.15 * Math.cos(0.15 * l * Math.PI) + 0.15;

            ArrayList<NumberSolution<Double>> newPop = new ArrayList<>(popSize);

            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> current = population.get(i);
                double[] posI = Util.toDoubleArray(current.getVariables());
                double[] S = new double[dim];
                for (int j = 0; j < popSize; j++) {
                    if (i == j) continue;
                    double[] posJ = Util.toDoubleArray(population.get(j).getVariables());
                    double[] diff = new double[dim];
                    for (int d = 0; d < dim; d++) diff[d] = posJ[d] - posI[d];
                    double dist = norm(diff);
                    if (dist == 0) continue;
                    double[] rvec = new double[dim];
                    for (int d = 0; d < dim; d++) rvec[d] = diff[d] / (dist + 1e-14);
                    double xjxi = 2.0 + (dist % 2.0);
                    double sf = sFunc(xjxi);
                    for (int d = 0; d < dim; d++)
                        S[d] += (ub[d] - lb[d]) * c / 2.0 * sf * rvec[d];
                }
                double[] candidate = new double[dim];
                for (int d = 0; d < dim; d++)
                    candidate[d] = c * S[d] + best.getValue(d);

                if (RNG.nextDouble() >= 0.5) {
                    double fNew = current.getEval();
                    double FP = sensory * Math.pow(Math.abs(fNew), powerExponent);
                    if (RNG.nextDouble() > 0.8) {
                        double amp = RNG.nextDouble() * RNG.nextDouble();
                        for (int d = 0; d < dim; d++)
                            candidate[d] = w * posI[d] + (amp * best.getValue(d) - posI[d]) * FP;
                    } else {
                        double eps = RNG.nextDouble();
                        int j1 = RNG.nextInt(popSize);
                        int j2 = RNG.nextInt(popSize);
                        while (j2 == j1) j2 = RNG.nextInt(popSize);
                        NumberSolution<Double> g1 = population.get(j1);
                        NumberSolution<Double> g2 = population.get(j2);
                        for (int d = 0; d < dim; d++) {
                            candidate[d] = w * posI[d] + (eps * eps * g1.getValue(d) - g2.getValue(d)) * FP;
                        }
                    }
                }

                task.problem.makeFeasible(candidate);
                if (task.isStopCriterion()) break;
                NumberSolution<Double> candSol = new NumberSolution<>(Util.toDoubleArrayList(candidate));
                task.eval(candSol);
                if (task.problem.isFirstBetter(candSol, current))
                    newPop.add(candSol);
                else
                    newPop.add(current);
            }
            if (task.isStopCriterion()) {
                population = newPop;
                break;
            }

            // Centroid OBL
            double[] M = new double[dim];
            for (int d = 0; d < dim; d++) {
                double sum = 0;
                for (NumberSolution<Double> sol : newPop) sum += sol.getValue(d);
                M[d] = sum / popSize;
            }
            for (int m = 0; m < popSize; m++) {
                double[] op = new double[dim];
                for (int d = 0; d < dim; d++) op[d] = 2 * M[d] - newPop.get(m).getValue(d);
                task.problem.makeFeasible(op);
                if (task.isStopCriterion()) break;
                NumberSolution<Double> opSol = new NumberSolution<>(Util.toDoubleArrayList(op));
                task.eval(opSol);
                if (task.problem.isFirstBetter(opSol, newPop.get(m))) newPop.set(m, opSol);
            }

            population = newPop;
            for (NumberSolution<Double> s : population) {
                if (task.problem.isFirstBetter(s, best)) best = s;
            }

            if ((l % T) == 0 && !task.isStopCriterion()) {
                int bestIdx = 0;
                int worstIdx = 0;
                for (int i = 1; i < popSize; i++) {
                    if (task.problem.isFirstBetter(population.get(i), population.get(bestIdx))) bestIdx = i;
                    if (task.problem.isFirstBetter(population.get(worstIdx), population.get(i))) worstIdx = i;
                }
                double[] mid = new double[dim];
                for (int d = 0; d < dim; d++)
                    mid[d] = (population.get(bestIdx).getValue(d) + population.get(worstIdx).getValue(d)) / 2.0;
                double[] delta = new double[dim];
                for (int d = 0; d < dim; d++) {
                    double sum = 0;
                    for (int i = 0; i < popSize; i++)
                        sum += population.get(i).getValue(d) - mid[d];
                    delta[d] = sum / popSize;
                    if (Math.abs(delta[d]) < epsilonPS)
                        delta[d] = Math.signum(delta[d]) * epsilonPS;
                }
                double[] bestPos = Util.toDoubleArray(best.getVariables());
                double[] newBest = patternSearch(bestPos, delta, lb, ub);
                task.problem.makeFeasible(newBest);
                if(task.isStopCriterion())
                    break;

                NumberSolution<Double> nb = new NumberSolution<>(Util.toDoubleArrayList(newBest));
                task.eval(nb);
                if (task.problem.isFirstBetter(nb, best)) best = nb;
            }

            sensory += 0.025 / (sensory * 500.0);

            task.incrementNumberOfIterations();
        }

        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        best = task.generateRandomEvaluatedSolution();
        population.add(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> sol = task.generateRandomEvaluatedSolution();
            population.add(sol);
            if (task.problem.isFirstBetter(sol, best)) best = sol;
        }
    }

    private double[] patternSearch(double[] base, double[] delta, double[] lb, double[] ub) throws StopCriterionException {
        double[] xBase = base.clone();
        Double fBaseBox = safeEval(xBase);
        if (fBaseBox == null)
            return xBase;
        double fBase = fBaseBox;

        while (maxAbs(delta) > epsilonPS && !task.isStopCriterion()) {

            boolean improved = false;

            /* coordinate search ------------------------------------------------------ */
            for (int j = 0; j < xBase.length && !task.isStopCriterion(); j++) {

                for (int sign : new int[]{+1, -1}) {
                    double[] trial = xBase.clone();
                    trial[j] += sign * delta[j];

                    task.problem.makeFeasible(trial);
                    Double fTrialBox = safeEval(trial);
                    if (fTrialBox == null) return xBase;      // budget exhausted
                    double fTrial = fTrialBox;

                    if (task.problem.isFirstBetter(fTrial, fBase, 0)) {
                        xBase = trial;
                        fBase = fTrial;
                        improved = true;
                        break;                                // leave ± loop
                    }
                }
            }

            /* pattern move ----------------------------------------------------------- */
            if (improved) {
                double[] pattern = new double[xBase.length];
                for (int d = 0; d < xBase.length; d++)
                    pattern[d] = xBase[d] + alphaPS * (xBase[d] - base[d]);

                task.problem.makeFeasible(pattern);
                Double fPatternBox = safeEval(pattern);
                if (fPatternBox == null) return xBase;
                double fPattern = fPatternBox;

                if (task.problem.isFirstBetter(fPattern, fBase, 0)) {
                    xBase = pattern;
                    fBase = fPattern;
                }
            } else {                                         // shrink step size
                for (int d = 0; d < delta.length; d++) delta[d] *= betaPS;
            }
        }
        return xBase;
    }

    private double sFunc(double r) {
        double f = 0.5;
        double l = 1.5;
        return f * Math.exp(-r / l) - Math.exp(-r);
    }

    private double norm(double[] v) {
        double sum = 0;
        for (double d : v) sum += d * d;
        return Math.sqrt(sum);
    }

    private double maxAbs(double[] v) {
        double m = 0;
        for (double d : v)
            m = Math.max(m, Math.abs(d));
        return m;
    }

    private Double safeEval(double[] pos) throws StopCriterionException {
        if (task.isStopCriterion())
            return null;
        NumberSolution<Double> s = new NumberSolution<>(Util.toDoubleArrayList(pos));
        task.eval(s);
        return s.getEval();
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        population = null;
        best = null;
    }
}