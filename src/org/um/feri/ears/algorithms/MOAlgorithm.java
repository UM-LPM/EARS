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

public abstract class MOAlgorithm<P extends Problem<NumberSolution<Type>>, T extends MOTask<Type>, Type extends Number> extends Algorithm<T, ParetoSolution<Type>> {

    protected T task;
    protected static boolean optimalParam;

    protected ParetoSolution<Type> result;

    private HashMap<String, Integer> positions = new HashMap<String, Integer>();  //stores the position of the current solution
    private HashMap<String, List<ParetoSolution<Type>>> all_solutions = new HashMap<String, List<ParetoSolution<Type>>>();
    private HashMap<String, Integer[]> permutations = new HashMap<String, Integer[]>();
    private ParetoSolutionCache cache;
    private String benchmarkInCache;
    protected ParetoSolution<Type> best;
    protected int numVar;
    protected int numObj;

    public ParetoSolution<Type> getLastResult() {
        return result;
    }

    protected ParetoSolution<Type> returnNext(MOTask task) {
        // load cache if empty or if different benchmark in cache
        if (cache == null || !benchmarkInCache.equals(task.getBenchmarkName())) {
            cache = Util.readParetoListFromJSON(ai.getAcronym(), task.getBenchmarkName());
            if (cache != null) {
                for (Entry<String, List<ParetoWithEval>> s : cache.data.entrySet()) {
                    List<ParetoSolution<Type>> solutions = new ArrayList<ParetoSolution<Type>>();
                    //List<List<double[]>> ps = cache.data.get(listID);

                    List<ParetoWithEval> ps = s.getValue();

                    for (ParetoWithEval pareto : ps) {
                        ParetoSolution<Type> solution = new ParetoSolution<Type>(pareto.pareto.size());

                        for (double[] obj : pareto.pareto) {
                            NumberSolution<Type> sol = new NumberSolution<Type>(obj.length);
                            sol.setObjectives(obj);
                            solution.add(sol);
                        }
                        solution.setEvalForAllUnaryQIs(pareto.qiEval);

                        solutions.add(solution);
                    }
                    all_solutions.put(s.getKey(), solutions);
                    positions.put(s.getKey(), 0);
                    Integer[] per = ArrayUtils.toObject(Util.randomPermutation(all_solutions.get(s.getKey()).size()));
                    permutations.put(s.getKey(), per);
                }
                benchmarkInCache = task.getBenchmarkName();
            } else {
                System.out.println("Cache does not exists!");
                return null;
            }
        }

        String key = getCacheKey(task.getTaskInfo());

        if (all_solutions.containsKey(key)) {
            if (caching == Cache.RANDOM) {
                List<ParetoSolution<Type>> pareto = all_solutions.get(key);
                return pareto.get(Util.nextInt(pareto.size()));
            }
            if (caching == Cache.ROUND_ROBIN) {
                List<ParetoSolution<Type>> pareto = all_solutions.get(key);
                int index = positions.get(key);
                if (index >= pareto.size()) {
                    index = 0;
                    positions.put(key, 0);
                }
                positions.put(key, positions.get(key) + 1);

                return pareto.get(index);
            }
            if (caching == Cache.RANDOM_PERMUTATION) {
                List<ParetoSolution<Type>> pareto = all_solutions.get(key);
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
    public ParetoSolution<Type> execute(T task) throws StopCriterionException {
        this.task = task;
        numVar = task.problem.getNumberOfDimensions();
        numObj = task.problem.getNumberOfObjectives();

        //ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
        long initTime = System.currentTimeMillis();
        init();

        // check cache after initialization when all parameters are set
        if (caching != Cache.NONE && caching != Cache.SAVE) {
            ParetoSolution<Type> next = returnNext(task);
            if (next != null)
                return next;
            else
                System.out.println("No solution found in chache for algorithm: " + ai.getAcronym() + " on problem: " + task.getProblemName());
        }

        start();
        long estimatedTime = System.currentTimeMillis() - initTime;
        //System.out.println("Total execution time: "+estimatedTime + "ms");

        Ranking<Type> ranking = new Ranking<>(best);
        best = ranking.getSubfront(0);

        if (saveData) {
            String algName = this.getAlgorithmInfo().getAcronym();
            best.saveParetoImage(algName, task.getProblemName());
            best.printFeasibleFUN("FUN_" + algName);
            best.printVariablesToFile("VAR_" + algName);
            best.printObjectivesToCSVFile("FUN_" + algName);
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
            Util.addParetoToJSON(getCacheKey(task.getTaskInfo()), task.getBenchmarkName(), ai.getAcronym(), best);
        }

        return best;
    }

    protected abstract void init() throws StopCriterionException;

    protected abstract void start() throws StopCriterionException;

}
