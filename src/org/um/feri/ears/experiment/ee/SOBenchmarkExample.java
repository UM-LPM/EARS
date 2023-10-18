package org.um.feri.ears.experiment.ee;

import org.um.feri.analyse.sopvisualization.AncestorUtil;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

public class SOBenchmarkExample {

    public static void main(String[] args) {
        DoubleProblem[] problems = new DoubleProblem[5];
        int[] dimmensions = {10};
        PSO psoLogging = new PSO();
        JADE jadeLogging = new JADE();
        DEAlgorithm deLogging = new DEAlgorithm(DEAlgorithm.Strategy.DE_BEST_1_BIN);

        for (int i = 0; i < dimmensions.length; ++i) {
            problems[0] = new Sphere(dimmensions[i]);
            problems[1] = new Griewank(dimmensions[i]);
            problems[2] = new Rastrigin(dimmensions[i]);
            problems[3] = new RosenbrockDeJong2(dimmensions[i]);
            problems[4] = new Schwefel226(dimmensions[i]);

            for (int pr = 0; pr < problems.length; ++pr) {
                DoubleProblem p = problems[pr];
                try {
                    Task t = new Task(p, StopCriterion.EVALUATIONS, 5000 * dimmensions[i], 0, 0);
                    t.enableAncestorLogging();
                    psoLogging.execute(t);
                    AncestorUtil.saveAncestorLogging(psoLogging.getId() + "_" + p.getName(), t);
                    AncestorUtil.saveGraphingFile(psoLogging.getId() + "_" + p.getName(), t, psoLogging);
                } catch (StopCriterionException e) {
                    e.printStackTrace();
                }

                Task.resetLoggingID();
                try {
                    Task t = new Task(p, StopCriterion.EVALUATIONS, 5000 * dimmensions[i], 0, 0);
                    t.enableAncestorLogging();
                    jadeLogging.execute(t);
                    AncestorUtil.saveAncestorLogging(jadeLogging.getId() + "_" + p.getName(), t);
                    AncestorUtil.saveGraphingFile(jadeLogging.getId() + "_" + p.getName(), t, jadeLogging);
                } catch (StopCriterionException e) {
                    e.printStackTrace();
                }

                Task.resetLoggingID();
                try {
                    Task t = new Task(p, StopCriterion.EVALUATIONS, 5000 * dimmensions[i], 0, 0);
                    t.enableAncestorLogging();
                    deLogging.execute(t);
                    AncestorUtil.saveAncestorLogging(deLogging.getId().replaceAll("/", "-") + "_" + p.getName(), t);
                } catch (StopCriterionException e) {
                    e.printStackTrace();
                }
                Task.resetLoggingID();

            }
        }
    }
}
