package org.um.feri.ears.statistic.true_skill.factorgraphs;

public class VariableFactory<TValue> {

    // using a Func<TValue> to encourage fresh copies in case it's overwritten
    protected final Func<TValue> variablePriorInitializer;

    public VariableFactory(Func<TValue> variablePriorInitializer) {
        this.variablePriorInitializer = variablePriorInitializer;
    }

}