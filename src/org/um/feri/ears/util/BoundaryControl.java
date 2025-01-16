package org.um.feri.ears.util;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.unconstrained.Rastrigin;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoundaryControl {

    public static void main(String[] args) {
        ArrayList<Double> position = new ArrayList<>(Arrays.asList(6.5, -6.5));
        ArrayList<Double> lowerLimits = new ArrayList<>(Arrays.asList(-5.12, -5.12));
        ArrayList<Double> upperLimits = new ArrayList<>(Arrays.asList(5.12, 5.12));

        System.out.println("Original Position: " + position);

        // Clamp method
        ArrayList<Double> clampPosition = new ArrayList<>(position);
        BoundaryControl.clamp(clampPosition, lowerLimits, upperLimits);
        System.out.println("After Clamp: " + clampPosition);

        // Reflect method
        ArrayList<Double> reflectPosition = new ArrayList<>(position);
        BoundaryControl.reflect(reflectPosition, lowerLimits, upperLimits);
        System.out.println("After Reflect: " + reflectPosition);

        // Random Reset method
        ArrayList<Double> randomResetPosition = new ArrayList<>(position);
        BoundaryControl.randomReset(randomResetPosition, lowerLimits, upperLimits);
        System.out.println("After Random Reset: " + randomResetPosition);

        // Periodic method
        ArrayList<Double> periodicPosition = new ArrayList<>(position);
        BoundaryControl.periodic(periodicPosition, lowerLimits, upperLimits);
        System.out.println("After Periodic: " + periodicPosition);

        System.out.println("Solution tests:");
        Rastrigin rastrigin = new Rastrigin(2);

        NumberSolution<Double> solution = rastrigin.generateRandomSolution();

        solution.getVariables().set(0, 6.5);
        solution.getVariables().set(1, -6.5);

        rastrigin.makeFeasible(solution, BoundaryControlMethod.RANDOM_RESET);
        System.out.println(solution);

    }

    public enum BoundaryControlMethod {
        CLAMP, // aka clipping
        REFLECT,
        RANDOM_RESET,
        PERIODIC,
        HALVING_DISTANCE
    }

    public static void clamp(List<Double> position, List<Double> lowerLimits, List<Double> upperLimits) {
        for (int i = 0; i < position.size(); i++) {
            if (position.get(i) < lowerLimits.get(i)) {
                position.set(i, lowerLimits.get(i));
            } else if (position.get(i) > upperLimits.get(i)) {
                position.set(i, upperLimits.get(i));
            }
        }
    }

    public static void reflect(List<Double> position, List<Double> lowerLimits, List<Double> upperLimits) {
        for (int i = 0; i < position.size(); i++) {
            if (position.get(i) < lowerLimits.get(i)) {
                position.set(i, lowerLimits.get(i) + (lowerLimits.get(i) - position.get(i)));
            } else if (position.get(i) > upperLimits.get(i)) {
                position.set(i, upperLimits.get(i) - (position.get(i) - upperLimits.get(i)));
            }
        }
    }

    public static void randomReset(List<Double> position, List<Double> lowerLimits, List<Double> upperLimits) {
        for (int i = 0; i < position.size(); i++) {
            if (position.get(i) < lowerLimits.get(i) || position.get(i) > upperLimits.get(i)) {
                position.set(i, RNG.nextDouble(lowerLimits.get(i), upperLimits.get(i)));
            }
        }
    }

    public static void periodic(List<Double> position, List<Double> lowerLimits, List<Double> upperLimits) {
        for (int i = 0; i < position.size(); i++) {
            double range = upperLimits.get(i) - lowerLimits.get(i);
            if (position.get(i) < lowerLimits.get(i)) {
                position.set(i, upperLimits.get(i) - (lowerLimits.get(i) - position.get(i)) % range);
            } else if (position.get(i) > upperLimits.get(i)) {
                position.set(i, lowerLimits.get(i) + (position.get(i) - upperLimits.get(i)) % range);
            }
        }
    }

    //TODO check if correct
    public static void halvingDistance(List<Double> position, List<Double> lowerLimits, List<Double> upperLimits) {
        double threshold = 1e-6; // Small value to avoid infinite loops
        for (int i = 0; i < position.size(); i++) {
            while (position.get(i) < lowerLimits.get(i) || position.get(i) > upperLimits.get(i)) {
                if (position.get(i) < lowerLimits.get(i)) {
                    double newPosition = lowerLimits.get(i) + 0.5 * (position.get(i) - lowerLimits.get(i));
                    if (Math.abs(newPosition - position.get(i)) < threshold) break; // Avoid infinite loop
                    position.set(i, newPosition);
                } else if (position.get(i) > upperLimits.get(i)) {
                    double newPosition = upperLimits.get(i) - 0.5 * (position.get(i) - upperLimits.get(i));
                    if (Math.abs(newPosition - position.get(i)) < threshold) break; // Avoid infinite loop
                    position.set(i, newPosition);
                }
            }
        }
    }

    public static void applyBoundaryControl(List<Double> position, List<Double> lowerLimits, List<Double> upperLimits, BoundaryControlMethod method) {
        switch (method) {
            case CLAMP:
                clamp(position, lowerLimits, upperLimits);
                break;
            case REFLECT:
                reflect(position, lowerLimits, upperLimits);
                break;
            case RANDOM_RESET:
                randomReset(position, lowerLimits, upperLimits);
                break;
            case PERIODIC:
                periodic(position, lowerLimits, upperLimits);
                break;
            default:
                throw new IllegalArgumentException("Unknown boundary control method: " + method);
        }
    }
}
