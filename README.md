# EARS - Evolutionary Algorithm Rating System

EARS is a free and open-source Java-based framework for ranking, developing and experimenting with single- and multi-objective evolutionary algorithms. The framework can be used for any optimization algorithm and is not limited just to evolutionary algorithms. EARS enables an easy and reliable way to rate and rank optimization algorithms. In the framework, a large set of implemented optimization algorithms and test problems are already provided.

[EARS used in an online competition](http://ears.um.si)

## Included features

* Multi-objective evolutionary algorithms (NSGA-II, NSGA-III, GDE3, PAES, PESA2, SPEA2, IBEA, OMOPSO, MOEA/D).
* Single-objective evolutionary algorithms (ABC, CRO, DE, FWA, GOA, GWO, ICA, JADE, PSO, TLBO).
* A wide variety of quality indicators (CS, EPS, IGD, IGD+, GD, MS, HV, R1, R2, R3, SPREAD, ER).
* Benchmarks (CEC2005, CEC2009, CEC2010, CEC2011, CEC2014, CEC2015, ZDT, DTLZ, WFG).
* Many common test functions (Ackley, Booth, Griewank, Rastrigin, Schaffer, Schwefel, Sphere ...).
* Experiments, examples and prepared benchmarks.

## How to use

### Intellij

**First Download EARS project**

* Go to: `File` - > `New` -> `Project from Version Control` -> `Git`
* Set URL to https://github.com/UM-LPM/EARS.git
* Select the directory where you want to save the project (example: "path to my projects/EARS")
* Import Gradle project

**Second create new project (select GRADLE) and set EARS Project dependency**

*Gradle*

In file **settings.gradle** add:

```
include ':EARS'
project(':EARS').projectDir = new File('path to EARS') //example ../EARS
```

In file  **build.gradle** add:

```
dependencies {
    implementation project(':EARS')
}
```

## Tips

* If you have a particular representation for your solution, create your own by extending the `Solution` class in EARS:
`class MySolution extends Solution`.
* Code examples can be found in the package `org.um.feri.ears.examples`.
* All information about the given problem (dimensions, constraints, bounds, etc...) can be obtained from the `Problem` object which is accessible from the `Task` object (e. g. `task.problem.getLowerLimit()`).
* Before every evaluation, check if the stopping criterion is reached by calling `task.isStopCriterion()`. If evaluate is called after the stopping criterion is reached, a `StopCriterionException` will be thrown.

## Examples

Implementing a custom algorithm by extending the `Algorithm` class:
```java
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class RandomWalkAlgorithm extends NumberAlgorithm { // needs to extend NumberAlgorithm
    private NumberSolution<Double> best; // used to save global best solution

    public RandomWalkAlgorithm() {
        ai = new AlgorithmInfo("RW", "Random Walk", "");
        au = new Author("Matej", "matej.crepinsek@um.si");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException { // method which executes the algorithm
        // the task object holds information about the stopping criterion
        // and information about the problem (number of dimensions, number of constraints, upper and lower bounds...)
        NumberSolution<Double> newSolution;
        best = task.getRandomEvaluatedSolution();  // generate new random solution (number of evaluations is incremented automatically)
        // to evaluate a custom solution or an array use task.eval(mySolution)
        while (!task.isStopCriterion()) { // run until the stopping criterion is not reached

            newSolution = task.getRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(newSolution, best)) { // use method isFirstBetter to check which solution is better (it checks constraints and considers the type of the problem (minimization or maximization))
                best = newSolution;
            }
            task.incrementNumberOfIterations(); // increase the number of generations for each iteration of the main loop
        }
        return best; // return the best solution found
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
```

Executing a single Task:

```java
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.random.RNG;

public class SOSingleRun {

    public static void main(String[] args) {

        //RNG.setSeed(100); // set a specific seed for the random generator

        DoubleProblem problem = new Sphere(5); // problem Sphere with five dimensions

        Task sphere = new Task(problem, StopCriterion.EVALUATIONS, 10000, 0, 0); // set the stopping criterion to max 10000 evaluations

        NumberAlgorithm alg = new DEAlgorithm(DEAlgorithm.Strategy.JDE_RAND_1_BIN);
        NumberSolution<Double> best;
        try {
            best = alg.execute(sphere);
            System.out.println("Best solution found = " + best); // print the best solution found after 10000 evaluations
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }
}
```
To perform a tournament you need more than one algorithm (player) and more than one task (problem) in the benchmark:

```java
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.RPUOed30Benchmark;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class SOBenchmarkExample {

    public static void main(String[] args) {
        RNG.setSeed(System.currentTimeMillis()); //set the seed of the random generator
        Benchmark.printInfo = false; //prints one on one results
        //add algorithms to a list

        ArrayList<NumberAlgorithm> algorithms = new ArrayList<NumberAlgorithm>();
        algorithms.add(new ABC());
        algorithms.add(new GWO());
        algorithms.add(new TLBOAlgorithm());
        algorithms.add(new RandomWalkAlgorithm());
        algorithms.add(new JADE());

        RPUOed30Benchmark rpuoed30 = new RPUOed30Benchmark(); // benchmark with prepared tasks and settings

        rpuoed30.addAlgorithms(algorithms);  // register the algorithms in the benchmark

        rpuoed30.run(5); //start the tournament with 10 runs/repetitions
    }
}
```


To use ExpBas, the user has to define ExpBas setting (see main method) and modify the implementation of their optimization algorithm (see runDE method) accordingly:
* To check if the algorithm was in the exploration phase or not (then it is in the exploitation phase), pass reference solutions, new solution and setting to method ExpBas.isExploration(...).
* The user has to count exploration phases manually for each newly created solution (exploration is assumed for random solutions)
* At the end user has to divide the number of exploration phases with the number of evaluations to get the expbas measure

```java
import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.*;
import org.um.feri.ears.util.Util;
import java.util.*;

public class ExpBasExample {
    
    public static void main(String[] args) {
        Problem problem = new Rastrigin(2); // Get some problem
        
        // First initialize settings!
        ExpBas.ExpBasSettings setting = new ExpBas.ExpBasSettings(problem, 0.1); // Second parameter is in percentages and decides the step per each dimension
        setting.lsVariant = ExpBas.LocalSearchVariant.DeterministicBestImprovement;
        setting.nType = ExpBas.NeighbourhoodType.Sparse;
        setting.nOrder = ExpBas.NeighbourhoodOrder.NonReversed;
        
        Object[] result = runDE(problem, setting); // Pass this setting to some metaheuristic
        // Print your results
        System.out.println("Best fitness: " + result[1] + " -> ExpBas: " + result[2]);
        System.out.print("Solution: [ ");
        for(int i = 0; i < problem.getNumberOfDimensions(); i++) {
            ArrayList<Double> solution = (ArrayList<Double>) (result[0]);
            System.out.print(solution.get(i).toString() + " ");
        }
        System.out.println("]");
    }

    // Simple implementation of DE - EXAMPLE
    public static Object[] runDE(Problem problem, final ExpBas.ExpBasSettings setting){
        double F = 0.5;
        double CR = 0.9;
        int NP = 25;
        int maxFES = problem.getNumberOfDimensions() * 1000;

        ArrayList<ArrayList<Double>> population = new ArrayList<>();
        ArrayList<Double> populationFitnesses = new ArrayList<>();
        population.add(new ArrayList<>(Arrays.asList(ArrayUtils.toObject(problem.getRandomVariables()))));
        populationFitnesses.add(problem.eval(population.get(0)));
        ArrayList<Double> bestSolution = new ArrayList<>(population.get(0));
        double bestFitness = populationFitnesses.get(0);

        ExpBas metric = new ExpBas(); // Initialize EXPBAS
        int explorationPhases = 1; // Exploration is assumed for random solutions
        int cntFES = 1;

        // Initialize population
        for(int i = 1; i < NP; i++){
            population.add(new ArrayList<>(Arrays.asList(ArrayUtils.toObject(problem.getRandomVariables()))));
            explorationPhases++; // Exploration is assumed for random solutions
            populationFitnesses.add(problem.eval(population.get(i)));
            cntFES++;
            if(populationFitnesses.get(i) < bestFitness){
                bestFitness = populationFitnesses.get(i);
                bestSolution = new ArrayList<>(population.get(i));
            }
        }

        // Main loop
        while(cntFES < maxFES){
            for(int i = 0; i < NP; i++){
                // Mutation
                Set<Integer> mySet = new LinkedHashSet<Integer>();
                while (mySet.size() < 3) {
                    mySet.add(org.um.feri.ears.util.RNG.nextInt(NP));
                }
                List<Integer> uniqInds = new ArrayList<Integer>();
                uniqInds.addAll(mySet);

                ArrayList<Double> v = new ArrayList<>();
                for(int j = 0; j < problem.getNumberOfDimensions(); j++){
                    v.add(population.get(uniqInds.get(0)).get(j) + F * (population.get(uniqInds.get(1)).get(j) - population.get(uniqInds.get(2)).get(j)));
                }

                // Crossover
                ArrayList<Double> u = new ArrayList<>();
                for(int j = 0; j < problem.getNumberOfDimensions(); j++){
                    if(org.um.feri.ears.util.RNG.nextDouble() <= CR || i == RNG.nextInt(NP))
                        u.add(v.get(j));
                    else
                        u.add(population.get(i).get(j));
                }

                // To find out if exploration or exploitation we have to pass reference solutions and new solution 
                // Since in DE 4 solutions are used to create new solution, these solutions are reference solutions
                ArrayList<ArrayList<Double>> referenceSols = new ArrayList<>();
                referenceSols.add(new ArrayList<>(population.get(uniqInds.get(0))));
                referenceSols.add(new ArrayList<>(population.get(uniqInds.get(1))));
                referenceSols.add(new ArrayList<>(population.get(uniqInds.get(2))));
                referenceSols.add(new ArrayList<>(population.get(i)));

                boolean isRandom = false;  // Flag remembers if new solution is created randomly
                if(!problem.isFeasible(u)){
                    u = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(problem.getRandomVariables())));
                    explorationPhases++;  // Exploration is assumed for random solutions
                    isRandom = true;
                }
                double uFitness = problem.eval(u);
                cntFES++;

                // Selection
                if(uFitness < populationFitnesses.get(i)){
                    population.set(i, u);
                    populationFitnesses.set(i, uFitness);
                }

                if(uFitness < bestFitness){
                    bestSolution = new ArrayList<>(u);
                    bestFitness = uFitness;
                }
                if(!isRandom){
                    // In case new solution was not generated randomly, we call our metric and check if exploration
                    // We pass reference solutions, new solution and concrete ExpBas setting
                    if(metric.isExploration(referenceSols, u, setting)){
                        explorationPhases++;
                    }
                }
            }
        }
        // Total ExpBas is calculated by the number of exploration phases divided by number of evaluated individuals.
        double expbas = (double) explorationPhases / (double) cntFES;
        return new Object[] { bestSolution, bestFitness, expbas };
    }
}
```

## Publications
N. Veček, M. Mernik, and M. Črepinšek. "A chess rating system for evolutionary algorithms: A new method for the comparison and ranking of evolutionary algorithms." Information Sciences 277 (2014): 656-679. http://www.sciencedirect.com/science/article/pii/S002002551400276X

N. Veček, M. Črepinšek, M. Mernik, D. Hrnčič. "A comparison between different chess rating systems for ranking evolutionary algorithms." 2014 Federated Conference on Computer Science and Information Systems. IEEE, 2014. https://ieeexplore.ieee.org/abstract/document/6933058

N. Veček, M. Mernik, B. Filipič, and M. Črepinšek. "Parameter tuning with Chess Rating System (CRS-Tuning) for meta-heuristic algorithms." Information Sciences 372 (2016): 446-469. https://www.sciencedirect.com/science/article/pii/S0020025516306430

N. Veček, M. Črepinšek, and M. Mernik. "On the influence of the number of algorithms, problems, and independent runs in the comparison of evolutionary algorithms." Applied Soft Computing 54 (2017): 23-45. https://doi.org/10.1016/j.asoc.2017.01.011

N. Veček, S. H. Liu, M. Črepinšek, and M. Mernik. "On the importance of the artificial bee colony control parameter ‘limit’." Information Technology And Control 46.4 (2017): 566-604. http://www.itc.ktu.lt/index.php/ITC/article/view/18215

M. Ravber, M. Mernik, and M. Črepinšek. "The impact of quality indicators on the rating of multi-objective evolutionary algorithms." Applied Soft Computing 55 (2017): 265-275. https://www.sciencedirect.com/science/article/pii/S1568494617300534

M. Ravber, M. Mernik, and M. Črepinšek. "Ranking multi-objective evolutionary algorithms using a chess rating system with quality indicator ensemble." 2017 IEEE Congress on Evolutionary Computation (CEC). IEEE, 2017. https://ieeexplore.ieee.org/abstract/document/7969481

M. Ravber, M. Črepinšek, M. Mernik, and T. Kosar. "Tuning Multi-Objective Optimization Algorithms for the Integration and Testing Order Problem." International Conference on Bioinspired Methods and Their Applications. Springer, Cham, 2018. https://link.springer.com/chapter/10.1007/978-3-319-91641-5_20

M. Jesenik, M. Mernik, M. Črepinšek, M. Ravber, and M. Trlep. "Searching for soil models’ parameters using metaheuristics." Applied Soft Computing 69 (2018): 131-148.
https://doi.org/10.1016/j.asoc.2018.04.045

M. Črepinšek, M. Ravber, M. Mernik, and T. Kosar. "Tuning Multi-Objective Evolutionary Algorithms on Different Sized Problem Sets." Mathematics, 7.9 (2019): 824. https://www.mdpi.com/2227-7390/7/9/824 

M. Črepinšek, S. H. Liu, M. Mernik, and M. Ravber. "Long term memory assistance for evolutionary algorithms." Mathematics, 7(11) (2019): 1129. https://www.mdpi.com/2227-7390/7/11/1129

M. Ravber, Ž. Kovačević, M. Črepinšek, and M. Mernik. "Inferring Absolutely Non-Circular Attribute Grammars with a Memetic Algorithm." Applied Soft Computing 100 (2021), 106956. https://www.sciencedirect.com/science/article/abs/pii/S1568494620308942

J. Jerebic, M. Mernik, S. Liu, M. Ravber, M. Baketarić, L. Mernik, and M. Črepinšek. "A novel direct measure of exploration and exploitation based on attraction basins." Expert Systems with Applications 167 (2021), 114353. https://doi.org/10.1016/j.eswa.2020.114353

*The authors acknowledge the financial support from the Slovenian Research Agency (research core funding No. P2-0041 COMPUTER SYSTEMS, METHODOLOGIES, AND INTELLIGENT SERVICES)* http://p2-0041.feri.um.si/en/
