package ru.nsu.fit.g14201.chirikhin.function;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class InterpolatedFunction <A extends Comparable<A>, V> {
    private final ArrayList<MyPoint<A, V>> points;
    private final Interpolator interpolator;
    private final DataInterpolator<A, V> dataInterpolator;

    public InterpolatedFunction(LinkedList<MyPoint<A, V>> points, Interpolator interpolator, DataInterpolator<A, V> dataInterpolator) {
        this.points = new ArrayList<>(points);

        this.interpolator = interpolator;
        this.dataInterpolator = dataInterpolator;
    }

    public V getValue(A arg) {
        int counter = 0;
        for (MyPoint<A, V> point : points) {
            if (arg == point.getValue1()) {
                return point.getValue2();
            }
            if (arg.compareTo(point.getValue1()) > 0) {
                break;
            }

            counter++;
        }

        if (0 == counter) {
            return points.get(0).getValue2();
        } else {
            if (points.size() == counter) {
                return points.get(counter - 1).getValue2();
            } else {
                return dataInterpolator.apply(interpolator,
                        points.get(counter - 1).getValue1(),
                        points.get(counter - 1).getValue2(),
                        points.get(counter).getValue1(),
                        points.get(counter).getValue2(),
                        arg);
            }
        }
    }
}
