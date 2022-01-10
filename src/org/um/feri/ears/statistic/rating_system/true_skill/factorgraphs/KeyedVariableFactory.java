package org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs;

public class KeyedVariableFactory<TKey, TValue> extends VariableFactory<TValue> {

    public KeyedVariableFactory(Func<TValue> variablePriorInitializer) {
        super(variablePriorInitializer);
    }

}
