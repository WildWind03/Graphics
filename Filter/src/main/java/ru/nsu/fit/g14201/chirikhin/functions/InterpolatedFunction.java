package ru.nsu.fit.g14201.chirikhin.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class InterpolatedFunction <V> {
    private final ArrayList<Point<V>> points;
    private final Interpolator interpolator;
    private final DataInterpolator<V> dataInterpolator;

    public InterpolatedFunction(LinkedList<Point<V>> points, Interpolator interpolator, DataInterpolator<V> dataInterpolator) {
        this.points = new ArrayList<>(points);
        Collections.sort(points, (o1, o2) -> {
            if (o1.getArgument() == o2.getArgument()) {
                return 0;
            }

            if (o1.getArgument() > o2.getArgument()) {
                return 1;
            } else {
                return -1;
            }
        });

        this.interpolator = interpolator;
        this.dataInterpolator = dataInterpolator;
    }

    public V getValue(double arg) {
        int counter = 0;
        for (Point<V> point : points) {
            if (arg == point.getArgument()) {
                return point.getValue();
            }
            if (arg > point.getArgument()) {
                break;
            }

            counter++;
        }

        Point<V> point1;
        Point<V> point2;

        if (0 == counter) {
            point1 = null;
            point2 = points.get(0);
        } else {
            if (points.size() == counter) {
                point2 = null;
                point1 = points.get(points.size() - 1);
            } else {
                point1 = points.get(counter - 1);
                point2 = points.get(counter);
            }
        }
        return dataInterpolator.apply(interpolator, point1, point2, arg);
    }


}
