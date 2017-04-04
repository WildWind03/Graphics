package ru.nsu.fit.g14201.chirikhin.function;

public class LinearInterpolator implements Interpolator {
    @Override
    public double get(double x1, double y1, double x2, double y2, double x) {
        return (y2 - y1) / (x2 - x1) * x + y2
                - ((y2 - y1) / (x2 - x1)) * x2;
    }
}
