package org.um.feri.ears.statistic.true_skill.factorgraphs;

public class KeyedVariableFactory<TKey, TValue> extends VariableFactory<TValue> {

    public KeyedVariableFactory(Func<TValue> variablePriorInitializer) {
        super(variablePriorInitializer);
    }

}
