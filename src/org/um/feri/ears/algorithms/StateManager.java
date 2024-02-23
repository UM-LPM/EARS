package org.um.feri.ears.algorithms;

public interface StateManager {

    void saveState(String fileName);
    default void loadState(String fileName) {
        loadState(fileName, false);
    }
    void loadState(String fileName, boolean reevaluate);
}
