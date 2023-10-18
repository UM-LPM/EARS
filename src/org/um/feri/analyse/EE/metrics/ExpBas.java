package org.um.feri.analyse.EE.metrics;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.util.random.RNG;

import java.util.*;


/*
    Author: Mihael Baketarić
    Note, this class computes ExpBas exactly as it would be computed with attraction basins.
    Assumes minimization and valid input parameters.
*/
public class ExpBas{
    /*
        Deterministic means neighbours will be evaluated always in the same (predefined) order, and hence,
        LocalSearchVariant will always follow the same path. Alternative would be to pick the neighbours (semi-)randomly...

        First Improvement always picks the first neighbour that is better than current solution.

        Best Improvement picks the best neighbour after evaluating all the neighbours.
    */
    public enum LocalSearchVariant{
        DeterministicFirstImprovement,
        DeterministicBestImprovement
    }

    /*
        Neighbourhood may be sampled in many different ways. Here we assumed the grid and defined the Sparse and Dense
        types of neighbourhood.

        Sparse neighbourhood means immediate neighbours in a grid (without diagonals).
        Dense neighbourhood means immediate neighbours + diagonal neighbours in a grid.

        For example, an alternative would be to sample the neighbours in some way from a circle with some radius...
    */
    public enum NeighbourhoodType{
        Sparse,
        Dense
    }

    /*
        When Local Search Variant evaluates the neighbours it is important the order of neighbours especially in
        the First Improvement variant. The same holds for the Best Improvement variant although to a lesser degree,
        relatively speaking (equally fit multiple best neighbours have to be present to have an impact).

        Here, we defined two orders of neighbours that are opposite of each other.
        Alternatives include any other order of neighbours.
    */
    public enum NeighbourhoodOrder{
        Reversed,
        NonReversed
    }

    // User-defined settings in the ExpBas measure.
    public static class ExpBasSettings{
        public LocalSearchVariant lsVariant;
        public NeighbourhoodType nType;
        public NeighbourhoodOrder nOrder;
        public ArrayList<Double> steps;  // Distances between two immediate neighbours (non-diagonal) in a grid.
        public DoubleProblem problem;

        public ExpBasSettings(DoubleProblem p, double percentage){
            problem = p;
            steps = new ArrayList<>();
            for(int i = 0; i < problem.getNumberOfDimensions(); i++){
                steps.add((problem.upperLimit.get(i) - problem.lowerLimit.get(i)) * (percentage / 100.0));
            }
        }
    }

    /*
        This method compares the attractors of new solution and its reference solutions
        If they are different, this is exploration (returns true), otherwise it is exploitation (returns false).
     */
    public boolean isExploration(final ArrayList<ArrayList<Double>> refSols, final ArrayList<Double> newSol, final ExpBasSettings setting){
        // 1. Find attractors of reference solutions
        ArrayList<ArrayList<Integer>> refSolsAttractors = new ArrayList<>();
        for(ArrayList<Double> refSol: refSols){
            refSolsAttractors.add(findAttractor(refSol, setting));
        }
        // 2. Find attractors of new solution
        ArrayList<Integer> newSolAttractor = findAttractor(newSol, setting);
        // 3. Check if attractor of new solution is same as attractors of reference solutions.
        // In case the attractor of new solution is same as the attractor of at least one of the reference solutions,
        // then it is exploitation, otherwise it is exploration.
        for(ArrayList<Integer> refSolAttractor: refSolsAttractors){
            if(areAttractorsSame(refSolAttractor, newSolAttractor, setting))
                return false;
        }
        return true;
    }

    // Finds an attractor under specific ExpBas setting
    private ArrayList<Integer> findAttractor(final ArrayList<Double> refSol, final ExpBasSettings setting){
        ArrayList<Integer> currSol = convertDoubleSolToIntegerSol(refSol, setting);
        Double currSolFitness = setting.problem.eval(refSol);
        Double oldFitness = currSolFitness;
        do{
            oldFitness = currSolFitness;
            ArrayList<ArrayList<Integer>> neighbours = getNeighbours(currSol, setting);
            for(ArrayList<Integer> neighbour: neighbours){
                Double neighbourFitness = setting.problem.eval(convertIntegerSolToDoubleSol(neighbour, setting));
                if(setting.lsVariant == LocalSearchVariant.DeterministicFirstImprovement){
                    if(neighbourFitness < currSolFitness){
                        currSol = neighbour;
                        currSolFitness = neighbourFitness;
                        break;
                    }
                }
                else if(setting.lsVariant == LocalSearchVariant.DeterministicBestImprovement){
                    if(neighbourFitness < currSolFitness){
                        currSol = neighbour;
                        currSolFitness = neighbourFitness;
                    }
                }
            }
        } while(oldFitness != currSolFitness);
        return currSol;
    }

    private ArrayList<ArrayList<Integer>> getNeighbours(final ArrayList<Integer> currSol, ExpBasSettings setting) {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();

        if(setting.nType == NeighbourhoodType.Sparse){
            for(int i = 0; i < setting.problem.getNumberOfDimensions(); i++){
                ArrayList<Integer> newSol1 = new ArrayList<Integer>(currSol);
                newSol1.set(i, newSol1.get(i) + 1);
                if(setting.problem.isFeasible(convertIntegerSolToDoubleSol(newSol1, setting)))
                    neighbours.add(newSol1);
                ArrayList<Integer> newSol2 = new ArrayList<Integer>(currSol);
                newSol2.set(i, newSol2.get(i) - 1);
                if(setting.problem.isFeasible(convertIntegerSolToDoubleSol(newSol2, setting)))
                    neighbours.add(newSol2);
            }
        }
        else if(setting.nType == NeighbourhoodType.Dense){
            ArrayList<ArrayList<Integer>> dirs = product(new ArrayList<Integer>(Arrays.asList(-1, 0, 1)), setting.problem.getNumberOfDimensions());
            dirs.remove((int)(dirs.size()/2));
            for(ArrayList<Integer> dir: dirs){
                ArrayList<Integer> newSol = new ArrayList<Integer>();
                for(int i = 0; i < setting.problem.getNumberOfDimensions(); i++){
                    newSol.add(currSol.get(i) + dir.get(i));
                }
                if(setting.problem.isFeasible(convertIntegerSolToDoubleSol(newSol, setting)))
                    neighbours.add(newSol);
            }
        }

        if(setting.nOrder == NeighbourhoodOrder.Reversed)
            Collections.reverse(neighbours);

        return neighbours;
    }

    private boolean areAttractorsSame(ArrayList<Integer> attr1, ArrayList<Integer> attr2, ExpBasSettings setting){
        if(!areFloatsEqual(setting.problem.eval(convertIntegerSolToDoubleSol(attr1, setting)), setting.problem.eval(convertIntegerSolToDoubleSol(attr2, setting))))
            return false;
        if(attr1.equals(attr2))
            return true;
        Set<ArrayList<Integer>> visited = new HashSet<>();
        LinkedList<ArrayList<Integer>> queue = new LinkedList<ArrayList<Integer>>();
        queue.add(attr1);
        visited.add(attr1);
        Double attr1Fitness = setting.problem.eval(convertIntegerSolToDoubleSol(attr1, setting));
        int cnt = 0;
        while(!queue.isEmpty()){
            ArrayList<Integer> currSol = queue.poll();
            ArrayList<ArrayList<Integer>> neighbours = getNeighbours(currSol, setting);
            for(ArrayList<Integer> neighbour: neighbours){
                if(!visited.contains(neighbour)){
                    visited.add(neighbour);
                    Double neighbourFitness = setting.problem.eval(convertIntegerSolToDoubleSol(neighbour, setting));
                    if(areFloatsEqual(neighbourFitness, attr1Fitness)) {
                        if (neighbour.equals(attr2))
                            return true;
                        queue.add(neighbour);
                    }
                }
            }
            cnt++;
            if(cnt >=1000)
                break;
        }
        return false;
    }

    private ArrayList<Integer> convertDoubleSolToIntegerSol(ArrayList<Double> sol, ExpBasSettings setting){
        ArrayList<Integer> convertedSol = new ArrayList<Integer>();
        for(int i = 0; i < sol.size(); i++){
            convertedSol.add((int) Math.round((sol.get(i) - setting.problem.lowerLimit.get(i)) / setting.steps.get(i)));
        }
        return convertedSol;
    }

    private ArrayList<Double> convertIntegerSolToDoubleSol(ArrayList<Integer> sol, ExpBasSettings setting){
        ArrayList<Double> convertedSol = new ArrayList<Double>();
        for(int i = 0; i < sol.size(); i++){
            convertedSol.add(setting.problem.lowerLimit.get(i) + sol.get(i) * setting.steps.get(i));
        }
        return convertedSol;
    }

    private ArrayList<ArrayList<Integer>> product(ArrayList<Integer> a, int r){
        ArrayList<ArrayList<Integer>> result = new ArrayList<>(Collections.nCopies(1, new ArrayList<>()));
        for (Collection<Integer> pool: Collections.nCopies(r, new LinkedHashSet<>(a))) {
            ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
            for (ArrayList<Integer> x : result) {
                for (Integer y : pool) {
                    ArrayList<Integer> z = new ArrayList<>(x);
                    z.add(y);
                    temp.add(z);
                }
            }
            result = temp;
        }
        return result;
    }

    private boolean areFloatsEqual(double f1, double f2){
        final double EPSILON = 1e-15;
        if(Math.abs(f1 - f2) <= EPSILON)
            return true;
        return Math.abs(f1 - f2) <= (EPSILON * Math.max(Math.abs(f1), Math.abs(f2)));
    }

    public ArrayList<ExpBasSettings> getDefaultSettings(DoubleProblem problem){
        final double percentage = 0.1;
        ArrayList<ExpBasSettings> listOfSettings = new ArrayList<>();
        // FSN
        ExpBasSettings es1 = new ExpBasSettings(problem, percentage);
        es1.lsVariant = LocalSearchVariant.DeterministicFirstImprovement;
        es1.nType = NeighbourhoodType.Sparse;
        es1.nOrder = NeighbourhoodOrder.NonReversed;
        listOfSettings.add(es1);

        // FSR
        ExpBasSettings es2 = new ExpBasSettings(problem, percentage);
        es2.lsVariant = LocalSearchVariant.DeterministicFirstImprovement;
        es2.nType = NeighbourhoodType.Sparse;
        es2.nOrder = NeighbourhoodOrder.Reversed;
        listOfSettings.add(es2);

        // FDN
        ExpBasSettings es3 = new ExpBasSettings(problem, percentage);
        es3.lsVariant = LocalSearchVariant.DeterministicFirstImprovement;
        es3.nType = NeighbourhoodType.Dense;
        es3.nOrder = NeighbourhoodOrder.NonReversed;
        listOfSettings.add(es3);

        // FDR
        ExpBasSettings es4 = new ExpBasSettings(problem, percentage);
        es4.lsVariant = LocalSearchVariant.DeterministicFirstImprovement;
        es4.nType = NeighbourhoodType.Dense;
        es4.nOrder = NeighbourhoodOrder.Reversed;
        listOfSettings.add(es4);

        // BSN
        ExpBasSettings es5 = new ExpBasSettings(problem, percentage);
        es5.lsVariant = LocalSearchVariant.DeterministicBestImprovement;
        es5.nType = NeighbourhoodType.Sparse;
        es5.nOrder = NeighbourhoodOrder.NonReversed;
        listOfSettings.add(es5);

        // BSR
        ExpBasSettings es6 = new ExpBasSettings(problem, percentage);
        es6.lsVariant = LocalSearchVariant.DeterministicBestImprovement;
        es6.nType = NeighbourhoodType.Sparse;
        es6.nOrder = NeighbourhoodOrder.Reversed;
        listOfSettings.add(es6);

        // BDN
        ExpBasSettings es7 = new ExpBasSettings(problem, percentage);
        es7.lsVariant = LocalSearchVariant.DeterministicBestImprovement;
        es7.nType = NeighbourhoodType.Dense;
        es7.nOrder = NeighbourhoodOrder.NonReversed;
        listOfSettings.add(es7);

        // BDR
        ExpBasSettings es8 = new ExpBasSettings(problem, percentage);
        es8.lsVariant = LocalSearchVariant.DeterministicBestImprovement;
        es8.nType = NeighbourhoodType.Dense;
        es8.nOrder = NeighbourhoodOrder.Reversed;
        listOfSettings.add(es8);

        return listOfSettings;
    }

    // Simple implementation of DE - EXAMPLE
    public static Object[] runDE(DoubleProblem problem, final ExpBas.ExpBasSettings setting){
        // SO, TO USE EXPBAS, USER HAS TO DEFINE EXPBAS SETTING AND MODIFY THE IMPLEMENTATION OF THEIR OPTIMIZATION ALGORITHM (like here)
        // 1. USER HAS TO PASS REFERENCE SOLUTIONS, NEW SOLUTION, AND SETTING, TO METHOD ExpBas.isExploration(...).
        //    This method checks if the algorithm was in the exploration phase or not (then it is in the exploitation phase).
        // 2. User has to count exploration phases manually for each newly created solution
        // 3. At the end user has to divide number of exploration phases with the number of evaluations, to get the expbas measure

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
        int explorationPhases = 1; // Since random solutions have no parents, exploration must be assumed
        int cntFES = 1;

        // Initialize population
        for(int i = 1; i < NP; i++){
            population.add(new ArrayList<>(Arrays.asList(ArrayUtils.toObject(problem.getRandomVariables()))));
            explorationPhases++; // Since random solutions have no parents, exploration must be assumed
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
                    mySet.add(RNG.nextInt(NP));
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
                    if(RNG.nextDouble() <= CR || i == RNG.nextInt(NP))
                        u.add(v.get(j));
                    else
                        u.add(population.get(i).get(j));
                }

                // To find out if exploration or exploitation we have to pass reference solutions (parents) and new solution (child)
                // Since in DE 4 solutions are used to create new solution, these solutions are reference solutions (parents)
                ArrayList<ArrayList<Double>> referenceSols = new ArrayList<>();
                referenceSols.add(new ArrayList<>(population.get(uniqInds.get(0))));
                referenceSols.add(new ArrayList<>(population.get(uniqInds.get(1))));
                referenceSols.add(new ArrayList<>(population.get(uniqInds.get(2))));
                referenceSols.add(new ArrayList<>(population.get(i)));

                boolean isRandom = false;
                if(!problem.isFeasible(u)){
                    u = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(problem.getRandomVariables())));
                    explorationPhases++; // Since random solutions have no parents, exploration must be assumed
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
                    // We pass reference solutions (parents), new solution (child) and concrete ExpBas setting
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
