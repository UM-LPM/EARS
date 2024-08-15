package org.um.feri.ears.util;

public class EncapsulatedNodeConfigDefinition {
    /**
     * Name of the encapsulated node
     */
    public String EncapsulatedNodeName;
    /**
     * Number of best individuals from which the encapsulated nodes of this type will be created and added to the terminal set
     */
    public int EncapsulatedNodeFrequency;
    /**
     * Run configuration of the encapsulated node
     */
    public RunConfiguration RunConfiguration;
}
