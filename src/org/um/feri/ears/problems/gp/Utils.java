package org.um.feri.ears.problems.gp;

import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static <T> List<T> list(T... items) {
        List<T> list = new LinkedList<T>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }
}
