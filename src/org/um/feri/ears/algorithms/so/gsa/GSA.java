package org.um.feri.ears.algorithms.so.gsa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Code from http://www.mathworks.com/matlabcentral/fileexchange/27756-gravitational-search-algorithm--gsa-
 */

public class GSA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    private double Rpower = 1; //in main
    private int Rnorm = 2; //in GSA it is not used
    private int finalPer = 2; //Gfield %In the last iteration, only 2 percent of agents apply force to the others.
    private double alpha = 20; //Gconstant
    private double G0 = 100; //Gconstant
    private int D; //dimension

    private static double eps = 2.2204e-16; //http://www.mathworks.com/help/matlab/ref/eps.html?searchHighlight=eps
    private boolean elitistCheck = true;
    private ArrayList<GSASolution> popX; //population
    private GSASolution g; //global best in matlab Fbest (fitness best),Lbest (location best)

    public GSA() {
        this(50, 2, 1, 20, 100); //Matlab settings
    }

    public GSA(int popSize) {
        this(popSize, 2, 1, 20, 100); //Matlab settings
    }

    public GSA(double RPower, double alpha, double G0) {
        this(50, 2, RPower, alpha, G0);
    }

    public GSA(int popSize, int finalPer, double RPower, double alpha, double G0) {
        super();
        this.alpha = alpha;
        this.G0 = G0;
        this.Rpower = RPower;
        this.popSize = popSize;
        this.Rpower = 1;
        this.finalPer = finalPer;
        setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("GSA", "Gravitational Search Algorithm",
                "@article{Rashedi20092232," +
                        "title = \"GSA: A Gravitational Search Algorithm \"," +
                        "journal = \"Information Sciences \"," +
                        "volume = \"179\"," +
                        "number = \"13\"," +
                        "pages = \"2232 - 2248\"," +
                        "year = \"2009\"," +
                        "note = \"Special Section on High Order Fuzzy Sets \"," +
                        "issn = \"0020-0255\"," +
                        "doi = \"http://dx.doi.org/10.1016/j.ins.2009.03.004\"," +
                        "url = \"http://www.sciencedirect.com/science/article/pii/S0020025509001200\"," +
                        "author = \"Esmat Rashedi and Hossein Nezamabadi-pour and Saeid Saryazdi\"}"
        );
        au = new Author("Matej", "matej.crepinsek@um.si");
    }


    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        double[] M = new double[popSize];
        double G;
        D = task.problem.getNumberOfDimensions();
        GSASolution tmpIn;
        while (!task.isStopCriterion()) {
            G = G0 * Math.exp(-alpha * task.getNumberOfIterations() / maxIt); //%eq. 28. Gconstant;
            if (debug) {
                System.out.println("Current iteration is " + task.getNumberOfIterations() + "/" + maxIt + " G=" + G);
            }
            //%Calculation of M. eq.14-20
            //[M]=massCalculation(fitness,min_flag);
            massCalculation(M); //put mass in Individual
            //%Calculation of Gravitational constant. eq.13.
            //G=Gconstant(iteration,max_it);
            //%Calculation of accelaration in gravitational field. eq.7-10,21.
            //a=Gfield(M,X,G,Rnorm,Rpower,ElitistCheck,iteration,max_it);
            Gfield(elitistCheck, maxIt, G);
            for (int i = 0; i < popSize; i++) {
                tmpIn = popX.get(i).move(task);
                if (task.problem.isFirstBetter(tmpIn, popX.get(i))) { //CM all have this?
                    popX.set(i, tmpIn);
                }
                if (task.problem.isFirstBetter(popX.get(i), g)) {
                    g = popX.get(i);
                    if (debug) {
                        System.out.println("New best: " + g.getEval());
                    }
                }
                if (task.isStopCriterion()) break;
            }
            task.incrementNumberOfIterations();
        }
        return g;
    }

    private void massCalculation(double[] m) {
		/*
function [M]=massCalculation(fit,min_flag);
%%%%here, make your own function of 'mass calculation'
Fmax=max(fit); Fmin=min(fit); Fmean=mean(fit); 
[i N]=size(fit);
if Fmax==Fmin
   M=ones(N,1);
else
   if min_flag==1 %for minimization
      best=Fmin;worst=Fmax; %eq.17-18.
   else %for maximization
      best=Fmax;worst=Fmin; %eq.19-20.
   end
   M=(fit-worst)./(best-worst); %eq.15,
end
M=M./sum(M); %eq. 16.
		 * 
		 */
        GSASolution best, worst;
        double sum = 0;
        best = popX.get(0);
        worst = popX.get(0);
        //sum = pop_x.get(0).getEval(); //problem of negative values????
        for (int i = 1; i < popSize; i++) {
            if (task.problem.isFirstBetter(popX.get(i), best)) best = popX.get(i);
            if (task.problem.isFirstBetter(worst, popX.get(i))) worst = popX.get(i);
        }
        //Add moveFit CM
        double moveFit = 0;
        if (((best.getEval() < 0) && (worst.getEval() > 0)) || ((best.getEval() > 0) && (worst.getEval() < 0))) { //-3, -2, .., 3, 4 or 3, 2, .., -3, -4
            if (!task.problem.getObjectiveMaximizationFlags()[0])
                moveFit = -worst.getEval();
            else
                moveFit = +worst.getEval();
        }
        if (best.equals(worst)) {
            Arrays.fill(m, 1); //if Fmax==Fmin; M=ones(N,1);
        } else {//Problem mixing positive and negative values fitness!
            for (int i = 0; i < popSize; i++) {
                //eps added that we dont have 0 on worst individual
                m[i] = moveFit + (popX.get(i).getEval() + moveFit - worst.getEval() + moveFit) / (best.getEval() + moveFit - worst.getEval() + moveFit) + eps; //M=(fit-worst)./(best-worst); %eq.15,
                sum += m[i];
            }

        }
        for (int i = 0; i < popSize; i++) {
            m[i] = m[i] / sum;
            popX.get(i).setMass(m[i]);
        }

    }

    /*
    function a=Gfield(M,X,G,Rnorm,Rpower,ElitistCheck,iteration,maxIt);

    [N,dim]=size(X);
     final_per=2; %In the last iteration, only 2 percent of agents apply force to the others.

    %%%%total force calculation
     if ElitistCheck==1
         kbest=final_per+(1-iteration/maxIt)*(100-final_per); %kbest in eq. 21.
         kbest=round(N*kbest/100);
     else
         kbest=N; %eq.9.
     end
        [Ms ds]=sort(M,'descend');

     for i=1:N
         E(i,:)=zeros(1,dim);
         for ii=1:kbest
             j=ds(ii);
             if j~=i
                R=norm(X(i,:)-X(j,:),Rnorm); %Euclidian distanse.
             for k=1:dim
                 E(i,k)=E(i,k)+rand*(M(j))*((X(j,k)-X(i,k))/(R^Rpower+eps));
                  %note that Mp(i)/Mi(i)=1
             end
             end
         end
     end

    %%acceleration
    a=E.*G; %note that Mp(i)/Mi(i)=1
         *
         *
         *
         */
    private void Gfield(boolean elitistCheck, int maxIt, double G) {
        int kbest;
        double dkbest = 0;
        if (elitistCheck) {
            dkbest = finalPer + (1. - (double) task.getNumberOfIterations() / maxIt) * (100. - finalPer);// %kbest in eq. 21.
            kbest = (int) Math.ceil(popSize * dkbest / 100); //CM instead round that return 0 in last iter
        } else {
            kbest = popSize;// %eq.9.
        }
        //[Ms ds]=sort(M,'descend');
		/* Works OK
		if (debug) {
			System.out.println("Take mass for k-best endividuals:"+kbest+" ("+dkbest+")");
		}
		*/
        popX.sort((o1, o2) -> {
            return Double.compare(o2.getMass(), o1.getMass());
        });
        /*	works OK
         */
        if (debug) {
            System.out.println("Descending masss (kbest=" + kbest + " )");
            printPop();
        }
        //*/
        double tmp, tm, R;
        for (int i = 0; i < popSize; i++) {
            popX.get(i).resetE(); //clear history
            for (int ii = 0; ii < kbest; ii++) { //mass for best k
                if (ii != i) {
                    //R=norm(X(i,:)-X(j,:),Rnorm); %Euclidian distanse Rnorm=2.
                    tmp = 0;
                    for (int d = 0; d < D; d++) {
                        tm = popX.get(i).getValue(d) - popX.get(ii).getValue(d);
                        tmp += tm * tm;
                    }
                    R = Math.sqrt(tmp);
                    //end of Euclidian distanse
                    for (int d = 0; d < D; d++) {
                        // E(i,k)=E(i,k)+rand*(M(j))*((X(j,k)-X(i,k))/(R^Rpower+eps));
                        popX.get(i).getE()[d] += RNG.nextDouble() * popX.get(ii).getMass() * (popX.get(ii).getValue(d) - popX.get(i).getValue(d)) / (Math.pow(R, Rpower) + eps);
                    }
                }
            }
            for (int d = 0; d < D; d++) {
                popX.get(i).getA()[d] = popX.get(i).getE()[d] * G; //a=E.*G; %note that Mp(i)/Mi(i)=1
            }
        }
    }

    private void printPop() {
        for (int i = 0; i < popSize; i++) {
            System.out.println("" + i + ". Mass " + popX.get(i).getMass() + " fit:" + popX.get(i).getEval());
        }
    }

    private void initPopulation() throws StopCriterionException {
        popX = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            popX.add(new GSASolution(task));
            if (i == 0) g = popX.get(0);
            else if (task.problem.isFirstBetter(popX.get(i), g)) g = popX.get(i);
            if (task.isStopCriterion()) break;
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
