package org.um.feri.ears.individual.btdemo.gp;

import org.um.feri.ears.individual.btdemo.gp.behaviour.*;
import org.um.feri.ears.individual.btdemo.gp.symbolic.*;
import org.um.feri.ears.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TreeGenerator { // TODO remove in the future
    List<Class<? extends Node>> baseFunctionNodeTypes;
    List<Class<? extends Node>> baseTerminalNodeTypes;

    int minTreeHeight;

    public TreeGenerator(List<Class<? extends Node>> baseFunctionNodeTypes, List<Class<? extends Node>> baseTerminalNodeTypes, int minTreeHeight) {
        this.baseFunctionNodeTypes = baseFunctionNodeTypes;
        this.baseTerminalNodeTypes = baseTerminalNodeTypes;
        this.minTreeHeight = minTreeHeight;
    }

    public Node generateRandomTree(int height, int maxHeight) {
        if(baseFunctionNodeTypes.isEmpty() || baseTerminalNodeTypes.isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }
        if (maxHeight == 1 || (height >= minTreeHeight && Util.rnd.nextBoolean())) {
            // Base case: return a leaf node
            return generateRandomNode(baseTerminalNodeTypes);
        } else {
            // Recursive case: return an internal node
            Node node = generateRandomNode(baseFunctionNodeTypes);
            int maxNumOfChildren = (Util.rnd.nextInt((node.arity - 1) + 1) + 1);
            for (int i = 0; i < maxNumOfChildren; i++) {
                node.insert(i, generateRandomTree(height + 1, maxHeight - 1));
            }
            return node;
        }
    }

    private Node generateRandomNode(List<Class<? extends Node>> nodeTypes) {
        int index = Util.rnd.nextInt(nodeTypes.size());
        try {
            // Create a new instance of the randomly chosen Node type
            Node node = nodeTypes.get(index).getDeclaredConstructor().newInstance();
            return node;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
