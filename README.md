Evolutionary Algorithms Rating System 2.0
=====================================

EARS in action http://earatingsystem.appspot.com

What is included:
* some banchmarks with problem functions (Sphere, ...).
* some already implemented test Alorithms
* some simple test experiments


How to use it!

* All projects are Eclipse java projects.
* Download it use git in eclipse.
* In same workspace create new java project.
* Add Properties -> Java Build Path -> Projects -> EARS
* Include your algorithm in the project.
* Modify algorithm to work with EARS

Example:
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
Run it on single task:

- Run/execute your algorithm.

Example:
```java
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class Main4Run {
	public static void main(String[] args) {
		Task t = new Task(EnumStopCriteria.EVALUATIONS, 5000, 0, 0, 0.0001, new Sphere(5)); //run problem Sphere Dimension 5, 3000 evaluations
		RandomWalkAlgorithm test = new RandomWalkAlgorithm();
		try {
			System.out.println(test.execute(t)); //prints best result afrer 3000 runs
		} catch (StopCriteriaException e) {
			e.printStackTrace();
		}
	}
}
```
Compare:

* For rating you need more than one algorithm (player) and more than one task (banchmark)).

Example:
```java
import java.util.ArrayList;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class MainBenchMarkTest {
	public static void main(String[] args) {
		Util.rnd.setSeed(System.currentTimeMillis());
		
		RatingBenchmark.debugPrint = true; //prints one on one results
		ArrayList<Algorithm> players = new ArrayList<Algorithm>();
		players.add(new ES1p1sAlgorithm()); //EARS exampels
		players.add(new TLBOAlgorithm()); //EARS examples
		players.add(new RandomWalkAlgorithm());
		ResultArena ra = new ResultArena(100); 
		RatingRPUOed2 suopm = new RatingRPUOed2(); //Create banchmark
		suopm.setDisplayRatingIntervalChart(false);
		for (Algorithm al:players) {
			ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
			suopm.registerAlgorithm(al);
		}
		BankOfResults ba = new BankOfResults();
		suopm.run(ra, ba, 20); //repeat competition 20X
		ArrayList<Player> list = ra.getPlayers();
		for (Player p: list) System.out.println(p); //print rangs
	}
}
```

Tips
____

* If you have special representation create your own individual by extending EARS Individual.
"class MySolution extends DoubleSolution"
* Search for main methods in EARS source code for more examples.
* All problem data (Dimension, Bounds, etc...) can be obtaint by Task in method public Individual run(Task taskProblem).
* Check taskProblem.isStopCriteria() after every evaluation.
