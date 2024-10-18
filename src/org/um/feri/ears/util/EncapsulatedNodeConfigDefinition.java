package org.um.feri.ears.util;

import org.um.feri.ears.operators.gp.GPOperator;

import java.io.Serializable;

public class EncapsulatedNodeConfigDefinition implements Serializable {
    /**
     * Name of the encapsulated node
     */
    public String EncapsulatedNodeName;
    /**
     * Number of best individuals from which the encapsulated nodes of this type will be created and added to the terminal set
     */
    public int EncapsulatedNodeFrequency;

    /**
     * If true, the encapsulated node will be added to the terminal set immediately. Otherwise, it will be added after behavior for all encapsulated nodes has been defined
     */
    public boolean AddToTerminalSetImmediately;

    /**
     * Pruning operators used after the evolution of encapsulated node
     */
    public String[] PruningOperators;

    /**
     * Run configuration of the encapsulated node
     */
    public RunConfiguration RunConfiguration;
}
