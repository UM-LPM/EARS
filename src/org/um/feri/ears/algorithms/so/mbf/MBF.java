package org.um.feri.ears.algorithms.so.mbf;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MBF extends Algorithm {

    private DoubleSolution best;

    private Task task;

    private double SP = 0.8;        //Mother Power
    private double SPdamp = 0.95;   //Mother Power Damping Ratio
    private double Dis = 1.8;         //Amount of Dispersion Coefficient
    private double Pdis = 0.2;      //Amount of Probability of Dispersion Coefficient
    private int popSize = 50;      //Population Size (Fish Numbers)
    private double ASDP; //Additional Surrounding Dispersion of Wild
    private double ASDN;                   //Additional Surrounding Dispersion of Mother
    private int nCrossover = 2;
    private int nShark;
    private int nm;
    private double beta = 8;        //Rouleteewheel Constant
    private double gamma = 0.09;    //Crossover Rate
    private int checkCONV = 0;
    private int checkpoint = 0;
    private int kk = 1;

    private int[][] effectOfSharkAttack;

    private ArrayList<CichlidsSolution> population;
    private CichlidsSolution[] NatureForce;
    private CichlidsSolution[] SharkAttack;
    private CichlidsSolution[] BestResult;

    public MBF() {
        this(50);
    }

    public MBF(int popSize) {
        super();
        this.popSize = popSize;

        nShark = (int) (0.04 * popSize);
        nm = (int) (0.04 * popSize * (Math.pow(SP, -0.4321)));

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("MBF",
                "@article{jahani2016tackling,"
                        + "  title={Tackling global optimization problems with a novel algorithm Mouth Brooding Fish algorithm},"
                        + "  author={Jahani, Ehsan and Chizari, Mohammad},"
                        + "  journal={Applied Soft Computing},"
                        + "  volume={},"
                        + "  pages={},"
                        + "  year={2016},"
                        + "  publisher={Elsevier}}",
                "MBF", "Mouth Brooding Fish");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;

        ASDP = 0.1 * (task.getUpperLimit()[0] - task.getLowerLimit()[0]);
        ASDN = -ASDP;

        effectOfSharkAttack = new int[task.getNumberOfDimensions()][2];

        for (int i = 0; i < task.getNumberOfDimensions(); i++) {
            //TODO are these indices
            effectOfSharkAttack[i][0] = i;
        }

        initPopulation();
        int it;
        while (!task.isStopCriterion()) {

            it = task.getNumberOfIterations();


            for (int i = 0; i < popSize; i++) {

                List<Double> newSolution = population.get(i).getVariables();
                CichlidsSolution solution = population.get(i);

                for (int d = 0; d < task.getNumberOfDimensions(); d++) {

                    if (it > 1) {
                        if ((BestResult[1].getEval() / BestResult[0].getEval()) < 0.85) {
                            //Update Movement Cichilds Protected by Mother using Regularity
                            solution.movment[d] = SP * solution.movment[d]
                                    + Dis * Util.nextDouble() * (solution.localBest.getValue(d) - solution.getValue(d))
                                    + Dis * Util.nextDouble() * (best.getValue(d) - solution.getValue(d));
                        } else {
                            //Update Movement Cichlids Protected by Mother using Force of Nature
                            solution.movment[d] = SP * solution.movment[d]
                                    + Dis * Util.nextDouble() * (solution.localBest.getValue(d) - solution.getValue(d))
                                    + Dis * Util.nextDouble() * (BestResult[1].getValue(d) - solution.getValue(d))
                                    + Dis * Util.nextDouble() * NatureForce[kk - 1].movment[d];
                            checkCONV = checkCONV + 1;
                        }
                    }
                    //Apply Movement Limits
                    solution.movment[d] = Math.max(solution.movment[d], ASDN);
                    solution.movment[d] = Math.min(solution.movment[d], ASDP);

                    //Update Position
                    newSolution.set(d, newSolution.get(d) + solution.movment[d]);

                    //Movement Mirror Effect
                    if (solution.movment[d] > task.getUpperLimit()[d] || solution.movment[d] < task.getLowerLimit()[d]) {
                        solution.movment[d] = -solution.movment[d];
                    }

                }
                solution.setVariables(newSolution);
                //Apply Position Limits
                task.setFeasible(solution);

                //Evaluation
                if (task.isStopCriterion()) {
                    break;
                }
                task.eval(solution);

                //Update Global Best
                if (task.isFirstBetter(solution, best)) {
                    checkpoint = 1;
                    best = new DoubleSolution(solution);
                    BestResult[it + 1] = solution;
                }

                //Update Cichlids BestLocal
                if (task.isFirstBetter(solution, solution.localBest))
                    solution.localBest = new DoubleSolution(solution);

            }

            //Left Out Cichlids Movement to Search the whole Space
            if (checkCONV > 10) {
                for (int i = 1; i < nm; i++) {
                    int NMC = (int) Math.ceil(task.getNumberOfDimensions() * Pdis);

                    int[] randSample = new int[NMC];

                    for (int j = 0; j < NMC; j++) {
                        randSample[j] = Util.nextInt(task.getNumberOfDimensions());
                    }

                    double UASDP = 4 * ASDP;
                    int ind = Util.nextInt(popSize);

                    List<Double> p = population.get(ind).getVariables();
                    for (int k = 0; k < NMC; k++) {
                        population.get(ind).setValue(randSample[k], UASDP * Util.nextDouble() + population.get(ind).getValue(randSample[k]));

                    }

                    //Update Movement of Left Out Cichilds
                    for (int j = 0; j < task.getNumberOfDimensions(); j++) {
                        population.get(ind).movment[j] = population.get(ind).getValue(j) - p.get(j);
                    }

                    //Apply Position Limits
                    task.setFeasible(population.get(ind));

                    //Evaluation
                    if (task.isStopCriterion()) {
                        break;
                    }

                    task.eval(population.get(ind));

                    //TODO no check in matlab code
                    //Update Cichlids BestLocal
                    if (task.isFirstBetter(population.get(ind), population.get(ind).localBest))
                        population.get(ind).localBest = new DoubleSolution(population.get(ind));

                    //Update Global Best BestLocal
                    if (task.isFirstBetter(population.get(ind), best)) {
                        checkpoint = 1;
                        BestResult[it + 1] = population.get(ind);
                        best = new DoubleSolution(population.get(ind));

                    }
                }
                checkCONV = checkCONV - 1;
            }
            //Crossover of Cichlids using RouletteWheel Selection
            population.sort(new TaskComparator(task));
            double[] P1 = new double[popSize];
            double sum = 0.0;
            for (int i = 0; i < popSize; i++) {
                P1[i] = Math.exp(1 - (beta * population.get(i).getEval() / best.getEval()));
                sum += P1[i];
            }

            for (int i = 0; i < popSize; i++) {
                P1[i] /= sum;
            }

            int i1, i2;
            //i1 = rouletteWheelSelection(P1);
            //i2 = rouletteWheelSelection(P1);
            i1 = 10;
            i2 = 20;

            crossover(i1, i2, it);

            if (checkpoint < 1) {
                //BestResult[it + 1] = BestResult[it];
                BestResult[1] = BestResult[0];
            }

            //Force of Nature (Convergence to the Best Trend)
            if (it > 0) {
                List<Double> nPosition = new ArrayList<Double>();
                double[] nMovement = new double[task.getNumberOfDimensions()];

                double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    nPosition.add(BestResult[1].getValue(i) - BestResult[0].getValue(i));
                    nMovement[i] = Math.abs(nPosition.get(i));
                    if (nMovement[i] > max) max = nMovement[i];
                    if (nMovement[i] < min) min = nMovement[i];

                }

                NatureForce[kk] = new CichlidsSolution(task.getNumberOfDimensions());
                NatureForce[kk].setVariables(nPosition);
                NatureForce[kk].movment = nMovement;

                int ncc = 0; //number of elements in ncc
                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    double Sx = (max - NatureForce[kk].movment[i]) / (max - min);
                    if (Sx < 0.4) {
                        NatureForce[kk].NCC[ncc] = i;
                        ncc++;
                    }
                }

                for (int row = 0; row < ncc; row++) {
                    for (int RCheck = 0; RCheck < task.getNumberOfDimensions(); RCheck++) {
                        if (effectOfSharkAttack[RCheck][0] == NatureForce[kk].NCC[row]) {
                            effectOfSharkAttack[RCheck][1] = effectOfSharkAttack[RCheck][1] + 1;
                            break;
                        }
                    }
                }

                for (int uj = 0; uj < ncc; uj++) {
                    NatureForce[kk].setValue(NatureForce[kk].NCC[uj], 10 * SP * NatureForce[kk].getValue(NatureForce[kk].NCC[uj]));
                }

                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    NatureForce[kk].movment[i] = NatureForce[kk].getValue(i);
                }

                //Shark Attack Effects on Cichlids

                SharkAttack[kk] = new CichlidsSolution(BestResult[0]);
                for (int hju = 0; hju < task.getNumberOfDimensions(); hju++) {
                    SharkAttack[kk].setValue(effectOfSharkAttack[task.getNumberOfDimensions() - 1][0], effectOfSharkAttack[task.getNumberOfDimensions() - 1][1] * SharkAttack[kk].getValue(effectOfSharkAttack[task.getNumberOfDimensions() - 1][0]));
                }

                for (int iii = 0; iii < nShark; iii++) {


                    int num = Util.nextInt(task.getNumberOfDimensions());
                    ;
                    List<Double> p = population.get(num).getVariables();

                    //Update Movement Cichlids Under Shark Attack
                    for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                        population.get(num).movment[i] = SharkAttack[kk].getValue(i) - p.get(i);
                    }

                    //Evaluation
		            /*Cichlids(num).Cost=CostFunction(Cichlids(num).Position);
		    
		            //Update Cichlids BestLocal
		            Cichlids(num).BestLocal.Position=Cichlids(num).Position;
		            Cichlids(num).BestLocal.Cost=Cichlids(num).Cost;
		    
		            //Update Global Best BestLocal
		            if(Cichlids(num).BestLocal.Cost < GlobalBest.Cost){
		                BestResult(it) = Cichlids(num);
		                GlobalBest=Cichlids(num).BestLocal;
		        
		        	}*/
                }
                //kk = kk + 1;
                kk = 2; //TODO ne bo delalo ker se potem na indeksu kk-1 nikoli ne spremeni vrednost
            }

            SP = SP * SPdamp;
            it = it + 1;
            checkpoint = 0;

            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void crossover(int i1, int i2, int it) throws StopCriterionException {

        double alpha;

        List<Double> x1 = population.get(i1).getVariables();
        List<Double> x2 = population.get(i2).getVariables();

        double[] y1 = new double[task.getNumberOfDimensions()];
        double[] y2 = new double[task.getNumberOfDimensions()];

        for (int i = 0; i < task.getNumberOfDimensions(); i++) {
            alpha = Util.nextDouble(-gamma, 1 + gamma);

            y1[i] = alpha * x1.get(i) + (1 - alpha) * x2.get(i);
            y2[i] = alpha * x2.get(i) + (1 - alpha) * x1.get(i);
            y1[i] = task.setFeasible(y1[i], i);
            y2[i] = task.setFeasible(y2[i], i);

        }

        if (task.isStopCriterion())
            return;

        CichlidsSolution sol1 = new CichlidsSolution(task.eval(y1));
        population.set(i1, sol1);

        if (task.isFirstBetter(sol1, best)) {
            checkpoint = 1;
            best = new DoubleSolution(sol1);
            BestResult[1] = sol1;
        }

        if (task.isStopCriterion())
            return;

        CichlidsSolution sol2 = new CichlidsSolution(task.eval(y2));
        population.set(i2, sol2);

        if (task.isFirstBetter(sol2, best)) {
            checkpoint = 1;
            best = new DoubleSolution(sol2);
            BestResult[1] = sol2;
        }
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<CichlidsSolution>();
        NatureForce = new CichlidsSolution[popSize];
        SharkAttack = new CichlidsSolution[popSize];
        BestResult = new CichlidsSolution[popSize];

        best = task.getRandomEvaluatedSolution();
        population.add(new CichlidsSolution(best));

        for (int i = 1; i < popSize; i++) {
            population.add(new CichlidsSolution(task.getRandomEvaluatedSolution()));
            population.get(i).movment = new double[task.getNumberOfDimensions()];

            if (task.isFirstBetter(population.get(i), best)) {
                best = new DoubleSolution(population.get(i));
            }

            if (task.isStopCriterion())
                break;
        }

        BestResult[0] = new CichlidsSolution(best);
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
