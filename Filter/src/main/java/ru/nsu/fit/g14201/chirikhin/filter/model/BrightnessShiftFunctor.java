package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.util.function.Function;

public class BrightnessShiftFunctor implements Function<int[], int[]> {
    private static final int SHIFT = 128;

    @Override
    public int[] apply(int[] ints) {
        int[] newColor = new int[3];

        newColor[0] = Math.min(ints[0] + SHIFT, 255);
        newColor[1] = Math.min(ints[1] + SHIFT, 255);
        newColor[2] = Math.min(ints[2] + SHIFT, 255);

        return newColor;
    }
}
