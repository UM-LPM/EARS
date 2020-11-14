package org.um.feri.ears.algorithms.so.abc;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class ABC extends Algorithm {

    int cs; // The number of colony size (employed bees + onlooker bees)
    int foodNumber;
    Task task;
    ABCSolution best;
    int limit;
    ArrayList<ABCSolution> population;

    public ArrayList<ABCSolution> getPopulaton() {
        return population;
    }

    public ArrayList getPopulaton2() {
        return population;
    }

    public ABC() {
        this(60);
    }

    public ABC(int cs) {
        super();
        this.cs = cs;
        this.foodNumber = cs / 2;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("ABC",
                "@article{karaboga2007powerful,"
                        + "title={A powerful and efficient algorithm for numerical function optimization: artificial bee colony (ABC) algorithm},"
                        + "author={Karaboga, Dervis and Basturk, Bahriye},"
                        + "journal={Journal of global optimization},"
                        + "volume={39},"
                        + "number={3},"
                        + "pages={459--471},"
                        + "year={2007},"
                        + "publisher={Springer}}",
                "ABC", "Artificial Bee Colony");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, cs + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        task = taskProblem;
        limit = (cs * task.getNumberOfDimensions()) / 2;
        initPopulation();

        while (!task.isStopCriteria()) {

            sendEmployedBees();
            calculateProbabilities();
            sendOnlookerBees();
            memorizeBestSource();
            sendScoutBees();

            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void sendScoutBees() throws StopCriteriaException {

        for (int i = 0; i < foodNumber; i++) {
            if (population.get(i).trials >= limit) {
                if (task.isStopCriteria())
                    return;
                ABCSolution newBee = new ABCSolution(task.getRandomSolution());
                population.set(i, newBee);
            }
        }

    }

    private void memorizeBestSource() {

        for (ABCSolution bee : population) {
            if (task.isFirstBetter(bee, best)) {
                best = new ABCSolution(bee);
            }
        }

    }

    private void sendOnlookerBees() throws StopCriteriaException {

        int neighbour, param2change;
        double phi, newValue;
        int t = 0, i = 0, r;

        while (t < foodNumber) {
            r = Util.nextInt(foodNumber);
            if (Util.nextDouble() < population.get(i).getProb()) {
                t++;

                //A randomly chosen solution is used in producing a mutant solution of the solution i
                neighbour = Util.nextInt(foodNumber);
                while (neighbour == i) //Randomly selected solution must be different from the solution i
                    neighbour = Util.nextInt(foodNumber);

                //The parameter to be changed is determined randomly
                param2change = Util.nextInt(task.getNumberOfDimensions());

                phi = Util.nextDouble(-1, 1);

                newValue = population.get(i).getValue(param2change) + (population.get(i).getValue(param2change) - population.get(neighbour).getValue(param2change)) * (phi - 0.5) * 2;
                newValue = task.setFeasible(newValue, param2change);

                ABCSolution newBee = new ABCSolution(population.get(i));
                newBee.setValue(param2change, newValue);

                if (task.isStopCriteria())
                    return;

                newBee = new ABCSolution(task.eval(newBee));

                if (newBee.getABCEval() > population.get(i).getABCEval()) {
                    newBee.trials = 0;
                    population.set(i, newBee);
                } else {
                    population.get(i).trials++;
                }

            }
            i++;

            if (i == foodNumber)
                i = 0;
        }

    }

    private void calculateProbabilities() {
        double maxFit;
        maxFit = population.get(0).getABCEval();

        for (int i = 1; i < foodNumber; i++) {

            if (maxFit < population.get(i).getABCEval()) {
                maxFit = population.get(i).getABCEval();
            }
        }

        for (ABCSolution bee : population) {
            bee.setProb((0.9 * (bee.getABCEval() / maxFit)) + 0.1);
        }

    }

    private void sendEmployedBees() throws StopCriteriaException {
        int neighbour, param2change;
        double phi, newValue;

        for (int i = 0; i < foodNumber; i++) {

            //A randomly chosen solution is used in producing a mutant solution of the solution i
            neighbour = Util.nextInt(foodNumber);
            while (neighbour == i) //Randomly selected solution must be different from the solution i
                neighbour = Util.nextInt(foodNumber);

            //The parameter to be changed is determined randomly
            param2change = Util.nextInt(task.getNumberOfDimensions());

            phi = Util.nextDouble(-1, 1);

            //TODO pomno�i samo z phi
            newValue = population.get(i).getValue(param2change) + (population.get(i).getValue(param2change) - population.get(neighbour).getValue(param2change)) * (phi - 0.5) * 2;
            newValue = task.setFeasible(newValue, param2change);

            ABCSolution newBee = new ABCSolution(population.get(i));
            newBee.setValue(param2change, newValue);

            if (task.isStopCriteria())
                return;

            newBee = new ABCSolution(task.eval(newBee));


            if (newBee.getABCEval() > population.get(i).getABCEval()) {
                newBee.trials = 0;
                population.set(i, newBee);
            } else {
                population.get(i).trials++;
            }
        }
    }

    private void initPopulation() throws StopCriteriaException {
        population = new ArrayList<ABCSolution>();
        ABCSolution bee = new ABCSolution(task.getRandomSolution());
        population.add(bee);
        best = new ABCSolution(bee);
        for (int i = 0; i < foodNumber - 1; i++) {
            ABCSolution newBee = new ABCSolution(task.getRandomSolution());
            population.add(newBee);
            if (task.isFirstBetter(newBee, best))
                best = new ABCSolution(newBee);
            if (task.isStopCriteria())
                break;
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
