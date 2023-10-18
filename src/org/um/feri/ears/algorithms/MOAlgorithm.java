package org.um.feri.ears.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.ParetoSolutionCache;
import org.um.feri.ears.util.ParetoWithEval;
import org.um.feri.ears.util.Ranking;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

public abstract class MOAlgorithm<N extends Number, S extends Solution, P extends Problem<S>> extends Algorithm<ParetoSolution<N>, S, P> {

    protected static boolean optimalParam;

    protected ParetoSolution<N> result;

    private HashMap<String, Integer> positions = new HashMap<String, Integer>();  //stores the position of the current solution
    private HashMap<String, List<ParetoSolution<N>>> all_solutions = new HashMap<>();
    private HashMap<String, Integer[]> permutations = new HashMap<String, Integer[]>();
    private ParetoSolutionCache cache;
    private String benchmarkInCache;
    protected ParetoSolution<N> best;
    protected int numObj;

    public ParetoSolution<N> getLastResult() {
        return result;
    }

    protected ParetoSolution<N> returnNext(Task task) {
        // load cache if empty or if different benchmark in cache
        if (cache == null || !benchmarkInCache.equals(task.problem.getBenchmarkName())) {
            cache = Util.readParetoListFromJSON(ai.getAcronym(), task.problem.getBenchmarkName());
            if (cache != null) {
                for (Entry<String, List<ParetoWithEval>> s : cache.data.entrySet()) {
                    List<ParetoSolution<N>> solutions = new ArrayList<>();
                    //List<List<double[]>> ps = cache.data.get(listID);

                    List<ParetoWithEval> ps = s.getValue();

                    for (ParetoWithEval pareto : ps) {
                        ParetoSolution<N> solution = new ParetoSolution<>(pareto.pareto.size());

                        for (double[] obj : pareto.pareto) {
                            NumberSolution<N> sol = new NumberSolution<>(obj.length);
                            sol.setObjectives(obj);
                            solution.add(sol);
                        }
                        solution.setEvalForAllUnaryQIs(pareto.qiEval);

                        solutions.add(solution);
                    }
                    all_solutions.put(s.getKey(), solutions);
                    positions.put(s.getKey(), 0);
                    Integer[] per = ArrayUtils.toObject(RNG.randomPermutation(all_solutions.get(s.getKey()).size()));
                    permutations.put(s.getKey(), per);
                }
                benchmarkInCache = task.problem.getBenchmarkName();
            } else {
                System.out.println("Cache does not exists!");
                return null;
            }
        }

        String key = getCacheKey(task.getTaskInfo());

        if (all_solutions.containsKey(key)) {
            if (caching == Cache.RANDOM) {
                List<ParetoSolution<N>> pareto = all_solutions.get(key);
                return pareto.get(RNG.nextInt(pareto.size()));
            }
            if (caching == Cache.ROUND_ROBIN) {
                List<ParetoSolution<N>> pareto = all_solutions.get(key);
                int index = positions.get(key);
                if (index >= pareto.size()) {
                    index = 0;
                    positions.put(key, 0);
                }
                positions.put(key, positions.get(key) + 1);

                return pareto.get(index);
            }
            if (caching == Cache.RANDOM_PERMUTATION) {
                List<ParetoSolution<N>> pareto = all_solutions.get(key);
                int index = positions.get(key);
                if (index >= pareto.size()) {
                    index = 0;
                    positions.put(key, 0);
                }
                positions.put(key, positions.get(key) + 1);
                index = permutations.get(key)[index];
                return pareto.get(index);
            }
        }
        System.out.println("No match in cache for key: " + key);
        return null;
    }

    public static boolean isRunWithOptimalParameters() {
        return optimalParam;
    }

    public static void setRunWithOptimalParameters(boolean runWithOptimalParameters) {
        MOAlgorithm.optimalParam = runWithOptimalParameters;
    }

    @Override
    public ParetoSolution<N> execute(Task task) throws StopCriterionException {
        this.task = task;
        numObj = task.problem.getNumberOfObjectives();

        //ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
        long initTime = System.currentTimeMillis();
        init();

        // check cache after initialization when all parameters are set
        if (caching != Cache.NONE && caching != Cache.SAVE) {
            ParetoSolution<N> next = returnNext(task);
            if (next != null)
                return next;
            else
                System.out.println("No solution found in chache for algorithm: " + ai.getAcronym() + " on problem: " + task.getProblemName());
        }

        start();
        long estimatedTime = System.currentTimeMillis() - initTime;
        //System.out.println("Total execution time: "+estimatedTime + "ms");

        Ranking<N> ranking = new Ranking<>(best);
        best = ranking.getSubfront(0);

        if (saveData) {
            String algName = this.getAlgorithmInfo().getAcronym();
            best.saveParetoImage(algName, task.getProblemName());
            best.printFeasibleFUN("FUN_" + algName);
            best.saveVariablesToFile("VAR_" + algName);
            best.saveObjectivesToCSVFile("FUN_" + algName);
        }
        if (displayData) {
            best.displayAllUnaryQualityIndicators(task.problem.getNumberOfObjectives(), task.problem.getReferenceSetFileName());
            best.displayData(this.getAlgorithmInfo().getAcronym(), task.getProblemName());
        }

        if (caching == Cache.SAVE) {
            try {
                best.evaluateWithAllUnaryQI(numObj, task.problem.getReferenceSetFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Util.addParetoToJSON(getCacheKey(task.getTaskInfo()), task.problem.getBenchmarkName(), ai.getAcronym(), best);
        }

        return best;
    }

    protected abstract void init() throws StopCriterionException;

    protected abstract void start() throws StopCriterionException;

}
