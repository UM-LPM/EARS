package org.um.feri.ears.algorithms;

import java.io.Serializable;

public class AlgorithmStepper<T extends Enum<T>> implements Serializable {
    private final T[] values;
    private int currentIndex;
    private boolean includeInitialization;

    public AlgorithmStepper(Class<T> enumClass, boolean includeInitialization) {
        values = enumClass.getEnumConstants();
        currentIndex = 0;
        this.includeInitialization = includeInitialization;
    }

    public T getCurrentValue() {
        return values[currentIndex];
    }

    public void stepForward() {
        currentIndex = (currentIndex + 1) % values.length;
        if(includeInitialization && currentIndex == 0)
            stepForward();
    }

    public void stepBackward() {
        currentIndex = (currentIndex - 1 + values.length) % values.length;
    }

    public boolean isLastStep() {
        return currentIndex == values.length - 1;
    }

    public void reset() {
        currentIndex = 0;
    }
}