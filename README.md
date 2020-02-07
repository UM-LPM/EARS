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

* Go to: `File` - > `New` -> `Project from Version Control` -> `Git`
* Set URL to https://github.com/UM-LPM/EARS.git
* Select the directory where you want to save the project (example: "path to my projects/EARS")
* Import Gradle project

**Gradle Include Dependency to EARS project**

In file **settings.gradle** add:

```
include ':EARS'
project(':EARS').projectDir = new File('path to EARS') //example ../EARS
```

In file  **build.gradle** add:

```
dependencies {
    compile project(':EARS')
}
```

## Tips

* If you have a special representation for your solution create your own by extending the `DoubleSolution` class in EARS:
`class MySolution extends DoubleSolution`.
* Code examples can be found in the package `org.um.feri.ears.examples`.
* All information of the given problem (dimensions, constraints, bounds, etc...) can be obtaint from the Task object: `public Individual run(Task taskProblem)`.
* Before every evaluation check if the stopping criterion is reached by calling `taskProblem.isStopCriteria()`. If evaluate is called after the stopping criterion is reached, a `StopCriteriaException will` be thrown.

## Examples

Implementing a custom algorithm by extending the `Algorithm` class:
```java
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class RandomWalkAlgorithm extends Algorithm { // needs to extend Algorithm
    private DoubleSolution best; // used to save global best solution

    public RandomWalkAlgorithm() {
        ai = new AlgorithmInfo("RWSi", "", "RWSi", "Random Walk Simple"); // add algorithm name
        au = new Author("Matej", "matej.crepinsek at um.si"); // add author information
    }

    @Override
    public DoubleSolution execute(Task task) throws StopCriteriaException { // method which executes the algorithm
        // the task object holds information about the stopping criterion
        // and information about the problem (number of dimensions, number of constraints, upper and lower bounds...)
        DoubleSolution newSolution;
        best = task.getRandomSolution();  // generate new random solution (number of evaluations is incremented automatically)
        // to evaluate a custom solution or an array use task.eval(mySolution)
        while (!task.isStopCriteria()) { // run until the stopping criterion is not reached

            newSolution = task.getRandomSolution();
            if (task.isFirstBetter(newSolution, best)) { // use method isFirstBetter to check which solution is better (it checks constraints and considers the type of the problem (minimization or maximization))
                best = newSolution;
            }
            task.incrementNumberOfIterations(); // increase the number of generations for each iteration of the main loop
        }
        return best; // return the best solution found
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {}
}
```

Executing a single Task:

```java
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.Util;

public class SOSingleRun {

    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis()); // set a new seed for the random generator for each run

        Problem problem = new Sphere(5); // problem Sphere with five dimensions

        Task sphere = new Task(EnumStopCriteria.EVALUATIONS, 10000, 0, 0, 0.001, problem); // set the stopping criterion to max 10000 evaluations

        Algorithm alg = new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin);
        DoubleSolution best;
        try {
            best = alg.execute(sphere);
            System.out.println("Best found solution :" + best); // print the best solution found after 10000 evaluations
        } catch (StopCriteriaException e) {
            e.printStackTrace();
        }
    }
}
```
To perform a tournament you need more than one algorithm (player) and more than one task (problem) in the benchmark:

```java
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class SORatingExample {

    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis()); //set the seed of the random generator
        RatingBenchmark.debugPrint = false; //prints one on one results
        //add algorithms to a list
        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
        algorithms.add(new ABC());
        algorithms.add(new GWO());
        algorithms.add(new TLBOAlgorithm());
        algorithms.add(new RandomWalkAlgorithm());
        algorithms.add(new JADE());
        ResultArena ra = new ResultArena(100); // the result arena holds information about the players (wins, loses, draws, ratings...) 

        RatingRPUOed30 rpuoed30 = new RatingRPUOed30(); // benchmark with prepared tasks and settings

        rpuoed30.registerAlgorithms(algorithms);  // register the algorithms in the benchmark

        for (Algorithm al:algorithms) {
            ra.addPlayer(al, al.getID()); // add players with initial ratings
        }
        BankOfResults ba = new BankOfResults();

        rpuoed30.run(ra, ba, 10); //start the tournament with 10 runs/repetitions

        //display the leaderboard
        ArrayList<Player> list = ra.getPlayers();
        for (Player p: list) System.out.println(p);
    }
}
```

## Publications
N. Veček, M. Mernik, and M. Črepinšek. "A chess rating system for evolutionary algorithms: A new method for the comparison and ranking of evolutionary algorithms." Information Sciences 277 (2014): 656-679. http://www.sciencedirect.com/science/article/pii/S002002551400276X

N. Veček, M. Črepinšek, M. Mernik, D. Hrnčič. "A comparison between different chess rating systems for ranking evolutionary algorithms." 2014 Federated Conference on Computer Science and Information Systems. IEEE, 2014. https://ieeexplore.ieee.org/abstract/document/6933058

N. Veček, M. Mernik, B. Filipič, and M. Črepinšek. "Parameter tuning with Chess Rating System (CRS-Tuning) for meta-heuristic algorithms." Information Sciences 372 (2016): 446-469. https://www.sciencedirect.com/science/article/pii/S0020025516306430

N. Veček, M. Črepinšek, and M. Mernik. "On the influence of the number of algorithms, problems, and independent runs in the comparison of evolutionary algorithms." Applied Soft Computing 54 (2017): 23-45. https://www.sciencedirect.com/science/article/pii/S0020025516306430

N. Veček, S. H. Liu, M. Črepinšek, and M. Mernik. "On the importance of the artificial bee colony control parameter ‘limit’." Information Technology And Control 46.4 (2017): 566-604. http://www.itc.ktu.lt/index.php/ITC/article/view/18215

M. Ravber, M. Mernik, and M. Črepinšek. "The impact of quality indicators on the rating of multi-objective evolutionary algorithms." Applied Soft Computing 55 (2017): 265-275. https://www.sciencedirect.com/science/article/pii/S1568494617300534

M. Ravber, M. Mernik, and M. Črepinšek. "Ranking multi-objective evolutionary algorithms using a chess rating system with quality indicator ensemble." 2017 IEEE Congress on Evolutionary Computation (CEC). IEEE, 2017. https://ieeexplore.ieee.org/abstract/document/7969481

M. Ravber, M. Črepinšek, M. Mernik, and T. Kosar. "Tuning Multi-Objective Optimization Algorithms for the Integration and Testing Order Problem." International Conference on Bioinspired Methods and Their Applications. Springer, Cham, 2018. https://link.springer.com/chapter/10.1007/978-3-319-91641-5_20

M. Črepinšek, M. Ravber, M. Mernik, and T. Kosar. "Tuning Multi-Objective Evolutionary Algorithms on Different Sized Problem Sets." Mathematics 7.9 (2019): 824. https://www.mdpi.com/2227-7390/7/9/824 



*The authors acknowledge the financial support from the Slovenian Research Agency (research core funding No. P2-0041 COMPUTER SYSTEMS, METHODOLOGIES, AND INTELLIGENT SERVICES)* http://p2-0041.feri.um.si/en/
