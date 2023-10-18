package org.um.feri.ears.algorithms.so.cs;

import java.util.ArrayList;

import org.apache.commons.math3.special.Gamma;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

public class CS extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
	@AlgorithmParameter(name = "discovery rate")
	private double pa = 0.25; //Discovery rate of alien eggs/solutions

    private NumberSolution<Double> best;
    private ArrayList<NumberSolution<Double>> nest;

    public CS() {
        this(25);
    }

    public CS(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("CS", "Cuckoo Search",
                "@inproceedings{yang2009cuckoo,"
                        + "title={Cuckoo search via L{\'e}vy flights},"
                        + "author={Yang, Xin-She and Deb, Suash},"
                        + "booktitle={Nature & Biologically Inspired Computing, 2009. NaBIC 2009. World Congress on},"
                        + "pages={210--214},"
                        + "year={2009},"
                        + "organization={IEEE}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        while (!task.isStopCriterion()) {

            //Generate new solutions (but keep the current best)
            getCuckoos();
            //System.out.println(best.getEval());

            task.incrementNumberOfIterations();

            //Discovery and randomization
            emptyNests();

            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void setBest(ArrayList<NumberSolution<Double>> offspringPopulation) {

        for (int i = 0; i < popSize; i++) {
            if (i >= offspringPopulation.size())
                return;

            if (task.problem.isFirstBetter(offspringPopulation.get(i), nest.get(i))) {
                nest.set(i, offspringPopulation.get(i));
            }

            if (task.problem.isFirstBetter(offspringPopulation.get(i), best)) {
                best = new NumberSolution<>(offspringPopulation.get(i));
            }
        }
    }

    /**
     * Replace some nests by constructing new solutions/nests
     */
    private void emptyNests() throws StopCriterionException {

        //A fraction of worse nests are discovered with probability pa
        ArrayList<NumberSolution<Double>> offspringPopulation = new ArrayList<>();

        int[] per1 = RNG.randomPermutation(popSize);
        int[] per2 = RNG.randomPermutation(popSize);

        double stepsize;
        for (int i = 0; i < popSize; i++) {

            if (RNG.nextDouble() > pa) {
                double[] newSolution = new double[task.problem.getNumberOfDimensions()];
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    stepsize = RNG.nextDouble() * (nest.get(per1[i]).getValue(j) - (nest.get(per2[i]).getValue(j)));
                    newSolution[j] = nest.get(i).getValue(j) + stepsize;
                }
                task.problem.setFeasible(newSolution);
                if (task.isStopCriterion())
                    break;


                NumberSolution<Double> newC = new NumberSolution<>(Util.toDoubleArrayList(newSolution));
                task.eval(newC);
                offspringPopulation.add(newC);
            } else {
                offspringPopulation.add(nest.get(i));
            }
        }

        setBest(offspringPopulation);
    }

    /**
     * Get cuckoos by random walk
     */
    private void getCuckoos() throws StopCriterionException {

        //Levy flights
        //Levy exponent and coefficient
        //For details, see equation (2.21), Page 16 (chapter 2) of the book
        //X. S. Yang, Nature-Inspired Metaheuristic Algorithms, 2nd Edition, Luniver Press, (2010).

        ArrayList<NumberSolution<Double>> offspringPopulation = new ArrayList<>();
        double beta = 3.0 / 2.0;
        double sigma = Math.pow((Gamma.gamma(1 + beta) * Math.sin(Math.PI * beta / 2) / (Gamma.gamma((1 + beta) / 2) * beta * Math.pow(2, (beta - 1) / 2))), (1 / beta));

        for (int i = 0; i < popSize; i++) {

            NumberSolution<Double> s = nest.get(i);
            double u, v, step, stepsize;
            double[] newSolution = new double[task.problem.getNumberOfDimensions()];
            for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                u = RNG.nextDouble() * sigma;
                v = RNG.nextDouble();

                step = u / Math.pow(Math.abs(v), 1 / beta);

                stepsize = 0.01 * step * (s.getValue(j) - best.getValue(j));
                newSolution[j] = s.getValue(j) + stepsize * RNG.nextDouble();
            }

            task.problem.setFeasible(newSolution);
            if (task.isStopCriterion())
                break;


            NumberSolution<Double> newC = new NumberSolution<>(Util.toDoubleArrayList(newSolution));
            task.eval(newC);
            offspringPopulation.add(newC);
        }
        setBest(offspringPopulation);
    }

    private void initPopulation() throws StopCriterionException {
        nest = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            nest.add(task.getRandomEvaluatedSolution());
            if (task.isStopCriterion())
                break;
        }
        nest.sort(new ProblemComparator<>(task.problem));
        best = nest.get(0);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
