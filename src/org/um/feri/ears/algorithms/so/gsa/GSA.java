package org.um.feri.ears.algorithms.so.gsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

/**
 * 
 * Code from http://www.mathworks.com/matlabcentral/fileexchange/27756-gravitational-search-algorithm--gsa-
 * 
 * @author crepinsek
 *
 */

public class GSA extends Algorithm {
	private static double eps = 2.2204e-16; //http://www.mathworks.com/help/matlab/ref/eps.html?searchHighlight=eps
	int pop_size;
	double Rpower = 1; //in main
	int Rnorm=2; //in GSA it is not used 
	int final_per=2; //Gfield %In the last iteration, only 2 percent of agents apply force to the others.
	double alfa=20; //Gconstant
	double G0=100; //Gconstant
	int D; //dimension
	boolean elitistCheck=true;
	ArrayList<GSAIndividual> pop_x; //population
	GSAIndividual g; //global best in matlab Fbest (fitness best),Lbest (location best)
	public GSA() {
		this(50, 2, 1, 20, 100); //Mathlab settings
	}
	
	public GSA(int pop_size) {
		this(pop_size, 2, 1, 20, 100); //Mathlab settings
	}
	
	public GSA(double RPower, double alfa, double G0) {
		this(50, 2, RPower,alfa, G0);
	}
	public GSA(int pop_size, int  final_per, double RPower, double alfa, double G0) {
		super();
		this.alfa = alfa;
		this.G0 = G0;
		this.Rpower = RPower;
		this.pop_size = pop_size;
		this.Rpower = 1;
		this.final_per = final_per;
	    setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("GSA2","@article{Rashedi20092232,"+
        		"title = \"GSA: A Gravitational Search Algorithm \","+
"journal = \"Information Sciences \","+
"volume = \"179\","+
"number = \"13\","+
"pages = \"2232 - 2248\","+
"year = \"2009\","+
"note = \"Special Section on High Order Fuzzy Sets \","+
"issn = \"0020-0255\","+
"doi = \"http://dx.doi.org/10.1016/j.ins.2009.03.004\","+
"url = \"http://www.sciencedirect.com/science/article/pii/S0020025509001200\","+
"author = \"Esmat Rashedi and Hossein Nezamabadi-pour and Saeid Saryazdi\"}",
"GSA2","Matlab GSA2");  //EARS add algorithm name
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED1, final_per + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED2, RPower + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED3, alfa + "");
        ai.addParameter(EnumAlgorithmParameters.UNNAMED4, G0 + "");
        au =  new Author("Ears implementation by Matej", "matej.crepinsek at um.si"); //EARS author info
	}
	
	
	@Override
	public DoubleSolution execute(Task t) throws StopCriteriaException {
		initPopulation(t);
		int iteration = 1;
		int max_it = (t.getMaxEvaluations()-pop_size)/pop_size;  //-initpopulation!
		double M[] = new double[pop_size];
		double G;
		D = t.getNumberOfDimensions();
		GSAIndividual tmpIn;
		while (!t.isStopCriteria()) {
			G=G0*Math.exp(-alfa*iteration/max_it); //%eq. 28. Gconstant; 
			if (debug) {
				System.out.println("Current iteration is "+iteration+"/"+max_it+" G="+G);
			}
			//%Calculation of M. eq.14-20
			//[M]=massCalculation(fitness,min_flag); 
			massCalculation(M,t); //put mass in Individual
			//%Calculation of Gravitational constant. eq.13.
			//G=Gconstant(iteration,max_it);
			//%Calculation of accelaration in gravitational field. eq.7-10,21.
			//a=Gfield(M,X,G,Rnorm,Rpower,ElitistCheck,iteration,max_it);
			Gfield( elitistCheck,  iteration,  max_it,  G);
			for (int i=0; i<pop_size; i++) {
				tmpIn = pop_x.get(i).move(t);
				if (t.isFirstBetter(tmpIn,pop_x.get(i))) { //CM all have this?
					pop_x.set(i, tmpIn);
				}
				if (t.isFirstBetter(pop_x.get(i), g)) {
					g = pop_x.get(i); 
					if (debug) {
						System.out.println("New best: "+g.getEval());
					}
				}
				if (t.isStopCriteria()) break;
			}
			iteration++;
		}
		return g;
	}

	
	private void massCalculation(double[] m, Task t) {
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
		GSAIndividual best, worst;
		double sum=0;
		best = pop_x.get(0);
		worst = pop_x.get(0);
		//sum = pop_x.get(0).getEval(); //problem of negative values????
		for (int i=1; i<pop_size; i++) {
			if (t.isFirstBetter(pop_x.get(i), best)) best = pop_x.get(i);
			if (t.isFirstBetter(worst, pop_x.get(i))) worst = pop_x.get(i);
		}
		//Add moveFit CM 
		double moveFit=0;
		if (((best.getEval()<0) && (worst.getEval()>0))|| ((best.getEval()>0) && (worst.getEval()<0))){ //-3, -2, .., 3, 4 or 3, 2, .., -3, -4
			if (t.isMaximize())
				moveFit=-worst.getEval();
			else
			  moveFit=+worst.getEval();
		}
		if (best.equals(worst)) {
			Arrays.fill(m, 1); //if Fmax==Fmin; M=ones(N,1);
		} else {//Problem mixing positive and negative values fitness!
			for (int i=0; i<pop_size; i++) { 
				//eps added that we dont have 0 on worst individual
				m[i] = moveFit+(pop_x.get(i).getEval()+moveFit-worst.getEval()+moveFit)/(best.getEval()+moveFit-worst.getEval()+moveFit)+eps; //M=(fit-worst)./(best-worst); %eq.15,
				sum += m[i]; 
			}
			
		}
		for (int i=0; i<pop_size; i++) {
			m[i] = m[i] / sum;
			pop_x.get(i).setMass(m[i]);
		}
		
	}

/*
function a=Gfield(M,X,G,Rnorm,Rpower,ElitistCheck,iteration,max_it);

[N,dim]=size(X);
 final_per=2; %In the last iteration, only 2 percent of agents apply force to the others.

%%%%total force calculation
 if ElitistCheck==1
     kbest=final_per+(1-iteration/max_it)*(100-final_per); %kbest in eq. 21.
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
	void Gfield(boolean elitistCheck, int iteration, int max_it, double G) {
		int kbest;
		double dkbest=0;
		if (elitistCheck) {
		   dkbest=final_per+(1.-(double)iteration/max_it)*(100.-final_per);// %kbest in eq. 21.
		  kbest=(int) Math.ceil(pop_size*dkbest/100); //CM instead round that return 0 in last iter
		}
		else {
		  kbest=pop_size;// %eq.9.
		}
		//[Ms ds]=sort(M,'descend');
		/* Works OK
		if (debug) {
			System.out.println("Take mass for k-best endividuals:"+kbest+" ("+dkbest+")");
		}
		*/
		Collections.sort(pop_x,new Comparator<GSAIndividual>(){
			@Override
			public int compare(GSAIndividual o1, GSAIndividual o2) {
				if (o1.getMass()>o2.getMass()) return -1;
				if (o1.getMass()<o2.getMass()) return 1;
				return 0; 
			}});
	/*	works OK
	 */ if (debug) {
		  System.out.println("Descending masss (kbest="+kbest+" )"); 
		  printPop(iteration);
		}
		//*/
		double tmp, tm,R;
		for (int i=0; i<pop_size; i++) {
			pop_x.get(i).resetE(); //clear history
			for (int ii=0; ii<kbest; ii++) { //mass for best k
				if (ii!=i) {
					//R=norm(X(i,:)-X(j,:),Rnorm); %Euclidian distanse Rnorm=2.
					tmp = 0;
					for (int d=0; d<D; d++) {
						tm = pop_x.get(i).getVariables()[d]-pop_x.get(ii).getVariables()[d];
						tmp+=tm*tm;
					}
					R = Math.sqrt(tmp);
					//end of Euclidian distanse
					for (int d=0; d<D; d++) {
						// E(i,k)=E(i,k)+rand*(M(j))*((X(j,k)-X(i,k))/(R^Rpower+eps));
						pop_x.get(i).getE()[d] +=Util.rnd.nextDouble()*pop_x.get(ii).getMass()*(pop_x.get(ii).getVariables()[d]-pop_x.get(i).getVariables()[d])/(Math.pow(R,Rpower)+eps);
					}
				}
			}
			for (int d=0; d<D; d++) {
			  pop_x.get(i).getA()[d]= pop_x.get(i).getE()[d]*G; //a=E.*G; %note that Mp(i)/Mi(i)=1
			}
		}
		
	}

	private void printPop(int iteration) {
		for (int i=0;i<pop_size;i++) {
			System.out.println(""+i+". Mass "+pop_x.get(i).getMass()+" fit:"+pop_x.get(i).getEval());
		}
		
	}


	private void initPopulation(Task taskProblem) throws StopCriteriaException {
		pop_x = new ArrayList<>();
		for (int i=0; i<pop_size; i++) {
			pop_x.add(new GSAIndividual(taskProblem));
			if (i==0) g = pop_x.get(0);
			else if (taskProblem.isFirstBetter(pop_x.get(i), g)) g=pop_x.get(i);
			if (taskProblem.isStopCriteria()) break;
		}
	}
	
	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub
		
	}

}
