package org.um.feri.ears.statistic.true_skill.factorgraphs;

public class DefaultVariable<TValue> extends Variable<TValue> {

    public DefaultVariable() { super(null, "Default"); }

    @Override
    public void setValue(TValue value) {
        throw new UnsupportedOperationException();
    }
}