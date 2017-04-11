package ru.nsu.fit.g14201.chirikhin.isolines.util;

public class Util {
    private static final int START_X = -2;
    private static final int END_X = 2;
    private static final int START_Y = -4;
    private static final int END_Y = 4;

    private Util() {

    }

    public static double function(double x, double y) {
        return (Math.exp(-x*x-y*y/2) * Math.cos(4*x) + Math.exp(-3*((x+0.5)*(x+0.5)+y*y/2)));
    }

    public static double toRealX(int u, int width) {
        return (END_X - START_X) * (double) u / (double) width + START_X;
    }

    public static double toRealY(int v, int height) {
        return (END_Y - START_Y) * (double) v / (double) height + START_Y;
    }
}
