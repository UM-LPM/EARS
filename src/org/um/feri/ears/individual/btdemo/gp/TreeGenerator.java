package org.um.feri.ears.individual.btdemo.gp;

import org.um.feri.ears.individual.btdemo.gp.behaviour.*;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeGenerator {
    private static final Random rand = new Random();
    private final List<String> variables;
    private final double minConstValue;
    private final double maxConstValue;

    public TreeGenerator(List<String> variables, double minConstValue, double maxConstValue) {
        this.variables = variables;
        this.minConstValue = minConstValue;
        this.maxConstValue = maxConstValue;
    }

    public Node generateRandomTree(int depth) {
        if (depth <= 0) {
            // Base case: return a leaf node
            if (rand.nextBoolean()) {
                return new ConstNode(rand.nextDouble() * 2 - 1);  // Random constant between -1 and 1
            } else {
                return new VarNode(variables.get(rand.nextInt(variables.size())));  // Random variable
            }
        } else {
            // Recursive case: return an operator node with random children
            List<Node> children = new ArrayList<>();
            int numChildren = rand.nextInt(3) + 1;  // Random number of children between 1 and 3
            for (int i = 0; i < numChildren; i++) {
                children.add(generateRandomTree(depth - 1)); // Decrease depth for each child
            }

            switch (rand.nextInt(4)) {  // Random operator
                case 0:
                    return new AddNode(children);
                case 1:
                    return new SubNode(children);
                case 2:
                    return new MulNode(children);
                case 3:
                    return new DivNode(children);
                default:
                    throw new AssertionError();
            }
        }
    }
}
