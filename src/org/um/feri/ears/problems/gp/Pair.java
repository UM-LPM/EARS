package org.um.feri.ears.problems.gp;

public class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}