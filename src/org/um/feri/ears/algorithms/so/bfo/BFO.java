package org.um.feri.ears.algorithms.so.bfo;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collections;

public class BFO extends Algorithm {
    private int S, Nc, Ns, Nre, Ned, Sr;
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

    public BFO(int S, int Nc, int Ns, int Nre, int Ned, double Ped) {
        this.S = S;
        this.Nc = Nc;
        this.Ns = Ns;
        this.Nre = Nre;
        this.Ned = Ned;
        this.Ped = Ped;
        Sr = S / 2;
        swarm = new ArrayList<>();
        ai = new AlgorithmInfo("", "", "BFO", "Bacterial Foraging Optimization");  //EARS add algorithm name
        au = new Author("Monika Bozhinova", "N/A"); //EARS author info
    }

    private void initSwarm(Task task) throws StopCriteriaException {
        swarm.clear();
        best = null;
        for (int i = 0; i < S; i++) {
            tmp = new Bacteria(task, Ci);
            if (best == null) best = tmp;
            else if (task.isFirstBetter(tmp, best)) {
                best = tmp;
                //System.out.println(task.getNumberOfEvaluations() + " " + best);
            }
            swarm.add(tmp);
            if (task.isStopCriteria()) break;
        }
    }

    private Bacteria chemotaxis(Bacteria b, Task task) throws StopCriteriaException {
        double[] di = new double[task.getNumberOfDimensions()];
        double[] cb = b.getDoubleVariables();
        double[] xn = new double[task.getNumberOfDimensions()];
        double rootProduct = 0;
        for (int i = 0; i < di.length; i++) {
            di[i] = 2 * Util.nextDouble() - 1;
            rootProduct += Math.pow(di[i], 2);
        }
        for (int i = 0; i < xn.length; i++) {
            xn[i] = task.setFeasible(cb[i] + b.c * (di[i] / Math.sqrt(rootProduct)), i);
        }
        Bacteria bx = new Bacteria(task.eval(xn), b);
        if (task.isStopCriteria()) return bx;

        if (task.isFirstBetter(bx, best)) {
            best = bx;
            //System.out.println(task.getNumberOfEvaluations() + " " + best);
        }

        if (task.isFirstBetter(bx, bx.prev)) {
            int m = 0;
            while (m < Ns) {
                m++;
                for (int i = 0; i < xn.length; i++) {
                    xn[i] = task.setFeasible(xn[i] + bx.c * (di[i] / Math.sqrt(rootProduct)), i);
                }
                tmp = new Bacteria(task.eval(xn), bx);

                if (task.isFirstBetter(tmp, bx)) {
                    bx = new Bacteria(tmp);
                    if (task.isFirstBetter(bx, best)) {
                        best = bx;
                        //System.out.println(task.getNumberOfEvaluations() + " " + best);
                    }
                    if (task.isStopCriteria()) break;
                } else break; //m = Ns;

            }
        }
        return bx;
    }

    private void reproduction() throws StopCriteriaException {
        Collections.sort(swarm);
        for (int i = 0; i < Sr; i++)
            swarm.get(i).c = getAvgDistance(swarm.get(i).getDoubleVariables(), swarm.get(i + 1).getDoubleVariables());

        for (int i = 0; i < Sr; i++)
            swarm.set(i + Sr, new Bacteria(swarm.get(i)));
    }

    private double getAvgDistance(double[] x, double[] y) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.abs(x[i] - y[i]);
        }
        return sum / x.length;
    }

    private Bacteria eliminationAndDispersal(Bacteria b, Task task, int i) throws StopCriteriaException {
        double prob = Util.nextDouble();
        if (prob < Ped) {
            Bacteria bx = new Bacteria(task, Ci);
            if (task.isFirstBetter(bx, best)) {
                best = bx;
                //System.out.println(task.getNumberOfEvaluations() + " " + best);
            }
            return bx;
        }
        return null;
    }

    @Override
    public DoubleSolution execute(Task task) throws StopCriteriaException {
        Ci = getAvgDistance(task.getUpperLimit(), task.getLowerLimit()) / 5;

        initSwarm(task);
        while (!task.isStopCriteria()) {
            for (int l = 0; l < Ned; l++) {
                for (int k = 0; k < Nre; k++) {
                    for (int j = 0; j < Nc; j++) {
                        for (int i = 0; i < S; i++) {
                            swarm.get(i).health = 0;
                            tmp = chemotaxis(swarm.get(i), task);
                            if (task.isFirstBetter(tmp, swarm.get(i))) swarm.set(i, tmp);
                            if (task.isStopCriteria()) return best;
                        }
                    }
                    reproduction();
                    if (task.isStopCriteria()) return best;
                }
                for (int i = 0; i < S; i++) {
                    tmp = eliminationAndDispersal(swarm.get(i), task, i);
                    if (tmp != null) swarm.set(i, tmp);
                    if (task.isStopCriteria()) return best;
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}