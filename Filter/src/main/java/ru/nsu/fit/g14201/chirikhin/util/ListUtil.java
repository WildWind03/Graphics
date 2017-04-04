package ru.nsu.fit.g14201.chirikhin.util;

import java.util.List;

public class ListUtil {
    private ListUtil() {

    }

    public static <T extends Comparable<T>> boolean isSorted(List<T> list) {
        T prevT = list.get(0);
        for (T t : list) {
            if (prevT.compareTo(t) < 0) {
                return false;
            }

            prevT = t;
        }

        return true;
    }
}
