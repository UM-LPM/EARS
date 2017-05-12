package org.um.feri.ears.algorithms.so.gsa;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

import java.util.*;

/**
 * Created by Nik Orter on 22. 10. 2016.
 */
public class GSAv2 extends Algorithm {

    int pop_size;
    ArrayList<Agent> pop;
    Agent best;

    double G;
    double epsilon= 0.0000000000000000000000000001;

    public GSAv2(int pop_size) {
        this.pop_size = pop_size;
        ai = new AlgorithmInfo("", "", "GSA", "GSO");  //EARS add algorithm name
        au = new Author("nikorter", "nik.orter@gmail.com"); //EARS author info
        pop = new ArrayList<>();
    }

    public GSAv2() {
        this(50);
    }

    //generate random population
    void initPop(Task t) throws StopCriteriaException {
        pop.clear();
        best=null;
            for (int i = 0; i < pop_size; i++) {
                Agent newAgent = new Agent(t,i);
                if (best == null)
                    best = new Agent(newAgent,t);
                else if (t.isFirstBetter(newAgent, best)) {
                    best =  new Agent(newAgent,t);
                    //System.out.println(best);
                }
                pop.add(newAgent);

                if (t.isStopCriteria()) break;
            }
    }

    void UpdateMasses()
    {
        //first we need best and worst fit in population
        double bestFit = pop.get(0).getEval();
        double worstFit = pop.get(0).getEval();
        for (Agent agent: pop) {
            if(bestFit > agent.getEval())
                bestFit = agent.getEval();
            if(agent.getEval() > worstFit)
                worstFit = agent.getEval();
        }
        //calculate tmp  mass
        double []m = new double[pop_size];
        double sumM = 0.0f;
        for(int i = 0; i < pop_size;i++){
            m[i] = (pop.get(i).getEval()-worstFit)/(bestFit-worstFit+epsilon);
            sumM += m[i];
        }
        //update Mass for every agent
        for(int i = 0; i < pop_size;i++){
            pop.get(i).setMass(m[i]/sumM);
        }
    }

    double []getForceActingOnMi(double G,double M_i,double M_j,double []position_i,double []position_j)
    {
        double sum = 0;
        //calculate euclidian distance
        for(int i = 0; i < position_i.length;i++){
            sum  += Math.pow(position_j[i]-position_i[i],2);
        }
        double R = Math.sqrt(sum);

        double []force = new double[position_i.length];
        for(int i = 0; i < position_i.length;i++){
            //force[i] = G*(M_i*M_j)/(R+epsilon)*(position_j[i]-position_i[i]);
            force[i] = Math.random()*M_j*(position_j[i]-position_i[i])/(R+epsilon);
        }
        return force;
    }

    double []getTotalForces(List<double[]> forces){
        double []totalForce = new double[forces.get(0).length];
        for (double[] f_vector:forces){
            for(int i = 0; i < f_vector.length;i++){
                totalForce[i] += f_vector[i]* Math.random();
            }
        }
        return totalForce;
    }


    Agent UpdatePosition(Task t,int popIndex) throws StopCriteriaException {
        double []positions = pop.get(popIndex).getDoubleVariables();
        double []velocites = pop.get(popIndex).getVelocities();
        double []newPosition = new double[positions.length];
        for(int i = 0; i < positions.length;i++){
            newPosition[i] = t.setFeasible(positions[i] + velocites[i],i);
        }
        return new Agent(t.eval(newPosition),t);
    }

    void nextGeneration(Task task,int iteration,int maxIteration) throws StopCriteriaException {

        //G = 10f * Math.exp(-0.9*( iteration / (double)(maxIteration)));
        //calculate gravity
        double alfa=10,g_init = 160;
        G=g_init*Math.exp(-alfa*iteration/maxIteration);
        //update masses for agents
        UpdateMasses();
        List<Agent> solutions = new ArrayList<>(pop);
        Collections.sort(solutions, new Comparator<Agent>() {
            public int compare(Agent o1, Agent o2) {
                return o1.getMass() > o2.getMass()?-1:o1.getMass() < o2.getMass()?1:0;
            }
        });

        //only k best solution apply forces to others
        //int kBest = (int) (pop_size - (pop_size-1)*(iteration/(double)maxIteration));
        int kBest= (int) (2+(1-iteration/(double)maxIteration)*(pop_size-2));
        kBest=Math.round(pop_size*kBest/pop_size);

        //calculate new force
        for(int i = 0; i < pop_size;i++){
            List<double[]> tmpForces = new ArrayList<>();
            for (int j = 0; j < kBest;j++){
                if(pop.get(i).getID() == solutions.get(j).getID())
                    continue;
                double M_i = pop.get(i).getMass();
                double M_j = solutions.get(j).getMass();
                tmpForces.add(getForceActingOnMi(G,M_i,M_j,pop.get(i).getDoubleVariables(),solutions.get(j).getDoubleVariables()));
            }
            pop.get(i).setForces(getTotalForces(tmpForces));
        }
        //update velocities
        for (Agent a:pop){
            for(int i = 0; i < a.getSolutionSize();i++){
                double newVelocity = Math.random()*a.getVelocityAtIndex(i)+ a.getAccelerationOfSolution(i,G);
                a.setVelocityAtIndex(i,newVelocity);
            }
        }

        for (int i = 0; i < pop_size;i++){
            Agent tmpAgent = UpdatePosition(task,i);
            if(task.isFirstBetter(tmpAgent,best)) {
                best =  new Agent(tmpAgent,task);
                //System.out.println("ID:" + best.getID() + "  Fitness:" + best.getEval() + "  Solution:" + best );
                //System.out.println("[" +best.getDoubleVariables() + "]");
            }

            pop.set(i,tmpAgent);

            if(task.isStopCriteria())
                break;
        }

    }


    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        //get epsilon from task
        epsilon = taskProblem.getEpsilon();
        //inicializacija populacije
        initPop(taskProblem);
        int generation = 1;
        while (!taskProblem.isStopCriteria()) {
            if(taskProblem.getStopCriteria() == EnumStopCriteria.EVALUATIONS 
            		|| taskProblem.getStopCriteria() == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS 
            		|| taskProblem.getStopCriteria() == EnumStopCriteria.STAGNATION)
            	nextGeneration(taskProblem, generation, taskProblem.getMaxEvaluations()/pop_size);
            if(taskProblem.getStopCriteria() == EnumStopCriteria.ITERATIONS)
            	nextGeneration(taskProblem, generation, taskProblem.getMaxIteratirons());
           //TODO stop criteria stagnation 	
            generation++;
            taskProblem.incrementNumberOfIterations();
        }
        return best;
    }

    @Override
    public void resetDefaultsBeforNewRun() {

    }
}
