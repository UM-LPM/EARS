package org.um.feri.ears.algorithms.so.sgc;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;
import java.util.List;

public class StochasticGradientDescent extends NumberAlgorithm {

    @AlgorithmParameter(name = "learningRate")
    private double learningRate;
    @AlgorithmParameter(name = "batchSize")
    private int batchSize;

    private NumberSolution<Double> currentSolution;

    public StochasticGradientDescent() {
        this(0.01, 1); // Default: learning rate = 0.01, batch size = 1
    }

    public StochasticGradientDescent(double learningRate, int batchSize) {
        super();
        this.learningRate = learningRate;
        this.batchSize = batchSize;
        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("SGD", "Stochastic Gradient Descent",
                "@book{ruder2016overview," +
                        "title={An overview of gradient descent optimization algorithms}," +
                        "author={Ruder, Sebastian}," +
                        "year={2016}," +
                        "url={https://arxiv.org/abs/1609.04747}" +
                        "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        currentSolution = task.generateRandomEvaluatedSolution();

        while (!task.isStopCriterion()) {
            ArrayList<Double> variables = currentSolution.getVariables();
            ArrayList<Double> gradients = computeGradient(variables);

            if(gradients == null)
                return currentSolution;

            // Update variables based on gradients
            for (int i = 0; i < variables.size(); i++) {
                variables.set(i, variables.get(i) - learningRate * gradients.get(i));
            }

            NumberSolution<Double> newSolution = new NumberSolution<>(variables);
            task.problem.makeFeasible(newSolution);

            if (task.isStopCriterion()) {
                return currentSolution;
            }
            task.eval(newSolution);

            if (task.problem.isFirstBetter(newSolution, currentSolution)) {
                currentSolution = newSolution;
            }
        }
        return currentSolution;
    }

    private ArrayList<Double> computeGradient(List<Double> variables) throws StopCriterionException {
        ArrayList<Double> gradient = new ArrayList<>(variables.size());

        for (int i = 0; i < variables.size(); i++) {
            double original = variables.get(i);
            double epsilon = 1e-6;

            variables.set(i, original + epsilon);
            NumberSolution<Double> forwardSolution = new NumberSolution<>(variables);
            if (task.isStopCriterion())
                return null;

            task.eval(forwardSolution);
            double forwardValue = forwardSolution.getObjective(0);

            variables.set(i, original - epsilon);
            NumberSolution<Double> backwardSolution = new NumberSolution<>(variables);

            if (task.isStopCriterion())
                return null;

            task.eval(backwardSolution);
            double backwardValue = backwardSolution.getObjective(0);

            double partialDerivative = (forwardValue - backwardValue) / (2 * epsilon);
            gradient.add(partialDerivative);

            // Restore original variable value
            variables.set(i, original);
        }
        return gradient;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
