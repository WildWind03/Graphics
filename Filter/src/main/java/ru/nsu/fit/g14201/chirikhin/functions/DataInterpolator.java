package ru.nsu.fit.g14201.chirikhin.functions;

public interface DataInterpolator<V> {
    V apply(Interpolator interpolator, Point<V> point1, Point<V> point2, double arg);
}
