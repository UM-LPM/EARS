package org.um.feri.ears.algorithms.so.laf;

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

import java.util.ArrayList;
import java.util.List;

public class LaF extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private boolean debug = true;

    private ArrayList<NumberSolution<Double>> leaders;
    private ArrayList<NumberSolution<Double>> followers;

    private List<Double> ub;
    private List<Double> lb;
    private int dimension;

    public LaF() {
        this(20);
    }

    public LaF(int popSize) {

        super();
        setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("LaF", "Leaders and Followers",
                "@article{Gonzalez-Fernandez2015,"
                        + "title={Leaders and Followers - A New Metaheuristic to Avoid the Bias of Accumulated Information},"
                        + "author={Gonzalez-Fernandez, Yasser and Chen, Stephen},"
                        + "proceedings={2015 IEEE Congress on Evolutionary Computation },"
                        + "pages={776--783},"
                        + "year={2015},"
                        + "publisher={IEEE press}}"
        );
        au = new Author("alex", "shliu@mail.fresnostate.edu"); //EARS author info

        this.popSize = popSize;
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException { //EARS main evaluation loop
        this.task = task;
        NumberSolution<Double> best = null;
        dimension = task.problem.getNumberOfDimensions();
        ub = task.problem.getUpperLimit();
        lb = task.problem.getLowerLimit();

        initPopulation();
        int leaderIndex;
        int followerIndex;
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion())
                    break;
                leaderIndex = RNG.nextInt(popSize);
                NumberSolution<Double> leader = leaders.get(leaderIndex);
                followerIndex = RNG.nextInt(popSize);
                NumberSolution<Double> follower = followers.get(followerIndex);
                NumberSolution<Double> trial = trial(leader, follower); //one fit eval here
                //System.out.println(trailCost);
                if (task.problem.isFirstBetter(trial, followers.get(followerIndex))) //eval done earlier
                    followers.set(followerIndex, trial);
            }
            if (task.problem.isFirstBetter(findMedianSolution(followers), findMedianSolution(leaders))) {
                leaders = merge(followers, leaders);
            }
            best = new NumberSolution<>(leaders.get(findMinIndex(leaders)));
            task.incrementNumberOfIterations();
        }
        return best;
    }

    public void initPopulation() throws StopCriterionException {
        leaders = new ArrayList<>();
        followers = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> newLeader = new NumberSolution<>(task.getRandomEvaluatedSolution());
            leaders.add(newLeader);
            if (task.isStopCriterion())
                break;
        }
        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> newFollower = new NumberSolution<>(task.getRandomEvaluatedSolution());
            followers.add(newFollower);
            if (task.isStopCriterion())
                break;
        }
    }

    private NumberSolution<Double> trial(NumberSolution<Double> leader, NumberSolution<Double> follower) throws StopCriterionException {
        double maxStep = 2.0;
        double[] gap = new double[dimension];
        ;
        double[] farthest = new double[dimension];
        double[] maxSteps = new double[dimension];
        double[] result = new double[dimension];

        for (int i = 0; i < dimension; i++) {
            maxSteps[i] = maxStep * 1;
        }
        for (int i = 0; i < dimension; i++) {
            gap[i] = leader.getValue(i) - follower.getValue(i);
            farthest[i] = follower.getValue(i) + maxStep * gap[i];
            if (farthest[i] < lb.get(i)) {
                maxSteps[i] = (lb.get(i) - follower.getValue(i)) / (ub.get(i) - lb.get(i));
            }
            if (farthest[i] > ub.get(i)) {
                maxSteps[i] = (ub.get(i) - follower.getValue(i)) / (ub.get(i) - lb.get(i));
            }
            double rand = RNG.nextDouble();
            result[i] = follower.getValue(i) + maxSteps[i] * rand * gap[i];
        }

        task.problem.setFeasible(result); //fixes upper and lower bound

        NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(result));
        task.eval(newSolution);

        return newSolution;
    }

    private NumberSolution<Double> findMedianSolution(ArrayList<NumberSolution<Double>> mArrayList) {
        mArrayList.sort(new ProblemComparator<NumberSolution<Double>>(task.problem));
        return mArrayList.get(popSize / 2);
    }

    private ArrayList<NumberSolution<Double>> merge(ArrayList<NumberSolution<Double>> followers, ArrayList<NumberSolution<Double>> leaders) {
        ArrayList<NumberSolution<Double>> merged = new ArrayList<>(leaders);
        merged.addAll(followers);
        boolean[] selected = new boolean[merged.size()];

		ArrayList<NumberSolution<Double>> selectedLeaders = new ArrayList<>(popSize);

        //add best solution
        int min_index = findMinIndex(merged);
        selectedLeaders.add(merged.get(min_index));
        selected[min_index] = true;

        for (int i = 0; i < popSize; i++) {
            int[] pick2 = randomSample(selected);
            int winner_index = compete(merged, pick2);
            selectedLeaders.add(merged.get(winner_index));
            selected[winner_index] = true;

        }
        return selectedLeaders;
    }

    //return the index of smallest eval
	private int findMinIndex(ArrayList<NumberSolution<Double>> newLeaders) {
        int i = 0;
        int minIndex = i;
        for (i = 1; i < newLeaders.size(); i++) {
            if (task.problem.isFirstBetter(newLeaders.get(i), newLeaders.get(minIndex))) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    //return an array w/ two int that are not selected before
	private int[] randomSample(boolean[] selected) {
        int[] pick2 = {0, 0};
        int r1 = RNG.nextInt(selected.length);
        while (selected[r1]) {
            r1 = RNG.nextInt(selected.length);
        }

        int r2 = RNG.nextInt(selected.length);
        while (selected[r2]) {
            r2 = RNG.nextInt(selected.length);
        }
        pick2[0] = r1;
        pick2[1] = r2;
        return pick2;
    }

    //return the index that has smaller fitness eval
	private int compete(ArrayList<NumberSolution<Double>> newLeaders, int[] pick2) {
        int r1 = pick2[0];
        int r2 = pick2[1];
        if (task.problem.isFirstBetter(newLeaders.get(r1), newLeaders.get(r2)))
            return r1;
        return r2;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
