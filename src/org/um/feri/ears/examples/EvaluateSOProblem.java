package org.um.feri.ears.examples;

import org.um.feri.ears.problems.unconstrained.cec2014.F1;

import java.util.Arrays;

public class EvaluateSOProblem {

    public static void main(String[] args) {

        F1 p = new F1(2);

        double[] t = {50.3, 64.9};

        System.out.println(Arrays.toString(p.getDecisionSpaceOptima()[0]));

        System.out.println(p.eval(p.getDecisionSpaceOptima()[0]));

        System.out.println(p.eval(t));
    }
}
