package org.um.feri.ears.experiment.ee;

import org.um.feri.analyse.sopvisualization.AncestorUtil;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.Griewank;
import org.um.feri.ears.problems.unconstrained.Rastrigin;
import org.um.feri.ears.problems.unconstrained.RosenbrockDeJong2;
import org.um.feri.ears.problems.unconstrained.Schwefel226;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.Util;

public class SOBenchmarkExample {

    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());

        Problem[] problems = new Problem[5];
        int[] dimmensions = {10};
        PSOoriginalLogging psoLogging = new PSOoriginalLogging();
        JADELogging jadeLogging = new JADELogging();
        DEAlgorithmLogging deLogging = new DEAlgorithmLogging(DEAlgorithmLogging.DE_best_1_bin);

        for (int i = 0; i < dimmensions.length; ++i) {
            problems[0] = new Sphere(dimmensions[i]);
            problems[1] = new Griewank(dimmensions[i]);
            problems[2] = new Rastrigin(dimmensions[i]);
            problems[3] = new RosenbrockDeJong2(dimmensions[i]);
            problems[4] = new Schwefel226(dimmensions[i]);

            for (int pr = 0; pr < problems.length; ++pr) {
                Problem p = problems[pr];
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
