package org.um.feri.ears.algorithms.so.jso;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class jSO extends NumberAlgorithm{
    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(name = "archive size")
    private int arcSize;
    @AlgorithmParameter(name = "archive rate", min = "1.0", max = "3.0")
    private double arcRate;
    @AlgorithmParameter(name = "current best weighted mutation", description = "value for current-to-pbest-w/1 mutation", min = "0.05", max = "0.5")
    private double pBestRateMax;
    @AlgorithmParameter(name = "historical memory size", description = "H", min = "2", max = "10")
    private int memorySize;

    private int reductionIndNum;

    private ArrayList<NumberSolution<Double>> population;
    private ArrayList<NumberSolution<Double>> offspringPopulation;
    private NumberSolution<Double> best;

    private List<Double> lowerLimit;
    private List<Double> upperLimit;

    boolean initPopSizeSet = false;
    int initialPopSize;

    public jSO(int initialPopSize) {
        this();
        this.initialPopSize = initialPopSize;
        initPopSizeSet = true;
    }

    public jSO() {
        arcRate = 1.0; //L-SHADE = 2.6
        pBestRateMax = 0.25; //initial p value for current-to-pbest-w/1 mutation iL-SHADE=0.2
        memorySize = 5; //H historical memory size L-SHADE = 6

        au = new Author("aleš", "ales.gartner@student.um.si");
        ai = new AlgorithmInfo("jSO", "jSO",
                "@inproceedings{brest2017single,"
                        + "title={Single objective real-parameter optimization: Algorithm jSO},"
                        + "author={Brest, Janez and Maucec, Mirjam Sepesy and Boskovic, Borko},"
                        + "booktitle={2017 IEEE congress on evolutionary computation (CEC)},"
                        + "pages={1311--1318},"
                        + "year={2017},"
                        + "organization={IEEE}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        lowerLimit = task.problem.getLowerLimit();
        upperLimit = task.problem.getUpperLimit();

        if(initPopSizeSet) // if initial population size set
            popSize = initialPopSize;
        else
            popSize = (int) Math.round(25.0* Math.log(task.problem.getNumberOfDimensions())* Math.sqrt(task.problem.getNumberOfDimensions()));//jSO
        //pop_size = 25*log(D)*sqrt(D)

        arcSize = (int) Math.round(popSize * arcRate);

        initPopulation();

        //for external archive
        int arcIndCount = 0;
        int randomSelectedArcInd;
        ArrayList<NumberSolution<Double>> archive = new ArrayList<>();
        for (int i=0; i<arcSize; i++){
            archive.add(null);
        }

        int numSuccessParams = 0;
        //int oldNumSuccessParams = 0;
        List<Double> successSf = new ArrayList<>();
        List<Double> successCr = new ArrayList<>();
        List<Double> difFitness = new ArrayList<>();

        double[] memorySf = new double[memorySize];
        double[] memoryCr = new double[memorySize];

        Arrays.fill(memorySf, 0.3); // the contents of M_f are initialised to 0.3 iL-SHADE = 0.5
        Arrays.fill(memoryCr, 0.8); // the contents of M_cr are initialised to 0.8

        double tempSumSf;
        double tempSumCr;
        double sum;
        double weight;

        //memory index counter
        int memoryPos = 0;

        //for new parameters sampling
        double muSf;
        double muCr;
        int randomSelectedPeriod;
        double[] popSf = new double[popSize];
        double[] popCr = new double[popSize];

        //for current-to-pbest-m/1
        int pBestInd;
        double pBestRate = pBestRateMax;
        int pNum = (int) Math.round(popSize * pBestRate);
        int[] sortedArray = new int[popSize];

        // for linear population size reduction
        int maxPopSize = popSize;
        int minPopSize = 4;
        int planPopSize;

        double[] offspring = new double[task.problem.getNumberOfDimensions()];
        //main loop
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                sortedArray[i] = i;
            }
            sortedArray = getSortedArray();

            for (int target = 0; target < popSize; target++) {
                //In each generation, CR_i and F_i used by each individual x_i are generated by first selecting an index r_i randomly from [1, H]
                randomSelectedPeriod = RNG.nextInt(0, memorySize);

                if(randomSelectedPeriod == memorySize-1){//jSO
                    muSf=0.9;
                    muCr=0.9;
                }
                else {
                    muSf = memorySf[randomSelectedPeriod];
                    muCr = memoryCr[randomSelectedPeriod];
                }


                //generate CR_i and repair its value
                if(muCr < 0){//jSO
                    popCr[target] = 0.0;
                }
                else {
                    popCr[target] = RNG.nextGaussian(muCr, 0.1);
                    if (popCr[target] > 1) {
                        popCr[target] = 1;
                    } else if (popCr[target] < 0) {
                        popCr[target] = 0;
                    }
                }

                if(task.getNumberOfEvaluations() < 0.25*task.getMaxEvaluations() && popCr[target] < 0.7){//jSO
                    popCr[target] = 0.7;
                }
                if(task.getNumberOfEvaluations() < 0.50*task.getMaxEvaluations() && popCr[target] < 0.6){//jSO
                    popCr[target] = 0.6;
                }

                //generate F_i and repair its value
                do {
                    popSf[target] = RNG.nextCauchy(muSf, 0.1);
                } while (popSf[target] <= 0);

                if (popSf[target] > 1) {
                    popSf[target] = 1;
                }
                if(task.getNumberOfEvaluations() < 0.6*task.getMaxEvaluations() && popSf[target] > 0.7){//jSO
                    popSf[target] = 0.7;
                }

                //p-best individual is randomly selected from the top pop_size *  p_i members
                do {
                    pBestInd = sortedArray[RNG.nextInt(0, pNum)];
                }while (task.getNumberOfEvaluations() < 0.50*task.getMaxEvaluations() && pBestInd == target);//iL-SHADE

                int r1, r2;
                double crossRate = popCr[target];
                double scalingFactor = popSf[target];

                //weight for mutation
                double jF = scalingFactor;//jSO
                if (task.getNumberOfEvaluations() < 0.2*task.getMaxEvaluations()){
                    jF = jF*0.7;
                }else if(task.getNumberOfEvaluations() < 0.4*task.getMaxEvaluations()){
                    jF = jF*0.8;
                }else{
                    jF = jF*1.2;
                }

                do {
                    r1 = RNG.nextInt(0, popSize);
                } while (r1 == target);
                do {
                    r2 = RNG.nextInt(0, popSize + arcIndCount);
                } while ((r2 == target) || (r2 == r1));

                int random_variable = RNG.nextInt(0, task.problem.getNumberOfDimensions());

                NumberSolution<Double> solution;
                if (r2 >= popSize) {
                    r2 -= popSize;
                    solution = archive.get(r2);
                } else
                    solution = population.get(r2);

                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    if ((RNG.nextFloat() < crossRate) || (i == random_variable)) {
                        offspring[i] = population.get(target).getValue(i) + jF * (population.get(pBestInd).getValue(i) - population.get(target).getValue(i)) + scalingFactor * (population.get(r1).getValue(i) - solution.getValue(i));//jSO
                    } else {
                        offspring[i] = population.get(target).getValue(i);
                    }
                }

                //If the mutant vector violates bounds, the bound handling method is applied
                modifySolutionWithParentMedium(offspring, population.get(target));

                if (task.isStopCriterion())
                    break;
                // evaluate the children's fitness value
                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(offspring));
                task.eval(newSolution);
                offspringPopulation.set(target, newSolution);
                //update the best solution and check the current number of fitness evaluations
                if (task.problem.isFirstBetter(newSolution, best))
                    best = new NumberSolution<>(newSolution);
            }

            //generation alternation
            for (int i = 0; i < popSize; i++) {
                if (offspringPopulation.get(i).getEval() == population.get(i).getEval()) {
                    population.set(i, new NumberSolution<>(offspringPopulation.get(i)));

                } else if (task.problem.isFirstBetter(offspringPopulation.get(i), population.get(i))) {
                    difFitness.add(Math.abs(population.get(i).getEval() - offspringPopulation.get(i).getEval()));
                    //population[i] = offspringPopulation[i]; L-SHADE

                    //successful parameters are preserved in S_F and S_CR
                    successSf.add(popSf[i]);
                    successCr.add(popCr[i]);

                    //parent vectors x_i which were worse than the trial vectors u_i are preserved
                    if (arcSize > 1) {
                        if (arcIndCount < arcSize) {
                            archive.set(arcIndCount, new NumberSolution<>(population.get(i)));
                            arcIndCount++;
                        }
                        //Whenever the size of the archive exceeds, randomly selected elements are deleted to make space for the newly inserted elements
                        else {
                            randomSelectedArcInd = RNG.nextInt(0, arcSize);
                            //for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                            archive.set(randomSelectedArcInd, new NumberSolution<>(population.get(i)));
                            //}
                        }
                    }
                    population.set(i, new NumberSolution<>(offspringPopulation.get(i)));//jSO
                }
            }
            //oldNumSuccessParams = numSuccessParams;
            numSuccessParams = successSf.size();

            // if number of successful parameters > 0, historical memories are updated
            if (numSuccessParams > 0) {
                double old_sf = memorySf[memoryPos];//jSO
                double old_cr = memoryCr[memoryPos];//jSO

                memorySf[memoryPos] = 0;
                memoryCr[memoryPos] = 0;
                tempSumSf = 0;
                tempSumCr = 0;
                sum = 0;

                for (int i = 0; i < numSuccessParams; i++) {
                    sum += difFitness.get(i);
                }

                //weighted lehmer mean
                for (int i = 0; i < numSuccessParams; i++) {
                    weight = difFitness.get(i) / sum;

                    memorySf[memoryPos] = memorySf[memoryPos] + weight * successSf.get(i) * successSf.get(i);
                    tempSumSf += weight * successSf.get(i);

                    memoryCr[memoryPos] = memoryCr[memoryPos] + weight * successCr.get(i) * successCr.get(i);
                    tempSumCr += weight * successCr.get(i);
                }

                memorySf[memoryPos] = memorySf[memoryPos] / tempSumSf;

                if (tempSumCr == 0 || memoryCr[memoryPos] == -1) {
                    memoryCr[memoryPos] = -1;
                } else {
                    memoryCr[memoryPos] = memoryCr[memoryPos] / tempSumCr;
                }

                memorySf[memoryPos] = (memorySf[memoryPos] + old_sf)/2.0;//jSO
                memoryCr[memoryPos] = (memoryCr[memoryPos] + old_cr)/2.0;//jSO
                //increment the counter
                memoryPos++;
                if (memoryPos >= memorySize) {
                    memoryPos = 0;
                }

                //clear out the S_F, S_CR and delta fitness
                successSf.clear();
                successCr.clear();
                difFitness.clear();
            }

            // calculate the population size in the next generation
            planPopSize = (int) Math.round((((minPopSize - maxPopSize) / (double) task.getMaxEvaluations()) * task.getNumberOfEvaluations()) + maxPopSize);

            if (popSize > planPopSize) {
                reductionIndNum = popSize - planPopSize;
                if (popSize - reductionIndNum < minPopSize) {
                    reductionIndNum = popSize - minPopSize;
                }

                reducePopulationWithSort();

                // resize the archive size
                arcSize = (int) (popSize * arcRate);
                if (arcIndCount > arcSize) {
                    arcIndCount = arcSize;
                }

                // resize the number of p-best individuals
                pBestRate = pBestRateMax * (1.0 - 0.5 * task.getNumberOfEvaluations() / (double) task.getMaxEvaluations());//jSO
                pNum = (int) Math.round(popSize * pBestRate);
                if (pNum <= 1) {
                    pNum = 2;
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }


    private int[] getSortedArray() {
        int[] indices = new int[population.size()];
        for (int i = 0; i < indices.length; i++)
            indices[i] = i;

        for (int i = 0; i < indices.length; i++) {
            for (int j = i + 1; j < indices.length; j++) {
                if (task.problem.isFirstBetter(population.get(indices[j]), population.get(indices[i]))) {
                    int tmp = indices[i];
                    indices[i] = indices[j];
                    indices[j] = tmp;
                }
            }
        }
        return indices;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        offspringPopulation = new ArrayList<>();
        for (int i = 0; i < popSize; i++){
            population.add(null);
            offspringPopulation.add(null);
        }
        best = task.generateRandomEvaluatedSolution();
        population.set(0, new NumberSolution<>(best));
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.set(i, task.generateRandomEvaluatedSolution());
            if (task.problem.isFirstBetter(population.get(i), best)) {
                best = new NumberSolution<>(population.get(i));
            }
        }
    }

    private void reducePopulationWithSort() {
        int worst_ind;

        for (int i = 0; i < reductionIndNum; i++) {
            worst_ind = 0;
            for (int j = 1; j < popSize; j++) {
                if (task.problem.isFirstBetter(population.get(worst_ind), population.get(j))) {
                    worst_ind = j;
                }
            }
            population.remove(worst_ind);
            popSize--;
        }
    }

    private void modifySolutionWithParentMedium(double[] child, NumberSolution<Double> parent) {

        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            if (child[i] < lowerLimit.get(i)) {
                child[i] = (lowerLimit.get(i) + parent.getValue(i)) / 2.0;
            } else if (child[i] > upperLimit.get(i)) {
                child[i] = (upperLimit.get(i) + parent.getValue(i)) / 2.0;
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}