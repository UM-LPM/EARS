/*
 * original code provided by Robin Roche
 */
package org.um.feri.ears.algorithms.so.ica;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;


public class ICA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(name = "number of initial imperialists")
    private int numOfInitialImperialists = 6;
    private int numOfAllColonies = popSize - numOfInitialImperialists;
    @AlgorithmParameter(name = "revolution rate", description = "Revolution is the process in which the socio-political characteristics of a country change suddenly")
    private double revolutionRate = 0.1;
    @AlgorithmParameter(name = "assimilation coefficient", description = "In the original paper assimilation coefficient is shown by \"beta\" MATLAB -> 1.5")
    private double assimilationCoefficient = 2;
    @AlgorithmParameter(name = "assimilation angle coefficient", description = "In the original paper assimilation angle coefficient is shown by \"gama\"")
    private double assimilationAngleCoefficient = 0.5;
    @AlgorithmParameter(description = "Total Cost of Empire = Cost of Imperialist + Zeta * mean(Cost of All Colonies)")
    private double zeta = 0.02;
    @AlgorithmParameter(name = "damp ratio")
    private double dampRatio = 0.99;
    @AlgorithmParameter(name = "one empire", description = "Set to true to stop the algorithm when just one empire is remaining. Set to false to continue the algorithm")
    private boolean stopIfJustOneEmpire = false;
    @AlgorithmParameter(name = "uniting threshold", description = "The percent of search space size, which enables the uniting process of two empires")
    private double unitingThreshold = 0.02;

    private double[] searchSpaceSize;                    // The search space size (between the min and max bounds)
    private ICAUtils utils = new ICAUtils();            // A class with useful methods for array operations
    private NumberSolution<Double> best;
    private EmpireSolution[] empiresList;
    private NumberSolution<Double>[] initialCountries;
    //ArrayList<NumberSolution<Double>> offspringPopulation;

    public ICA() {
        this(50, 6, 0.1, 2, 0.5, 0.02, 0.99, false, 0.02);
    }

    public ICA(int popSize, int numOfInitialImperialists, double revolutionRate, double assimilationCoefficient, double assimilationAngleCoefficient, double zeta, double dampRatio, boolean stopIfJustOneEmpire, double unitingThreshold) {
        super();
        this.popSize = popSize;
        this.numOfInitialImperialists = numOfInitialImperialists;
        this.revolutionRate = revolutionRate;
        this.assimilationCoefficient = assimilationCoefficient;
        this.assimilationAngleCoefficient = assimilationAngleCoefficient;
        this.zeta = zeta;
        this.dampRatio = dampRatio;
        this.stopIfJustOneEmpire = stopIfJustOneEmpire;
        this.unitingThreshold = unitingThreshold;

        numOfAllColonies = popSize - numOfInitialImperialists;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("ICA", "Imperialist Competitive Algorithm",
                "@article{@inproceedings{atashpaz2007imperialist,"
                        + "title={Imperialist competitive algorithm: an algorithm for optimization inspired by imperialistic competition},"
                        + "author={Atashpaz-Gargari, Esmaeil and Lucas, Caro},"
                        + "booktitle={Evolutionary computation, 2007. CEC 2007. IEEE Congress on},"
                        + "pages={4661--4667},"
                        + "year={2007},"
                        + "organization={IEEE}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        searchSpaceSize = new double[task.problem.getNumberOfDimensions()];

        // Compute the problem search space, between the min and max bounds
        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            searchSpaceSize[i] = task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i);
        }

        initPopulation();

        // Create the initial empires
        createInitialEmpires();

        while (!task.isStopCriterion()) {

            // Update the revolution rate
            revolutionRate = dampRatio * revolutionRate;

            for (EmpireSolution empire : empiresList) {
                // Assimilation: movement of colonies toward their imperialist
                assimilateColonies(empire);

                // Revolution: change the position of some colonies, to avoid getting stuck into local minima
                revolution(empire);

                // Empire possession: a strong colony can become the imperialist
                possesEmpire(empire);

                // Update the empire's total cost
                updateTotalCost(empire);
            }

            // Uniting Similiar Empires
            uniteSimilarEmpires();

            // Imperialistic Competition
            imperialisticCompetition();

            // If the user wants it, the algorithm can stop when only one empire remains
            if (empiresList.length == 1 && stopIfJustOneEmpire) {
                break;
            }

            task.incrementNumberOfIterations();
        }
        return best;
    }

    /**
     * Runs the competition between empires
     */
    private void imperialisticCompetition() {

        //TODO not in original
        if (RNG.nextDouble() > .11) {
            return;
        }

        if (empiresList.length <= 1) {
            return;
        }

        // Get the total cost of each empire
        double[] totalCosts = new double[empiresList.length];
        for (int i = 0; i < empiresList.length; i++) {
            totalCosts[i] = empiresList[i].totalCost;
        }
        //utils.printArray("totalCosts", totalCosts);

        // Get the weakest empire (the one with the highest cost) and its cost
        int weakestEmpireInd = utils.getMaxIndex(totalCosts);
        double maxTotalCost = totalCosts[weakestEmpireInd];

        // Get the power of each empire
        double[] totalPowers = new double[empiresList.length];
        for (int i = 0; i < empiresList.length; i++) {
            totalPowers[i] = maxTotalCost - totalCosts[i];
        }
        //utils.printArray("totalPowers", totalPowers);

        // Get the possession probability of each empire
        double[] possessionProbability = new double[empiresList.length];
        for (int i = 0; i < empiresList.length; i++) {
            possessionProbability[i] = totalPowers[i] / utils.getSum(totalPowers);
        }
        //utils.printArray("possessionProbability", possessionProbability);

        // Select an empire according to their probabilities
        int selectedEmpireInd = selectAnEmpire(possessionProbability);
        //System.out.println("selectedind: " + selectedEmpireInd);

        // Generate a random integer
        int numOfColoniesOfWeakestEmpire = empiresList[weakestEmpireInd].colonies.length;
        int indexOfSelectedColony = RNG.nextInt(numOfColoniesOfWeakestEmpire);

        // Update the positions of the colonies of the selected empire
        // by adding the position of the randomly selected colony of the weakest empire
        empiresList[selectedEmpireInd].colonies = concatenateColonies(empiresList[selectedEmpireInd].colonies, empiresList[weakestEmpireInd].colonies[indexOfSelectedColony]);


        // Update the positions of the colonies of the weakest empire
        // by removing the position of the randomly selected colony of the empire
        empiresList[weakestEmpireInd].colonies = removeColony(empiresList[weakestEmpireInd].colonies, indexOfSelectedColony);

        // Get the number of colonies of the weakest empire
        numOfColoniesOfWeakestEmpire = empiresList[weakestEmpireInd].colonies.length;

        // If it has not more less 1 colony, then make it disapear/collapse
        // It is then absorbed by the selected empire
        if (numOfColoniesOfWeakestEmpire < 1) {
            // Update the positions of the colonies by adding the collapsed imperialist
            empiresList[selectedEmpireInd].colonies = concatenateColonies(empiresList[selectedEmpireInd].colonies, empiresList[weakestEmpireInd].imperialist);

            // Erase the collapsed empire from the empires list
            empiresList = ArrayUtils.removeElement(empiresList, empiresList[weakestEmpireInd]);
            //System.out.println("An empire has collapsed");
            //System.out.println("New number of empires: " + empiresList.length);
        }
    }

    private NumberSolution<Double>[] removeColony(NumberSolution<Double>[] colonies, int indexOfSelectedColony) {

        colonies = ArrayUtils.removeElement(colonies, colonies[indexOfSelectedColony]);

        return colonies;
    }

    private NumberSolution<Double>[] concatenateColonies(NumberSolution<Double>[] colonies, NumberSolution<Double> colony) {

        NumberSolution<Double>[] conColonies = new NumberSolution[colonies.length + 1];
        System.arraycopy(colonies, 0, conColonies, 0, colonies.length);
        conColonies[colonies.length] = colony;
        return conColonies;
    }

    /**
     * Selects an empire according to their probabilities
     *
     * @param probability the probability vector
     * @return the selected empire index
     */
    private int selectAnEmpire(double[] probability) {
        // Create a vector of random numbers
        double[] randVector = new double[probability.length];
        for (int i = 0; i < probability.length; i++) {
            randVector[i] = RNG.nextDouble();
        }

        // Substract to each element of this vector the corresponding
        // value of the probability vector
        double[] dVector = new double[probability.length];
        for (int i = 0; i < probability.length; i++) {
            dVector[i] = probability[i] - randVector[i];
        }

        // Return the index of the maximum value of the vector
        return utils.getMaxIndex(dVector);
    }

    /**
     * Unites imperialists that are close to each other
     */
    private void uniteSimilarEmpires() {
        // Get the threshold distance between two empires
        double thresholdDistance = unitingThreshold * utils.getNorm(searchSpaceSize);

        // Get the number of empires
        int numOfEmpires = empiresList.length;

        // Compare each empire with the other ones
        for (int i = 0; i < (numOfEmpires - 1); i++) {
            for (int j = i + 1; j < numOfEmpires; j++) {
                // Compute the distance between the two empires i and j
                double[] distanceVector = new double[task.problem.getNumberOfDimensions()];
                for (int k = 0; k < task.problem.getNumberOfDimensions(); k++) {
                    distanceVector[k] = empiresList[i].imperialist.getValue(k) - empiresList[j].imperialist.getValue(k);
                }
                double distance = utils.getNorm(distanceVector);

                // If the empires are too close
                if (distance <= thresholdDistance) {
                    // Get the best and worst empires of the two
                    int betterEmpireInd;
                    int worseEmpireInd;
                    if (task.problem.isFirstBetter(empiresList[i].imperialist, empiresList[j].imperialist)) {
                        betterEmpireInd = i;
                        worseEmpireInd = j;
                    } else {
                        betterEmpireInd = j;
                        worseEmpireInd = i;
                    }

                    // Update the positions
                    uniteEmpires(betterEmpireInd, worseEmpireInd);

                    // Update the total cost of the united empire
                    updateTotalCost(empiresList[betterEmpireInd]);

                    // Update the empires list
                    empiresList = ArrayUtils.removeElement(empiresList, empiresList[worseEmpireInd]);
                    //deleteAnEmpire(worseEmpireInd);
                    //System.out.println("New number of empires: " + empiresList.length);

                    return;
                }

            }
        }
    }

    private void uniteEmpires(int betterEmpireInd, int worseEmpireInd) {

        int newSize = empiresList[betterEmpireInd].colonies.length + 1 + empiresList[worseEmpireInd].colonies.length;

        NumberSolution<Double>[] unitedColonies = new NumberSolution[newSize];

        System.arraycopy(empiresList[betterEmpireInd].colonies, 0, unitedColonies, 0, empiresList[betterEmpireInd].colonies.length);
        unitedColonies[empiresList[betterEmpireInd].colonies.length] = empiresList[worseEmpireInd].imperialist;
        System.arraycopy(empiresList[worseEmpireInd].colonies, 0, unitedColonies, empiresList[betterEmpireInd].colonies.length + 1, empiresList[worseEmpireInd].colonies.length);

        empiresList[betterEmpireInd].colonies = unitedColonies;

    }

    /**
     * Make colonies of an empire do a revolution.
     * This is equivalent to a perturbation which avoids getting stuck
     * into local minima.
     *
     * @param empire to revolve
     */
    private void revolution(EmpireSolution empire) throws StopCriterionException {

        // Get the number of colonies to revolve
        int numOfRevolvingColonies = (int) Math.round((revolutionRate * empire.colonies.length));

        int[] R = RNG.randomPermutation(empire.colonies.length);

        for (int i = 0; i < R.length; i++) {

            if (task.isStopCriterion())
                break;

            if (i < numOfRevolvingColonies) {
                empire.colonies[R[i]] = task.getRandomEvaluatedSolution();
            } else {
                task.eval(empire.colonies[R[i]]);
            }

            if (task.problem.isFirstBetter(empire.colonies[R[i]], best)) {
                best = new NumberSolution(empire.colonies[R[i]]);
            }

        }
    }

    /**
     * Can make a colony become the imperialist
     * if it is more powerful than the imperialist.
     *
     * @param empire empire to posses
     */
    private void possesEmpire(EmpireSolution empire) {
        // Get the cost of the best colony (the lowest cost)
        int bestColonyId = utils.getMinIndex(empire.colonies);

        // If this cost is lower than the one of the imperialist
        if (task.problem.isFirstBetter(empire.colonies[bestColonyId], empire.imperialist)) {
            NumberSolution<Double> temp = empire.imperialist;
            empire.imperialist = empire.colonies[bestColonyId];
            empire.colonies[bestColonyId] = temp;
        }
    }

    /**
     * Assimilates the colonies of an empire: move their positions
     *
     * @param empire assimilate colonies for this empire
     */
    private void assimilateColonies(EmpireSolution empire) {

        // Get the number of colonies of the empire
        int numOfColonies = empire.colonies.length;


        for (int i = 0; i < numOfColonies; i++) {
            ArrayList<Double> newColony = new ArrayList<>();
            for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                //TODO 2 * beta -> matlab samo beta = 1.5
                newColony.add(empire.colonies[i].getValue(j) + 2 * assimilationCoefficient * RNG.nextDouble() * (empire.imperialist.getValue(j) - empire.colonies[i].getValue(j)));
                newColony.set(j, task.problem.setFeasible(newColony.get(j), j));
            }
            empire.colonies[i].setVariables(newColony);
        }
    }

    /**
     * Generates the initial empires
     */
    private void createInitialEmpires() throws StopCriterionException {
        empiresList = new EmpireSolution[numOfInitialImperialists];

        // Extract the best countries to create empires
        NumberSolution<Double>[] allImperialists = Arrays.copyOfRange(initialCountries, 0, numOfInitialImperialists);

        // Extract the rest to create colonies
        NumberSolution<Double>[] allColonies = Arrays.copyOfRange(initialCountries, numOfInitialImperialists, initialCountries.length);

        // Compute the power of imperialists
        double[] allImperialistsPower = new double[numOfInitialImperialists];
        double max = utils.getMax(allImperialists).getEval();
        if (max > 0) {
            for (int i = 0; i < allImperialists.length; i++) {
                allImperialistsPower[i] = 1.3 * max - allImperialists[i].getEval();
            }
        } else {
            for (int i = 0; i < allImperialists.length; i++) {
                allImperialistsPower[i] = 0.7 * max - allImperialists[i].getEval();
            }
        }

        // Set the number of colonies for the imperialists
        int[] allImperialistNumOfColonies = new int[numOfInitialImperialists];
        for (int i = 0; i < allImperialistsPower.length; i++) {
            allImperialistNumOfColonies[i] = (int) Math.round(allImperialistsPower[i] / utils.getSum(allImperialistsPower) * numOfAllColonies);
        }
        allImperialistNumOfColonies[allImperialistNumOfColonies.length - 1] =
                Math.max(numOfAllColonies - utils.getSum(Arrays.copyOfRange(allImperialistNumOfColonies, 0, allImperialistNumOfColonies.length - 1)), 0);
        //utils.printArray("allImperialistNumOfColonies", allImperialistNumOfColonies);

        // Initialize the empires
		/*for(int i=0; i<numOfInitialImperialists; i++)
		{
			empiresList[i] = new Empire(problemDimension);
		}*/

        // Create a random permutation of integers
        int[] randomIndex = RNG.randomPermutation(numOfAllColonies);
        int index = 0;
        // Create the empires and attribute them their colonies
        for (int i = 0; i < numOfInitialImperialists; i++) {
            NumberSolution<Double>[] colonies = new NumberSolution[allImperialistNumOfColonies[i]];

            for (int j = 0; j < allImperialistNumOfColonies[i]; j++) {
                colonies[j] = allColonies[randomIndex[index]];
                index++;
            }

            empiresList[i] = new EmpireSolution();
            empiresList[i].imperialist = allImperialists[i];
            empiresList[i].colonies = colonies;
            updateTotalCost(empiresList[i]);
        }

        // If an empire has no colony, give it one
        for (EmpireSolution empire : empiresList) {
            if (empire.colonies.length == 0) {
                empire.colonies = new NumberSolution[]{task.getRandomEvaluatedSolution()};
            }
        }

    }

    private void updateTotalCost(EmpireSolution empire) {

        empire.totalCost = empire.imperialist.getEval() + zeta * utils.getMean(empire.colonies);
    }

    private void initPopulation() throws StopCriterionException {
        initialCountries = new NumberSolution[popSize];

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> newSolution = task.getRandomEvaluatedSolution();
            initialCountries[i] = newSolution;
        }
        Arrays.sort(initialCountries, new ProblemComparator<>(task.problem));
        //initialCountries.sort(new ProblemComparator(task.problem));
        best = new NumberSolution<>(initialCountries[0]);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
