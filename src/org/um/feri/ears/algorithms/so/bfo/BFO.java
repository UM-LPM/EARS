package org.um.feri.ears.algorithms.so.bfo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BFO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    private int Nc, Ns, Nre, Ned, Sr;
    private double Ped, Ci;
    private ArrayList<Double> C;
    private ArrayList<Bacteria> swarm;
    private Bacteria best, tmp;

//	S 	:	number of bacteria,
//	Nc 	: 	number of chemotactic steps,
//	Ns 	: 	number of swim steps,
//	Nre	: 	number of reproductive steps,
//	Ned	: 	number of elimination and dispersal steps,
//	Ped	: 	probability of elimination,
//	C 	:	the run-length unit (i.e., the chemotactic step size during each run or tumble).
//  Sr	: 	number of bacteria for reproduction, Sr = S / 2

    public BFO() {
        this(10, 10, 10, 5, 2, 0.01);
//		this(100, 100, 10, 5, 2, 0.01);
//		this(60, 25, 4, 4, 2, 0.01);
//		this(60, 10, 10, 5, 2, 0.01);
    }

    public BFO(int popSize, int Nc, int Ns, int Nre, int Ned, double Ped) {
        this.popSize = popSize;
        this.Nc = Nc;
        this.Ns = Ns;
        this.Nre = Nre;
        this.Ned = Ned;
        this.Ped = Ped;
        Sr = popSize / 2;
        swarm = new ArrayList<>();
        ai = new AlgorithmInfo("BFO", "Bacterial Foraging Optimization", "");
        au = new Author("Monika Bozhinova", "N/A"); //EARS author info
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        Ci = getAvgDistance(task.problem.getUpperLimit(), task.problem.getLowerLimit()) / 5;

        initSwarm();
        while (!task.isStopCriterion()) {
            for (int l = 0; l < Ned; l++) {
                for (int k = 0; k < Nre; k++) {
                    for (int j = 0; j < Nc; j++) {
                        for (int i = 0; i < popSize; i++) {
                            swarm.get(i).health = 0;
                            tmp = chemotaxis(swarm.get(i));
                            if (task.problem.isFirstBetter(tmp, swarm.get(i))) swarm.set(i, tmp);
                            if (task.isStopCriterion()) return best;
                        }
                    }
                    reproduction();
                    if (task.isStopCriterion()) return best;
                }
                for (int i = 0; i < popSize; i++) {
                    tmp = eliminationAndDispersal(swarm.get(i), i);
                    if (tmp != null) swarm.set(i, tmp);
                    if (task.isStopCriterion()) return best;
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void initSwarm() throws StopCriterionException {
        swarm.clear();
        best = null;
        for (int i = 0; i < popSize; i++) {
            tmp = new Bacteria(task, Ci);
            if (best == null) best = tmp;
            else if (task.problem.isFirstBetter(tmp, best)) {
                best = tmp;
                //System.out.println(task.getNumberOfEvaluations() + " " + best);
            }
            swarm.add(tmp);
            if (task.isStopCriterion()) break;
        }
    }

    private Bacteria chemotaxis(Bacteria b) throws StopCriterionException {
        double[] di = new double[task.problem.getNumberOfDimensions()];
        double[] cb = Util.toDoubleArray(b.getVariables());
        double[] xn = new double[task.problem.getNumberOfDimensions()];
        double rootProduct = 0;
        for (int i = 0; i < di.length; i++) {
            di[i] = 2 * RNG.nextDouble() - 1;
            rootProduct += Math.pow(di[i], 2);
        }
        for (int i = 0; i < xn.length; i++) {
            xn[i] = task.problem.setFeasible(cb[i] + b.c * (di[i] / Math.sqrt(rootProduct)), i);
        }

        NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(xn));
        task.eval(newSolution);

        Bacteria bx = new Bacteria(newSolution, b);
        if (task.isStopCriterion()) return bx;

        if (task.problem.isFirstBetter(bx, best)) {
            best = bx;
            //System.out.println(task.getNumberOfEvaluations() + " " + best);
        }

        if (task.problem.isFirstBetter(bx, bx.prev)) {
            int m = 0;
            while (m < Ns) {
                m++;
                for (int i = 0; i < xn.length; i++) {
                    xn[i] = task.problem.setFeasible(xn[i] + bx.c * (di[i] / Math.sqrt(rootProduct)), i);
                }

                newSolution = new NumberSolution<>(Util.toDoubleArrayList(xn));
                task.eval(newSolution);

                tmp = new Bacteria(newSolution, bx);

                if (task.problem.isFirstBetter(tmp, bx)) {
                    bx = new Bacteria(tmp);
                    if (task.problem.isFirstBetter(bx, best)) {
                        best = bx;
                        //System.out.println(task.getNumberOfEvaluations() + " " + best);
                    }
                    if (task.isStopCriterion()) break;
                } else break; //m = Ns;

            }
        }
        return bx;
    }

    private void reproduction() throws StopCriterionException {
        Collections.sort(swarm);
        for (int i = 0; i < Sr; i++)
            swarm.get(i).c = getAvgDistance(swarm.get(i).getVariables(), swarm.get(i + 1).getVariables());

        for (int i = 0; i < Sr; i++)
            swarm.set(i + Sr, new Bacteria(swarm.get(i)));
    }

    private double getAvgDistance(List<Double> x, List<Double> y) {
        double sum = 0;
        for (int i = 0; i < x.size(); i++) {
            sum += Math.abs(x.get(i) - y.get(i));
        }
        return sum / x.size();
    }

    private Bacteria eliminationAndDispersal(Bacteria b, int i) throws StopCriterionException {
        double prob = RNG.nextDouble();
        if (prob < Ped) {
            Bacteria bx = new Bacteria(task, Ci);
            if (task.problem.isFirstBetter(bx, best)) {
                best = bx;
                //System.out.println(task.getNumberOfEvaluations() + " " + best);
            }
            return bx;
        }
        return null;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}