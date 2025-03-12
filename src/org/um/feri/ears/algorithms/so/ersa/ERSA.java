package org.um.feri.ears.algorithms.so.ersa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.SolutionUtils;
import org.um.feri.ears.util.random.RNG;

import java.util.*;

// Changed N = 100
// and set M = pow(N, 1.3)
// and selected at most 10 streamers in each iteration

public class ERSA extends NumberAlgorithm {

    boolean printDebug = false;
    NumberSolution<Double> globalBest; // best solution
    double beta = 0.5; // 0.75 for local optimum considerations, 0.25 for finding global solutions
    int N = 50; // Number of initial steamers; N=200 for infinite search space, N=50 for finite search space
    double E0 = 1000.0;   // E0 in [500, 1500]
    double CV = computeCV();
    double r = 0.2; // 10 for uncountable search spaces, 0.2 for finite search area
    int M = computeM();  // number of random points in search space for each steamer


    double computeCV() {
        return 0.1 * E0;
    }

    int computeM() {
        return 4 * N; // N*N
    }

    class Streamer {
        NumberSolution<Double> Fn0; // n-th streamer solution initial solution
        NumberSolution<Double> Fn_curr;  // n-th streamer solution in time t
        NumberSolution<Double> Fn_prev; // n-th streamer solution in time t-1
        double E_prev;     // number of electrons in the streamer (E^{t-1}_{n} in paper) at previous iteration

        Streamer() throws StopCriterionException {
            // create an initial random evaluated solution in the feasible region
            try {
                Fn0 = task.generateRandomEvaluatedSolution();
            } catch (StopCriterionException e) {
                throw e;
            }

            Fn_curr = new NumberSolution<>(Fn0);
            Fn_prev = new NumberSolution<>(Fn0);

            E_prev = E0;

            if (printDebug)
                System.out.println("New random evaluated solution at " + Fn0.getVariables().get(0));
        }

        Streamer(Streamer streamerToFork) throws StopCriterionException {
            // Initialize new streamer

            // We have two options:
            // a) use previous streamer's Fn0, in case electron flow will be higher (according to eq.4's exp term)
            // b) define a new Fn0 in terms of last streamer's best objective function
            //  Since paper doesn't define this, we TODO tried both approaches

            Fn0 = new NumberSolution<Double>(streamerToFork.Fn0);

            Fn_prev = new NumberSolution<Double>(streamerToFork.Fn_curr);
            Fn_curr = new NumberSolution<Double>(streamerToFork.Fn_curr);

            double r_max_tn = r * Math.exp((E_prev - CV) / CV);

            // Divide electrons
            E_prev = streamerToFork.E_prev / 2.0;
            streamerToFork.E_prev = E_prev;

            try {
                Fn_curr = generateSolution(r_max_tn);
            } catch (StopCriterionException e) {
                throw e;
            }
        }

        ArrayList<Double> randomUniformHypersphere(int dim, double radius, ArrayList<Double> center) {
            // References:
            // A) Generate uniformly on *surface* of hypersphere: https://math.stackexchange.com/a/447023
            // B) Convert from surface to uniform points *inside* hypersphere: https://math.stackexchange.com/a/2635801

            double minNorm = 0.00000001;   // avoids numerical problems when normalizing
            ArrayList<Double> result = new ArrayList<Double>();
            ArrayList<Double> x;

            // part A:
            double norm = 0.0;
            do {
                x = new ArrayList<>();
                for (int i = 0; i < dim; i++) {
                    double value = RNG.nextGaussian();
                    x.add(value);
                    norm += value * value;
                }
                norm = Math.sqrt(norm);
            } while (norm < minNorm);

            for (int i = 0; i < dim; i++) {
                double normalized = x.get(i) / norm;
                x.set(i, normalized);
            }

            // part B:
            double uniform = RNG.nextDouble();   // generate uniform [0,1]
            uniform = Math.pow(uniform, 1.0 / (double) dim);
            for (int i = 0; i < dim; i++) {
                double scaled = x.get(i) * uniform;
                x.set(i, scaled);
            }

            // Scale by radius and shift values by center
            for (int i = 0; i < dim; i++) {
                double scaled_shifted = x.get(i) * radius + center.get(i);

                // System.out.println(x.get(i)*radius);

                result.add(scaled_shifted);
            }

            return result;
        }

        NumberSolution<Double> generateSolution(double radius) throws StopCriterionException {
            NumberSolution<Double> s;
            ArrayList<Double> currVarList = Fn_curr.getVariables();
            ArrayList<Double> newVarList;
            double newVar;

            newVarList = randomUniformHypersphere(task.problem.getNumberOfDimensions(), radius, currVarList);
            NumberSolution<Double> newSolution = new NumberSolution<>(newVarList);
            try {
                task.eval(newSolution);
            } catch (StopCriterionException e) {
                throw e;
            }

            return newSolution;
        }

        boolean update() throws StopCriterionException {
            // Returns True if streamer is to be kept active
            // Returns False if streamer needs to be eliminated (better solution not found or En < CV)

            double x = SolutionUtils.calculateEuclideanDistance(Fn_prev, Fn_curr);

            //if(printDebug)
            //System.out.println(String.format("Fn0=%e, Curr=%e, Prev=%e, Glob=%e", Fn0.getEval(), Fn_curr.getEval(), Fn_prev.getEval(), globalBest.getEval()));

            double lambda = beta * (1.0 - Math.exp(-Math.abs(Fn_curr.getEval() - Fn_prev.getEval()))) + (1 - beta) * Math.exp(-Math.abs(Fn_curr.getEval() - globalBest.getEval()));
            double E_new = E_prev * (Math.exp((Math.abs(Fn_curr.getEval() - Fn_prev.getEval()) / Fn0.getEval()) * x) + lambda);

            //System.out.println(String.format("Current E/CV = %.2f", E_new/CV));
            // Paper Section 3.1:
            //      ``The streamer continues the searching
            //  process when the number of electrons is greater than a
            //  critical value. The streamers which cannot satisfy the
            //  critical value’s condition were eliminated from the search
            //  space.''
            if (E_new < CV) {
                // signalize to eliminate streamer
                return false;
            }

            E_prev = E_new;

            double r_max_tn = r * Math.exp((E_new - CV) / CV);

            // Generate M solutions inside hypersphere around the current streamer's tip
            //System.out.println("Generating M=" + M + " solutions around streamer's tip");
            NumberSolution<Double> chosenNewSolution = null;
            for (int i = 0; i < M; i++) {
                NumberSolution<Double> newSolution;
                try {
                    newSolution = generateSolution(r_max_tn);
                } catch (StopCriterionException e) {
                    // TODO: if you wish, update global best first
                    throw e;
                }

                // System.out.println(String.format("New solution at x=%.3f", newSolution.getVariables().get(0)));
                if (task.problem.isFirstBetter(newSolution, chosenNewSolution))
                    chosenNewSolution = new NumberSolution<>(newSolution);
            }

            // (paper Section 3.1, "Escaping from local minima")
            //      ''when the streamer does not find the better
            //  solution from its current solution, the streamer increases
            //  the local search space by decreasing the number of electrons
            //  to a critical value (CV). By development of the local   <------ "development" == expanding
            //  search space, the probability of finding better solutions
            //  increased. However, the streamer eliminated when the
            //  better solution not be achieved in the new local search
            //  space.``
            //    AND
            //      ''to escape from local optima, the streamer increases
            //  the radius of the sphere to the maximum value satisfying
            //  CV condition. To reach this purpose, the streamer
            //  decreases the number of electrons to the CV. Therefore, the
            //  maximum value of r in streamer n and iteration t is calculated
            //  as follows: r_max_tn = r * exp([En_t - CV]/CV)   [equation 7]''
            //
            //  For our code, we interpreted the above as follows:
            //     When better than current solution is not found (chosenNewSolution == null)
            //  we expand search radius to r_max_tn (paper equation 7) and search again.
            //  If solution is still not improved, streamer is eliminated.

            if (!task.problem.isFirstBetter(chosenNewSolution, Fn_curr)) {
                E_new = CV;
                r_max_tn = r;
                //E_new = E_prev = CV;

                for (int i = 0; i < M; i++) {
                    NumberSolution<Double> newSolution;
                    try {
                        newSolution = generateSolution(r_max_tn);
                    } catch (StopCriterionException e) {
                        throw e;
                    }
                    if (task.problem.isFirstBetter(newSolution, chosenNewSolution))
                        chosenNewSolution = new NumberSolution<>(newSolution);
                }
            }

            // Better solution not found?

            if (!task.problem.isFirstBetter(chosenNewSolution, Fn_curr)) {
                // signalize to eliminate streamer
                return false;
            }

            // Update current solution variable
            Fn_prev = new NumberSolution<>(Fn_curr);
            Fn_curr = new NumberSolution<>(chosenNewSolution);

            return true;
        }
    }

    public ERSA() {
        this(0.5, 50, 1000.0, 0.2);
    }
    public ERSA(double beta, int N, double E0, double r) {
        super();

        this.beta = beta;
        this.N = N;
        this.E0 = E0;
        this.r = r;
        this.CV = computeCV();
        this.M = computeM();

        ai = new AlgorithmInfo(
                "ERSA",
                "Electron Radar Search Algorithm",
                "@article{rahmanzadeh2020electron,\n" +
                        "  title={Electron radar search algorithm: a novel developed meta-heuristic algorithm},\n" +
                        "  author={Rahmanzadeh, Sajjad and Pishvaee, Mir Saman},\n" +
                        "  journal={Soft Computing},\n" +
                        "  volume={24},\n" +
                        "  number={11},\n" +
                        "  pages={8443--8465},\n" +
                        "  year={2020},\n" +
                        "  publisher={Springer}\n" +
                        "}\n");

        au = new Author("BK", "Blaz Kovacic");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        ArrayList<Streamer> streamers = new ArrayList<>();

        if (printDebug)
            System.out.println(String.format("Creating initial population of N=%d streamers", N));

        // Create initial population of N streamers
        for (int i = 0; i < N; i++) {
            Streamer streamer;
            try {
                streamer = new Streamer();
            } catch (StopCriterionException e) {
                if (streamers.isEmpty())
                    return null;
                else
                    setGlobalBest(streamers);

                return globalBest;
            }
            streamers.add(streamer);

            // Set best global solution so far
            setGlobalBest(streamers);
        }
        //setGlobalBest(streamers);

        //task.incrementNumberOfIterations();

        while (true) {
            if (printDebug)
                System.out.println("In iteration " + task.getNumberOfIterations());

            // Update streamers
            ArrayList<Streamer> streamersToRemove = new ArrayList<>();
            for (int i = 0; i < streamers.size(); i++) {
                Streamer streamer = streamers.get(i);
                boolean updateResult;
                try {
                    updateResult = streamer.update();
                } catch (StopCriterionException e) {
                    return globalBest;
                }

                // In case the current streamer has achieved the best solution, set global best
                setGlobalBest(streamers);

                if (!updateResult && (streamersToRemove.size() + 1) < streamers.size()) {
                    streamersToRemove.add(streamer);
                    if (printDebug)
                        System.out.println("Removing streamer");
                }
            }

            // Remove streamers which don't have enough electrons (<CV) or haven't improved objective function
            if (printDebug)
                System.out.println(String.format("From pop_size=%d removing %d streamers", streamers.size(), streamersToRemove.size()));

            streamers.removeAll(streamersToRemove);

            // Keep at most 5 best streamers
            while (streamers.size() > 5) {
                Streamer worst;
                worst = streamers.get(0);
                for (Streamer s : streamers)
                    if (!task.problem.isFirstBetter(s.Fn_curr, worst.Fn_curr))
                        worst = s;
                streamers.remove(worst);
            }

            // Fork streamers with probability
            ArrayList<Streamer> streamersToFork = new ArrayList<>();
            for (int i = 0; i < streamers.size(); i++) {
                // Don't fork streamers with E <= 2 CV
                double E = streamers.get(i).E_prev;
                if (E <= 2.0 * CV)
                    continue;

                // Forking probability
                double Pr = 1.0 - 2.0 * CV / E;
                if (RNG.nextDouble() < Pr)
                    streamersToFork.add(streamers.get(i));
            }

            for (Streamer streamer : streamersToFork) {
                Streamer streamerNew;
                //System.out.println("Forking streamer");
                try {
                    streamerNew = new Streamer(streamer);   // constructor takes care of dividing electrons amongst streamers
                } catch (StopCriterionException e) {
                    return globalBest;
                }
                streamers.add(streamerNew);
            }

            // In case there are no more streamers (?)
            if (streamers.isEmpty())
                break;

            // Find and set global best streamer
            setGlobalBest(streamers);

            task.incrementNumberOfIterations();

            if (printDebug)
                System.out.println(String.format("Eval(iter=%d) = %e", task.getNumberOfIterations(), globalBest.getEval()));
        }

        return globalBest;
    }

    void setGlobalBest(ArrayList<Streamer> streamers) {
        globalBest = new NumberSolution<>(streamers.get(0).Fn_curr);

        for (Streamer streamer : streamers) {
            if (task.problem.isFirstBetter(streamer.Fn_curr, globalBest)) {
                globalBest = new NumberSolution<>(streamer.Fn_curr);
            }
        }

        if (printDebug)
            System.out.println(String.format("Global best fitness=%e", globalBest.getEval()));
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
