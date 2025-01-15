package org.um.feri.ears.util;

import java.io.Serializable;
import java.util.List;

public class RunConfiguration implements Serializable {
    public String Name;
    public int NumberOfReruns;
    public EARSConfiguration EARSConfiguration;
    public List<EncapsulatedNodeConfigDefinition> EncapsulatedNodeDefinitions;
    public Object UnityConfiguration;
    public Object UnityConfigurationMasterTournamentGraph;
    public Object UnityConfigurationConvergenceGraph;
}
