/*
 * Copyright (c) 2011 Murilo Rebelo Pontes
 * murilo.pontes@gmail.com
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */
package org.um.feri.ears.algorithms.so.fss;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.qualityIndicator.QualityIndicatorUtil;
import org.um.feri.ears.util.Comparator.TaskComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;

public class FSS extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private DoubleSolution best;
    private Task task;

    //Parameters
    public static final double FISH_WEIGHT_MIN = 1;
    public static final double FISH_WEIGHT_MAX = 5000;
    private static final double STEP_INDIVIDUAL_INIT = 1.0;
    private static final double STEP_INDIVIDUAL_FINAL = 0.00001;
    private static final double STEP_VOLATILE_INIT = 1.0;
    private static final double STEP_VOLATILE_FINAL = 0.00001;


    private ArrayList<FishSolution> school;

    public FSS() {
        this(100);
    }

    public FSS(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("FSS", "Fish School Search",
                "@inproceedings{bastos2008novel,"
                        + "title={A novel search algorithm based on fish school behavior},"
                        + "author={Bastos Filho, Carmelo JA and de Lima Neto, Fernando B and Lins, Anthony JCC and Nascimento, Antonio IS and Lima, Marilia P},"
                        + "booktitle={Systems, Man and Cybernetics, 2008. SMC 2008. IEEE International Conference on},"
                        + "pages={2646--2651},"
                        + "year={2008},"
                        + "organization={IEEE}}"
        );
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        while (!this.task.isStopCriterion()) {
            double stepIndividual = 1.0;

            //TODO iterations and cpu time
            if (this.task.getStopCriterion() == EnumStopCriterion.EVALUATIONS) {

                stepIndividual = FSS.STEP_INDIVIDUAL_INIT - (FSS.STEP_INDIVIDUAL_INIT - FSS.STEP_INDIVIDUAL_FINAL) * ((double) this.task.getNumberOfEvaluations() / (double) this.task.getMaxEvaluations());
            }

            individualOperator(stepIndividual);

            feedingOperator();

            double[] schoolInstinctive = colletiveInstinctiveOperator();

            //TODO iterations and cpu time
            double stepVolatile = FSS.STEP_VOLATILE_INIT - (FSS.STEP_VOLATILE_INIT - FSS.STEP_VOLATILE_FINAL) * ((double) this.task.getNumberOfEvaluations() / (double) this.task.getMaxEvaluations());

            individualOperator(stepIndividual);

            collectivesVolatileOperator(stepVolatile * (this.task.getUpperLimit(0) - this.task.getLowerLimit(0)), schoolInstinctive);

            this.task.incrementNumberOfIterations();
        }
        return best;
    }

    private int collectivesVolatileOperator(double step_size, double[] school_instinctive) throws StopCriterionException {

        double[] schoolBarycentre = new double[task.getNumberOfDimensions()];
        double[] sumProd = new double[task.getNumberOfDimensions()];
        double sumWeightNow = 0;
        double sumWeightPast = 0;

        //clear
        for (int i = 0; i < sumProd.length; i++) {
            sumProd[i] = 0;
            schoolBarycentre[i] = 0;
        }

        //for each fish contribute with your neighbor position and weight
        for (FishSolution fish : school) {
            for (int i = 0; i < fish.deltaX.length; i++) {
                sumProd[i] += fish.neighbor.getValue(i) * fish.weightNow;
            }
            //sum weight
            sumWeightNow += fish.weightNow;
            sumWeightPast += fish.weightPast;
        }
        //calculate barycentre
        for (int i = 0; i < sumProd.length; i++) {
            schoolBarycentre[i] = sumProd[i] / sumWeightNow;
        }

        double direction = 0;
        if (sumWeightNow > sumWeightPast) {
            //weight gain -> contract
            direction = 1;
        } else {
            //weight loss -> dilate
            direction = -1;
        }

        int countSuccess = 0;
        for (FishSolution fish : school) {

            double[] newSolution = fish.neighbor.getDoubleVariables();

            double de = 0.0;

            try {
                de = QualityIndicatorUtil.distance(newSolution, schoolBarycentre);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //take care about zero division
            if (de != 0) {

                for (int i = 0; i < task.getNumberOfDimensions(); i++) {

                    //continue to update neighbor with dilate/shrink
                    newSolution[i] += (step_size * direction * Util.nextDouble() * (newSolution[i] - schoolBarycentre[i])) / de;
                }

                //take care about bounds of search space
                newSolution = task.setFeasible(newSolution);

                //evaluate new current solution
                if (task.isStopCriterion())
                    return countSuccess;

                fish.neighbor = task.eval(newSolution);

                //update current if neighbor is best
                fish.volatileMoveSuccess = false;
                if (task.isFirstBetter(fish.neighbor, fish.current)) {
                    fish.current = new DoubleSolution(fish.neighbor);
                    fish.volatileMoveSuccess = true;
                    countSuccess++;
                }

                //if need replace best solution
                if (task.isFirstBetter(fish.current, fish.best)) {
                    fish.best = new DoubleSolution(fish.current);
                }

                if (task.isFirstBetter(fish.best, best)) {
                    best = new DoubleSolution(fish.best);
                }

            } else {
                //warning user
                System.err.println("bypass volatile operator (zero division)");
            }
        }

        return countSuccess;
    }

    private double[] colletiveInstinctiveOperator() {

        double[] school_instinctive = calculateInstinctiveDirection();

        for (FishSolution fish : school) {
            //use current as template to neighbor
            fish.neighbor = new DoubleSolution(fish.current);

            //update neighbor with instinctive direction
            for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                fish.neighbor.setValue(i, fish.neighbor.getValue(i) + school_instinctive[i]);
            }
        }

        return school_instinctive;
    }

    private double[] calculateInstinctiveDirection() {

        double[] schoolInstinctive = new double[task.getNumberOfDimensions()];
        double[] sumProd = new double[task.getNumberOfDimensions()];
        double sumFitnessGain = 0;


        //for each fish contribute with your direction scaled by your fitness gain
        for (FishSolution fish : school) {
            //only good fishes
            if (fish.individualMoveSuccess) {
                //sum product of solution by fitness gain
                for (int i = 0; i < fish.deltaX.length; i++) {
                    sumProd[i] += fish.deltaX[i] * fish.fitnessGainNormalized;
                }
                //sum fitness gains
                sumFitnessGain += fish.fitnessGainNormalized;
            }
        }

        //calculate global direction of good fishes
        for (int i = 0; i < sumProd.length; i++) {
            //take care about zero division
            if (sumFitnessGain != 0) {
                schoolInstinctive[i] = sumProd[i] / sumFitnessGain;
            } else {
                schoolInstinctive[i] = 0;
            }
        }
        return schoolInstinctive;
    }

    private void feedingOperator() {

        //sort school by fitness gain
        school.sort(new FSSComparatorByFitnessGain());

        //max absolute value of fitness gain
        double absDeltaFMax = Math.abs(school.get(0).delta_f);
        double absDeltaFMax2 = Math.abs(school.get(school.size() - 1).delta_f);
        if (absDeltaFMax2 > absDeltaFMax) absDeltaFMax = absDeltaFMax2;

        //take care about zero division
        if (absDeltaFMax != 0) {
            //calculate normalized gain
            for (FishSolution fish : school) {
                fish.fitnessGainNormalized = fish.delta_f / absDeltaFMax;
            }

            //feed all fishes
            for (FishSolution fish : school) {
                //
                fish.weightPast = fish.weightNow;
                fish.weightNow += fish.fitnessGainNormalized;
                //take care about min and max weight
                if (fish.weightNow < FSS.FISH_WEIGHT_MIN) fish.weightNow = FSS.FISH_WEIGHT_MIN;
                if (fish.weightNow > FSS.FISH_WEIGHT_MAX) fish.weightNow = FSS.FISH_WEIGHT_MAX;
            }
        } else {
            //warning user
            System.err.println("bypass feeding (zero division)");
        }
    }

    private int individualOperator(double step_size) throws StopCriterionException {

        int countSuccess = 0;
        for (FishSolution fish : school) {
            //use current as template for neighbor
            fish.neighbor = new DoubleSolution(fish.current);

            double[] newSolution = fish.neighbor.getDoubleVariables();

            for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                //calculate displacement
                fish.deltaX[i] = Util.nextDouble(-1, 1) * step_size;
                //generate new solution in neighbor
                newSolution[i] += fish.deltaX[i];
            }

            //take care about bounds of search space
            newSolution = task.setFeasible(newSolution);

            //evaluate new current solution
            if (task.isStopCriterion())
                return countSuccess;


            fish.neighbor = task.eval(newSolution);

            //calculate fitness difference
            fish.delta_f = fish.neighbor.getEval() - fish.current.getEval();

            //update current if neighbor is best
            fish.individualMoveSuccess = false;
            if (task.isFirstBetter(fish.neighbor, fish.current)) {
                fish.current = new DoubleSolution(fish.neighbor);
                fish.individualMoveSuccess = true;
                countSuccess++;
            }

            //if need replace best solution
            if (task.isFirstBetter(fish.current, fish.best)) {
                fish.best = new DoubleSolution(fish.current);
            }

            if (task.isFirstBetter(fish.best, best)) {
                best = new DoubleSolution(fish.best);
            }

        }
        return countSuccess;
    }

    private void initPopulation() throws StopCriterionException {
        school = new ArrayList<FishSolution>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            FishSolution newSolution = new FishSolution(task.getRandomEvaluatedSolution());
            school.add(newSolution);
        }

        school.sort(new TaskComparator(task));
        best = new DoubleSolution(school.get(0).current);
		
		/*StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for(int k = 0; k < pop_size; k++)
		{
			
			double[] var = population.get(k).getDoubleVariables();
			for (int i = 0; i < var.length; i++) {
				sb.append(var[i]);
				if(i+1 < var.length)
					sb.append("\t");
			}
			if(k+1 < pop_size)
				sb.append(";");

		}
		sb.append("]");
		System.out.println(sb.toString());*/
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
