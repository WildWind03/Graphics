package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.util.function.Function;

public class GrayscaleFunctor implements Function<int[], int[]> {
    private static final double redK = 0.299;
    private static final double greenK = 0.587;
    private static final double blueK = 0.114;

    @Override
    public int[] apply(int[] ints) {
        int newValue = (int) (redK * ints[0] + greenK * ints[1] + blueK * ints[2]);
        return new int[] {newValue, newValue, newValue};
    }
}
