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

* Download it using the git plugin in eclipse.
* In same workspace create new java project.
* Add Properties -> Java Build Path -> Projects -> EARS
* Include your algorithm in the project.
* Modify algorithm to work with EARS.

### Add dependency to EARS project in gradle
In file **settings.gradle** add:
include ':EARS'
project(':EARS').projectDir = new File('path to EARS') //example ../EARS

In file  **build.gradle** add:
dependencies {
    compile project(':EARS')
}

## Tips

* If you have a special representation for your solution create your own by extending the `DoubleSolution` class in EARS:
`class MySolution extends DoubleSolution`.
* Code examples can be found in the package `org.um.feri.ears.examples`.
* All information of the given problem (dimensions, constraints, bounds, etc...) can be obtaint from the Task object: `public Individual run(Task taskProblem)`.
* Before every evaluation check if the stopping criterion is reached by calling `taskProblem.isStopCriteria()`. If evaluate is called after the stopping criterion is reached, a `StopCriteriaException will` be thrown.

## Examples

Implementing a custom algorithm by extending the `Algorithm` class:
```java
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;

public class RandomWalkAlgorithm extends Algorithm { //needs to me extended 
	private DoubleSolution i; //EARS Individual includes solution vector and its fitness value
	private boolean debug = true;

	public RandomWalkAlgorithm() { 
		super();
		setDebug(debug);  //EARS prints some debug info
		ai = new AlgorithmInfo("","","RWSi+","Random Walk+");  //EARS add algorithm name
		au =  new Author("robi", "N/A"); //EARS author info
	}

	@Override  
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException{ //EARS main evaluation loop 
		DoubleSolution ii;
		i = taskProblem.getRandomSolution(); //EARS Helper for creating random solution, it takes one evaluation (eval++)
		//user can use its own representation for example double[] and in fase of evaluation calls taskProblem.eval that creates individual
		System.out.println(taskProblem.getNumberOfEvaluations()+" "+i); //prints number of evaluations

		while (!taskProblem.isStopCriteria()) {   //EARS user needs to take care about number of evaluations
			ii = taskProblem.getRandomSolution();
			if (taskProblem.isFirstBetter(ii, i)) { //EARS primary function it takes care if we are searching minimum or maximum, if solution is valit etc.
				i = ii;
				if (debug) System.out.println(taskProblem.getNumberOfEvaluations()+" "+i);
			}
		}
		return i;

	}

	@Override
	public void resetDefaultsBeforNewRun() {
		i=null;
	}
}
```

Executing a single Task:

```java
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class Main4Run {
	public static void main(String[] args) {
		Task t = new Task(EnumStopCriteria.EVALUATIONS, 5000, 0, 0, 0.0001, new Sphere(5)); // problem Sphere, 5 dimensions, 5000 evaluations
		RandomWalkAlgorithm test = new RandomWalkAlgorithm();
		try {
			System.out.println(test.execute(t)); //prints best result afrer 5000 evaluations
		} catch (StopCriteriaException e) {
			e.printStackTrace();
		}
	}
}
```
To perform a tournament you need more than one algorithm (player) and more than one task (problem) in the benchmark:

```java
import java.util.ArrayList;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.cro.CRO;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.fwa.FWA;
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

public class SORatingExample {
	
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis()); //set the seed of the random generator    
        RatingBenchmark.debugPrint = false; //prints one on one results
        //add algorithms to list of players
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();
        players.add(new ABC());
        players.add(new CRO());
        players.add(new GWO());
        players.add(new FWA());
        players.add(new TLBOAlgorithm());
        players.add(new RandomWalkAlgorithm());
        players.add(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin));
        players.add(new JADE());
        ResultArena ra = new ResultArena(100); 

        RatingRPUOed30 rpuoed30 = new RatingRPUOed30();
        
        rpuoed30.registerAlgorithms(players);
        
        //set initial rating for each participating player
        for (Algorithm al:players) {
            ra.addPlayer(al, al.getID(), 1500, 350, 0.06,0,0,0); //initialize rating to 1500, RD to 350 and volatility to 0.6
        }
        BankOfResults ba = new BankOfResults();
        
        rpuoed30.run(ra, ba, 10); //start the tournament with 10 runs
        
        //display the leaderboard
        ArrayList<Player> list = ra.getPlayers();
        for (Player p: list) System.out.println(p);
    }
}
```

## Publications
N. Veček, M. Mernik, and M. Črepinšek. "A chess rating system for evolutionary algorithms: A new method for the comparison and ranking of evolutionary algorithms." Information Sciences 277 (2014): 656-679. http://www.sciencedirect.com/science/article/pii/S002002551400276X

*The authors acknowledge the financial support from the Slovenian Research Agency (research core funding No. P2-0041 COMPUTER SYSTEMS, METHODOLOGIES, AND INTELLIGENT SERVICES)* http://p2-0041.feri.um.si/en/
