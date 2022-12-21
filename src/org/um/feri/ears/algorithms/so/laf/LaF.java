package org.um.feri.ears.algorithms.so.laf;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.comparator.TaskComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;

public class LaF extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private boolean debug = true;
    private Task task;

    private ArrayList<NumberSolution<Double>> leaders;
    private ArrayList<NumberSolution<Double>> followers;


    private double[] ub;
    private double[] lb;
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
    public NumberSolution<Double> execute(Task task) throws StopCriterionException { //EARS main evaluation loop
        this.task = task;
        NumberSolution<Double> best = null;
        dimension = this.task.getNumberOfDimensions();
        ub = this.task.getUpperLimit();
        lb = this.task.getLowerLimit();

        initPopulation();
        int leaderIndex;
        int followerIndex;
        while (!this.task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                if (this.task.isStopCriterion())
                    break;
                leaderIndex = Util.nextInt(popSize);
                NumberSolution<Double> leader = leaders.get(leaderIndex);
                followerIndex = Util.nextInt(popSize);
                NumberSolution<Double> follower = followers.get(followerIndex);
                NumberSolution<Double> trial = trial(leader, follower); //one fit eval here
                //System.out.println(trailCost);
                if (this.task.isFirstBetter(trial, followers.get(followerIndex))) //eval done earlier
                    followers.set(followerIndex, trial);
            }
            if (this.task.isFirstBetter(findMedianSolution(followers), findMedianSolution(leaders))) {
                leaders = merge(followers, leaders);
            }
            best = new NumberSolution<>(leaders.get(findMinIndex(leaders)));
            this.task.incrementNumberOfIterations();
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
            if (farthest[i] < lb[i]) {
                maxSteps[i] = (lb[i] - follower.getValue(i)) / (ub[i] - lb[i]);
            }
            if (farthest[i] > ub[i]) {
                maxSteps[i] = (ub[i] - follower.getValue(i)) / (ub[i] - lb[i]);
            }
            double rand = Util.nextDouble();
            result[i] = follower.getValue(i) + maxSteps[i] * rand * gap[i];
        }

        result = task.setFeasible(result); //fixes upper and lower bound
        return task.eval(result);
    }

    private NumberSolution<Double> findMedianSolution(ArrayList<NumberSolution<Double>> mArrayList) {
        mArrayList.sort(new TaskComparator(task));
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
            if (task.isFirstBetter(newLeaders.get(i), newLeaders.get(minIndex))) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    //return an array w/ two int that are not selected before
	private int[] randomSample(boolean[] selected) {
        int[] pick2 = {0, 0};
        int r1 = Util.nextInt(selected.length);
        while (selected[r1]) {
            r1 = Util.nextInt(selected.length);
        }

        int r2 = Util.nextInt(selected.length);
        while (selected[r2]) {
            r2 = Util.nextInt(selected.length);
        }
        pick2[0] = r1;
        pick2[1] = r2;
        return pick2;
    }

    //return the index that has smaller fitness eval
	private int compete(ArrayList<NumberSolution<Double>> newLeaders, int[] pick2) {
        int r1 = pick2[0];
        int r2 = pick2[1];
        if (task.isFirstBetter(newLeaders.get(r1), newLeaders.get(r2)))
            return r1;
        return r2;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
