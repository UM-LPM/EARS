package org.um.feri.ears.algorithms.so.goa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.TaskComparator;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;

public class GOA extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter
    private double cMax = 1;
    @AlgorithmParameter
    private double cMin = 0.00001;

    private Task task;
    private NumberSolution<Double> best;
    private ArrayList<NumberSolution<Double>> population;
    private ArrayList<NumberSolution<Double>> offspringPopulation;

    public GOA() {
        this(20);
    }

    public GOA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("GOA", "Grasshopper Optimisation Algorithm",
                "@article{mirjalili2017grasshopper,"
                        + "  title={Grasshopper Optimisation Algorithm: Theory and application},"
                        + "  author={Saremi, Shahrzad and Mirjalili, Seyedali and Lewis, Andrew},"
                        + "  journal={Advances in Engineering Software},"
                        + "  volume={105},"
                        + "  pages={30--47},"
                        + "  year={2017},"
                        + "  publisher={Elsevier}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task task) throws StopCriterionException {
        this.task = task;
        initPopulation();
        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = task.getMaxEvaluations() / popSize;
        }

        double dist, r_ij1, r_ij2;
        double[] ub = task.getUpperLimit();
        double[] lb = task.getLowerLimit();
        double eps = Math.pow(2, -52);
        while (!task.isStopCriterion()) {
            //TODO normalize variables
            double c = cMax - (task.getNumberOfIterations() + 2) * ((cMax - cMin) / maxIt); // Eq. (2.8) in the paper
            double xj_xi, s_ij1, s_ij2;
            double[] S_total;
            offspringPopulation = new ArrayList<>();
            for (int i = 0; i < popSize; i++) {
                double[] temp_i = Util.toDoubleArray(population.get(i).getVariables());
                double[] newPosition = new double[task.getNumberOfDimensions()];
                S_total = new double[task.getNumberOfDimensions()];
                for (int k = 0; k < task.getNumberOfDimensions(); k += 2) {
                    for (int j = 0; j < popSize; j++) {
                        if (i == j)
                            continue;

                        double[] temp_j = Util.toDoubleArray(population.get(j).getVariables());


                        dist = distance(temp_i[k], temp_i[k + 1], temp_j[k], temp_j[k + 1]); //Calculate the distance between two grasshoppers

                        //xj-xi/dij in Eq. (2.7)
                        r_ij1 = (temp_j[k] - temp_i[k]) / (dist + eps);
                        r_ij2 = (temp_j[k + 1] - temp_i[k + 1]) / (dist + eps);

                        xj_xi = 2 + dist % 2; // |xjd - xid| in Eq. (2.7)

                        s_ij1 = ((ub[k] - lb[k]) * c / 2) * functionS(xj_xi) * r_ij1;
                        s_ij2 = ((ub[k + 1] - lb[k + 1]) * c / 2) * functionS(xj_xi) * r_ij2;
                        S_total[k] += s_ij1;
                        S_total[k + 1] += s_ij2;
                    }
                }

                for (int k = 0; k < task.getNumberOfDimensions(); k++) {
                    newPosition[k] = c * S_total[k] + best.getValue(k);
                }

                newPosition = task.setFeasible(newPosition);
                if (task.isStopCriterion())
                    break;
                NumberSolution<Double> newGH = task.eval(newPosition);

                offspringPopulation.add(newGH);
            }

            population = offspringPopulation;
            for (NumberSolution<Double> s : population) {
                if (task.isFirstBetter(s, best)) {
                    //System.out.println(s.getEval());
                    //best = new DoubleSolution(s);
                    best = s;
                }
            }
            //System.out.println("Iteration "+ (task.getNumberOfIterations()+2) +"  "+best.getEval() + " "+ c);

            task.incrementNumberOfIterations();
        }
        return best;
    }

    private double functionS(double r) {
        double f = 0.5, l = 1.5;
        return f * Math.exp(-r / l) - Math.exp(-r); //Eq. (2.3) in the paper
    }

    private double distance(double a1, double a2, double b1, double b2) {

        return Math.sqrt(Math.pow((a1 - b1), 2) + Math.pow((a2 - b2), 2));
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> newSolution = task.getRandomEvaluatedSolution();
            population.add(newSolution);
        }

        population.sort(new TaskComparator(task));
        best = new NumberSolution<>(population.get(0));
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
