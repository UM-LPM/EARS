package org.um.feri.ears.tuning;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.benchmark.MOAlgorithmEvalResult;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.IndicatorFactory;
import org.um.feri.ears.quality_indicator.QualityIndicator;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.statistic.rating_system.GameResult;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.glicko2.TournamentResults;
import org.um.feri.ears.util.random.RNG;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MOCRSTuning<N extends Number> {

    CRSSolution[] population;
    CRSSolution[] bestInGeneration;
    int numOfRuns = 1;
    int pop_size = 20; //30 for DE

    int D; //number of dimensions

    /* --------- jDE constants ----------- */
    final static double Finit = 0.5; // F INITIAL FACTOR VALUE
    final static double CRinit = 0.9; // CR INITIAL FACTOR VALUE

    final static double Fl = 0.1; // F in the range [0.1, 1.0]
    final static double Fu = 0.9; //

    final static double CRl = 0.0; // CR in the range [0.0, 1.0]
    final static double CRu = 1.0; //

    final static double tao1 = 0.1; // probability to adjust F
    final static double tao2 = 0.1; // probability to adjust CR

    double F, memF, CR, memCR; /* control variables of DE */
    double tmpF, tmpCR, tmp3;

    double tmp[];

    CRSSolution pold[]; // double pold[MAXPOP][MAXDIM]
    CRSSolution pnew[];
    CRSSolution pswap[];

    int r1, r2, r3; /* placeholders for random indexes */

    int num_eval = 0;
    int max_eval = 1000;
    int max_gen = 20;

    CRSSolution best;
    CRSSolution bestit;

    Class<? extends Algorithm> classAlg;
    String algName;
    ArrayList<ControlParameter> controlParameters;

    ArrayList<Task<NumberSolution<N>, NumberProblem<N>>> tasks;

    double drawLimit = 1e-7;

    List<IndicatorName> indicators;

    boolean threadForEachRun = false;


    public CRSSolution tune(Class<? extends Algorithm> classAlg, String algName, ArrayList<ControlParameter> controlParameters, ArrayList<Task<NumberSolution<N>, NumberProblem<N>>> tasks, List<IndicatorName> indicators, int popSize, int maxGen) {

        this.classAlg = classAlg;
        this.algName = algName;
        this.controlParameters = controlParameters;
        this.tasks = tasks;
        this.indicators = indicators;
        this.pop_size = popSize;
        this.max_gen = maxGen;
        //Player algorithm = new Player("IBEA");


        execute();
        //remove significantly worse and replace with new ones?

        //JDE_rand_1_bin
        return best;
    }

    private void execute() {

        init();

        pold = population; // old population (generation G)
        pnew = new CRSSolution[pop_size]; // new population (generation G+1)
        tmp = new double[D];
        bestInGeneration = new CRSSolution[max_gen];

        if (pop_size < 4) {
            System.err.println("Population must contain at least 4 members");
            return;
        }
        int gen = 0;
        while (gen < max_gen) {

            System.out.println("Current generation: " + gen);
            System.out.println("Evaulations used: " + num_eval);

            for (int i = 0; i < pop_size; i++) {
                if (num_eval >= max_eval)
                    break;

                do {
                    r1 = RNG.nextInt(pop_size);
                } while (r1 == i);

                do {
                    r2 = RNG.nextInt(pop_size);
                } while ((r2 == i) || (r2 == r1));

                do {
                    r3 = RNG.nextInt(pop_size);
                } while ((r3 == i) || (r3 == r1) || (r3 == r2));


                assignd(D, tmp, pold[i].params);
                tmpF = pold[i].getF();
                tmpCR = pold[i].getCR();
                int n = (int) (RNG.nextDouble() * D);
                // SELF-ADAPTATION OF CONTROL PARAMETERS
                if (RNG.nextDouble() < tao1) { // F
                    F = Fl + RNG.nextDouble() * Fu;
                    tmpF = F;
                } else
                    F = tmpF;
                if (RNG.nextDouble() < tao2) { // CR
                    CR = CRl + RNG.nextDouble() * CRu;
                    tmpCR = CR;
                } else
                    CR = tmpCR;

                for (int L = 0; L < D; L++) // perform D binomial trials
                {
                    if ((RNG.nextDouble() < CR) || L == (D - 1)) {
                        tmp[n] = pold[r1].params[n] + F * (pold[r2].params[n] - pold[r3].params[n]);
                    }
                    n = (n + 1) % D;
                }

	            /*for (int kk = 0; kk < D; kk++) {
	            	setFeasible(tmp[kk], kk);
				}*/


                CRSSolution br = evaluate(tmp, pold, i);
                CRSSolution trial_cost = new CRSSolution(br, tmpF, tmpCR);
                if (isFirstBetter(trial_cost, pold[i])) {
                    pnew[i] = trial_cost;
                    System.out.println("New improvment");

                } else {
                    pnew[i] = new CRSSolution(pold[i]);
                }

            }

            //recalculate rating for the new population
            createPlayers(pnew, null, -1);

            findBestSolution(pnew);

            bestInGeneration[gen] = new CRSSolution(best);

            bestit = best;
            pswap = pold;
            pold = pnew;
            pnew = pswap;

            gen++;
        }

        System.out.println("Best solution by generation:");
        for (int j = 0; j < bestInGeneration.length; j++) {
            System.out.println("Generation " + (j + 1) + ": " + bestInGeneration[j].name + " " + bestInGeneration[j].getEval());
        }

        System.out.println("Best solution found:");
        System.out.println(best.name);
        System.out.println("Rating: " + best.getEval());
    }

    private void findBestSolution(CRSSolution[] pop) {

        best = pop[0];

        for (int i = 1; i < pop.length; i++) {
            if (isFirstBetter(pop[i], best)) {
                best = pop[i];
            }
        }

        System.out.println("New best solution:");
        System.out.println(best.name);
        System.out.println("Rating: " + best.getEval());
        System.out.println("RD: " + best.p.getGlicko2Rating().getRatingDeviation());

    }

    private CRSSolution evaluate(double[] newValue, CRSSolution[] pop, int index) {

        String newName = algName + "_";
        HashMap<String, Double> params = new HashMap<String, Double>();
        for (int i = 0; i < controlParameters.size(); i++) {
            ControlParameter cp = controlParameters.get(i);
            ControlParameter dep = cp.getDependency();
            if (dep != null) {
                double nweUpperBound = params.get(dep.name);
                cp.upper_bound = nweUpperBound;
            }

            newValue[i] = setFeasible(newValue[i], i);

            newName += controlParameters.get(i).name + "(" + newValue[i] + ")";
            params.put(cp.name, newValue[i]);
        }
        CRSSolution newSolution = createSoluton(newName, params, newValue);
        createPlayers(pop, newSolution, index);
        return newSolution;
    }

    private boolean isFirstBetter(CRSSolution first, CRSSolution second) {

        return first.getEval() > second.getEval();
    }

    private double setFeasible(double value, int index) {

        ControlParameter cp = controlParameters.get(index);
        return cp.correctValue(value);
    }

    void assignd(int D, double[] a, double[] b) {
        System.arraycopy(b, 0, a, 0, D);
        // System.arraycopy(src, srcPos, dest, destPos, length)
    }

    private void init() {

        population = new CRSSolution[pop_size];
        D = controlParameters.size();
        for (int i = 0; i < pop_size; i++) {

            String newName = algName + "_";
            HashMap<String, Double> params = new HashMap<String, Double>();
            double[] solParams = new double[controlParameters.size()];
            int ind = 0;
            for (ControlParameter cp : controlParameters) {

                ControlParameter dep = cp.getDependency();
                if (dep != null) {
                    double nweUpperBound = params.get(dep.name);
                    cp.upper_bound = nweUpperBound;
                }

                double value = cp.randomValue();
                newName += cp.name + "(" + value + ")";
                params.put(cp.name, value);
                solParams[ind++] = value;
            }

            CRSSolution newSolution = createSoluton(newName, params, solParams);
            //add to population
            population[i] = newSolution;

        }
        createPlayers(population, null, -1);

        //save best solution
        CRSSolution currentBest = population[0];
        for (int i = 1; i < population.length; i++) {
            if (isFirstBetter(population[i], currentBest)) {
                currentBest = population[i];
            }
        }
        best = currentBest;
        bestit = currentBest;
    }


    private CRSSolution createSoluton(String name, HashMap<String, Double> params, double[] solParams) {

        //fill player game list
        CRSSolution newSolution = fillPlayerGameList(name, params);

        newSolution.params = solParams;
        newSolution.name = name;
        //set initial CR and F
        newSolution.setCR(CRinit);
        newSolution.setF(Finit);

        return newSolution;
    }

    private MOAlgorithm createObject(String name) {
        //create constructor
        Constructor<?> ctor = null;
        try {
            //classAlg = Class.forName(classAlg);
            ctor = classAlg.getConstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Create new algorithm instance
        MOAlgorithm algorithmObject = null;
        try {
            algorithmObject = (MOAlgorithm) ctor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }

        //Set algorithm name
        AlgorithmInfo ai = algorithmObject.getAlgorithmInfo();
        ai.setAcronym(name);
        algorithmObject.setAlgorithmInfo(ai);

        return algorithmObject;
    }

    private void setParameters(Object object, HashMap<String, Double> params) {

        Class<?> current = object.getClass();
        while (current.getSuperclass() != null) { // we don't want to process Object.class
            if (current.getSimpleName().equals("MOAlgorithm") || current.getSimpleName().equals("Algorithm")) {
                return;
            }
            Field[] fields = current.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (!field.getType().isPrimitive()) {
                        try {
                            field.setAccessible(true);
                            Object objectMember = field.get(object);
                            if (objectMember != null) {
                                setParameters(objectMember, params);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        for (Entry<String, Double> cursor : params.entrySet()) {
                            if (cursor.getKey().equals(field.getName())) {
                                field.setAccessible(true);
                                try {
                                    if (field.getType() == int.class || field.getType() == Integer.class)
                                        field.setInt(object, cursor.getValue().intValue());
                                    else
                                        field.setDouble(object, cursor.getValue());
                                } catch (IllegalArgumentException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                }
            }
            current = current.getSuperclass();
        }
    }

    private CRSSolution fillPlayerGameList(String name, HashMap<String, Double> params) {

        long gamesTime = System.currentTimeMillis();
        //create default object
        MOAlgorithm defaultObject = createObject(name);
        setParameters(defaultObject, params);

        System.out.println("Fill player game list for: " + name);
        int threads = Runtime.getRuntime().availableProcessors();
        CRSSolution sol = new CRSSolution();

        if (threadForEachRun) {
            for (Task<NumberSolution<N>, NumberProblem<N>> task : tasks) {
                //System.out.println("Current task: "+task.getProblemName());
                try {
                    ExecutorService service = Executors.newFixedThreadPool(threads);

                    Set<Future<AlgorithmRunResult>> set = new HashSet<Future<AlgorithmRunResult>>();
                    for (int i = 0; i < numOfRuns; i++) {

                        //create new object for each thread
                        MOAlgorithm object = createObject(name);
                        setParameters(object, params);

                        Future<AlgorithmRunResult> future = service.submit(object.createRunnable(object, task.clone()));
                        set.add(future);
                    }

                    for (Future<AlgorithmRunResult> future : set) {

                        AlgorithmRunResult<ParetoSolution<Double>, NumberSolution<Double>, NumberProblem<Double>, MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>> res = future.get();
                        sol.allGamesPlayed.add(new MOAlgorithmEvalResult(res.solution, defaultObject, res.task));
                    }

                    service.shutdown();
                    service.awaitTermination(10, TimeUnit.HOURS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {

            //System.out.println("Current task: "+task.getProblemName());
            List<AlgorithmRunResult> futureResults = new ArrayList<AlgorithmRunResult>();
            try {
                ExecutorService service = Executors.newFixedThreadPool(threads);

                Set<Future<AlgorithmRunResult>> set = new HashSet<Future<AlgorithmRunResult>>();
                for (Task<NumberSolution<N>, NumberProblem<N>> task : tasks) {
                    //create new object for each thread
                    MOAlgorithm object = createObject(name);
                    setParameters(object, params);

                    Future<AlgorithmRunResult> future = service.submit(object.createRunnable(object, task.clone()));
                    set.add(future);
                }

                for (Future<AlgorithmRunResult> future : set) {
                    AlgorithmRunResult res = future.get();
                    futureResults.add(res);
                }

                //Order results by tasks
                for (Task<NumberSolution<N>, NumberProblem<N>> task : tasks) {
                    for (AlgorithmRunResult<ParetoSolution<Double>, NumberSolution<Double>, NumberProblem<Double>, MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>> res : futureResults) {
                        if (task.getProblemName().equals(res.task.getProblemName())) {
                            sol.allGamesPlayed.add(new MOAlgorithmEvalResult(res.solution, defaultObject, res.task));
                            break;
                        }
                    }
                }

                service.shutdown();
                service.awaitTermination(10, TimeUnit.HOURS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long estimatedTime = (System.currentTimeMillis() - gamesTime) / 1000;
        System.out.println("Filling games time: " + estimatedTime + "s");
        return sol;
    }

    private HashMap<IndicatorName, Double> evaluateWithQI(Task<NumberSolution<N>, NumberProblem<N>> task, ParetoSolution result) {

        HashMap<IndicatorName, Double> indicatorValues = new HashMap<IndicatorName, Double>();

        for (IndicatorName indicatorName : indicators) {
            QualityIndicator qi = IndicatorFactory.createIndicator(indicatorName, task.problem.getNumberOfObjectives(), task.problem.getReferenceSetFileName());
            try {
                result.evaluate(qi);
            } catch (Exception e) {
                e.printStackTrace();
            }
            indicatorValues.put(indicatorName, result.getEval());
        }

        return indicatorValues;
    }

    // Creates evaluated player for provided solution, if you want to create players for the whole population
    // set solution to null and inedx to -1
    private void createPlayers(CRSSolution[] population, CRSSolution newSolution, int solIndex) {

        long evalTime = System.currentTimeMillis();

        if (newSolution != null)
            System.out.println("Creating player for: " + newSolution.name);

        TournamentResults arena = new TournamentResults(100);
        //add players to the arena
        for (int i = 0; i < population.length; i++) {

            if (i == solIndex)
                arena.addPlayer(new Player(newSolution.name));
            else
                arena.addPlayer(new Player(population[i].name));
        }

        int index = 0;
        for (Task<NumberSolution<N>, NumberProblem<N>> task : tasks) {

            for (int k = 0; k < numOfRuns; k++) {

                ArrayList<MOAlgorithmEvalResult> sameGameResults = new ArrayList<MOAlgorithmEvalResult>();
                for (int i = 0; i < population.length; i++) {
                    if (i == solIndex)
                        sameGameResults.add(newSolution.allGamesPlayed.get(index));
                    else
                        sameGameResults.add(population[i].allGamesPlayed.get(index));
                }
                //TODO check if task in allGamesPlayed is the same as task
                for (IndicatorName indicatorName : indicators) {

                    FitnessComparator fc;

                    QualityIndicator qi = IndicatorFactory.createIndicator(indicatorName, task.problem.getNumberOfObjectives(), task.problem.getReferenceSetFileName());

                    fc = new FitnessComparator(task, qi);

                    MOAlgorithmEvalResult win;
                    MOAlgorithmEvalResult lose;
                    Collections.sort(sameGameResults, fc); //best first
                    for (int i = 0; i < sameGameResults.size() - 1; i++) {
                        win = sameGameResults.get(i);
                        for (int j = i + 1; j < sameGameResults.size(); j++) {
                            lose = sameGameResults.get(j);
                            if (resultEqual(win.getBest(), lose.getBest(), qi)) {
                                arena.addGameResult(GameResult.DRAW, win.getAl().getAlgorithmInfo().getAcronym(), lose.getAl().getAlgorithmInfo().getAcronym(), task.getProblemName(), qi.getName());
                            } else {
                                if (win.getAl() == null) {
                                    System.out.println("NULL ID " + win.getClass().getName());
                                }
                                if (win.getBest() == null) {
                                    System.out.println(win.getAl().getId() + " NULL");
                                }
                                if (lose.getAl() == null) {
                                    System.out.println("NULL ID " + lose.getClass().getName());
                                }
                                if (lose.getBest() == null) {
                                    System.out.println(lose.getAl().getId() + " NULL");
                                }
                                arena.addGameResult(GameResult.WIN, win.getAl().getAlgorithmInfo().getAcronym(), lose.getAl().getAlgorithmInfo().getAcronym(), task.getProblemName(), qi.getName());
                            }
                        }
                    }
                }
                index++;
            }
        }

        arena.calculateRatings();

        //set players

        long estimatedTime = (System.currentTimeMillis() - evalTime) / 1000;

        if (solIndex == -1) { //update all players
            for (CRSSolution sol : population) {
                num_eval++;
                sol.p = arena.getPlayer(sol.name);
            }
            System.out.println("Evaluation time for all players: " + estimatedTime + "s");

        } else {
            newSolution.p = arena.getPlayer(newSolution.name);
            System.out.println("Evaluation for one player: " + estimatedTime + "s");
            num_eval++;
        }
    }

    public boolean resultEqual(ParetoSolution a, ParetoSolution b, QualityIndicator qi) {
        if ((a == null) && (b == null)) return true;
        if (a == null) return false;
        if (b == null) return false;
        if (qi.getIndicatorType() == IndicatorType.UNARY)
            return qi.isEqual(a, b, drawLimit);
        else if (qi.getIndicatorType() == IndicatorType.BINARY) {
            if (qi.compare(a, b, drawLimit) == 0) {
                return true;
            }
        }
        return false;
    }

    class FitnessComparator implements Comparator<MOAlgorithmEvalResult> {
        Task<NumberSolution<N>, NumberProblem<N>> t;
        QualityIndicator qi;

        public FitnessComparator(Task<NumberSolution<N>, NumberProblem<N>> t, QualityIndicator qi) {
            this.t = t;
            this.qi = qi;
        }

        @Override
        public int compare(MOAlgorithmEvalResult arg0, MOAlgorithmEvalResult arg1) {
            if (arg0.getBest() != null) {
                if (arg1.getBest() != null) {
                    // if (resultEqual(arg0.getBest(), arg1.getBest())) return 0; Normal sor later!
                    if (qi.getIndicatorType() == IndicatorType.UNARY) {
                        try {
                            arg0.getBest().evaluate(qi, true);
                            arg1.getBest().evaluate(qi, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        if (t.problem.isFirstBetter(arg0.getBest(), arg1.getBest(), qi)) return -1;
                        else return 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else return -1; //second is null
            } else if (arg1.getBest() != null) return 1; //first null
            return 0; //both equal
        }
    }
}
