package org.um.feri.ears.statistic.friedman;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.MeanStDev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class FriedmanTransport {
    private double[][] mean;
    private Vector<String> algoritms;
    private Vector<String> datasets;

    public FriedmanTransport(double[][] mean, Vector<String> algoritms, Vector<String> datasets) {
        super();
        this.mean = mean;
        this.algoritms = algoritms;
        this.datasets = datasets;
    }

    public void print() {
        System.out.println(algoritms);
        System.out.println(datasets);
        System.out.println(Arrays.toString(mean));
    }

    //allSingleProblemRunResults.add(task, result, algorithm);
    //Hashtable<AlgorithmBase, Hashtable<Task, ArrayList<Double>>> all
    public static FriedmanTransport calc4Friedman(Hashtable<NumberAlgorithm, Hashtable<Task, ArrayList<AlgorithmRunResult<ParetoSolution<Double>, NumberSolution<Double>, NumberProblem<Double>, MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>>>>> all) {
        //FriedmanTransport
        double[][] mean;
        Vector<String> algorithms = new Vector<String>();
        Vector<String> datasets = new Vector<String>();
        StringBuffer sb = new StringBuffer();
        ArrayList<Algorithm> tmp = new ArrayList<>(all.keySet());
        ArrayList<Task> tmpProblem; // = new ArrayList(all.keySet());
        Hashtable<Task, ArrayList<AlgorithmRunResult<ParetoSolution<Double>, NumberSolution<Double>, NumberProblem<Double>, MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>>>> algorithmHm;
        ArrayList<AlgorithmRunResult<ParetoSolution<Double>, NumberSolution<Double>, NumberProblem<Double>, MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>>> algorithmRunResults;
        MeanStDev std;
        mean = null;

        int i = -1;
        int j;
        for (Algorithm a : tmp) {
            i++;
            algorithms.add(a.getId());
            algorithmHm = all.get(a);
            tmpProblem = new ArrayList<>(algorithmHm.keySet());
            //mean[i] = new double[tmpProblem.size()];
            j = -1;
            for (Task p : tmpProblem) {
                j++;
                if (i == 0) {
                    if (j == 0) {
                        mean = new double[tmpProblem.size()][];
                        for (int g = 0; g < tmpProblem.size(); g++) {
                            mean[g] = new double[tmp.size()];
                        }
                    }

                    datasets.add(p.getProblemName());
                }
                algorithmRunResults = algorithmHm.get(p);
                ArrayList<Double> results = new ArrayList<>();
                for(AlgorithmRunResult<ParetoSolution<Double>, NumberSolution<Double>, NumberProblem<Double>, MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>> res: algorithmRunResults)
                    results.add(res.solution.getEval());
                std = new MeanStDev(results);
                sb.append(a.getId()).append('\t').append(p.getProblemName());
                sb.append('\t').append(std.getMean());
                sb.append("\n");
                mean[j][i] = std.mean;
            }
        }
        System.out.println(sb);
        return new FriedmanTransport(mean, algorithms, datasets);
    }

    public double[][] getMean() {
        return mean;
    }

    public Vector<String> getAlgoritms() {
        return algoritms;
    }

    public Vector<String> getDatasets() {
        return datasets;
    }


}
