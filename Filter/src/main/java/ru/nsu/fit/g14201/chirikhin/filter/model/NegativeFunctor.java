package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.util.function.Function;

public class NegativeFunctor implements Function<int[], int[]> {
    private static final int MAX_COLOR_VALUE = 255;
    @Override
    public int[] apply(int[] ints) {
        return new int[] {MAX_COLOR_VALUE - ints[0], MAX_COLOR_VALUE - ints[1], MAX_COLOR_VALUE - ints[2]};
    }
}
